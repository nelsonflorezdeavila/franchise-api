package com.nequi.franchise.infrastructure.filter;

import com.nequi.franchise.infrastructure.utils.LoggingProperties;
import com.nequi.franchise.infrastructure.utils.LoggingUtils;
import com.nequi.franchise.infrastructure.utils.RequestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class ReactiveLoggingWebFilter implements WebFilter {

    private final LoggingUtils loggingUtils;
    private final RequestUtils requestUtils;
    private final LoggingProperties loggingProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (!loggingProperties.getHttp().isEnabled() ||
                requestUtils.shouldSkipPath(exchange.getRequest().getURI().getPath())) {
            return chain.filter(exchange);
        }

        String requestId = requestUtils.getOrGenerateRequestId(exchange.getRequest());
        long startTime = System.currentTimeMillis();

        exchange.getResponse().getHeaders().add("X-Request-ID", requestId);

        ServerWebExchange loggingExchange = createLoggingExchange(exchange, requestId);

        return logRequest(loggingExchange, requestId)
                .then(chain.filter(loggingExchange))
                .doOnSuccess(unused -> logResponse(loggingExchange, requestId, startTime))
                .doOnError(throwable -> logError(loggingExchange, requestId, startTime, throwable))
                .onErrorResume(throwable -> {
                    logError(loggingExchange, requestId, startTime, throwable);
                    return Mono.error(throwable);
                });
    }

    private ServerWebExchange createLoggingExchange(ServerWebExchange exchange, String requestId) {
        if (shouldLogRequestBody(exchange.getRequest())) {
            ServerHttpRequest decoratedRequest = new CachingServerHttpRequestDecorator(exchange.getRequest());
            return exchange.mutate().request(decoratedRequest).build();
        }
        return exchange;
    }

    private boolean shouldLogRequestBody(ServerHttpRequest request) {
        if (!loggingProperties.getHttp().isIncludeBody()) return false;

        HttpMethod method = request.getMethod();
        return (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH) &&
                request.getHeaders().getContentLength() > 0 &&
                request.getHeaders().getContentLength() <= loggingProperties.getHttp().getMaxBodySize();
    }

    private Mono<Void> logRequest(ServerWebExchange exchange, String requestId) {
        ServerHttpRequest request = exchange.getRequest();
        Map<String, Object> requestLog = createRequestLog(request, requestId);

        if (shouldLogRequestBody(request)) {
            return logRequestWithBody(exchange, requestLog);
        } else {
            loggingUtils.logInfo("HTTP Request", requestLog);
            return Mono.empty();
        }
    }

    private Map<String, Object> createRequestLog(ServerHttpRequest request, String requestId) {
        Map<String, Object> requestLog = loggingUtils.createBaseLogEntry();
        requestLog.put("requestId", requestId);
        requestLog.put("method", request.getMethod().name());
        requestLog.put("uri", request.getURI().toString());
        requestLog.put("path", request.getURI().getPath());
        requestLog.put("remoteAddress", requestUtils.getClientIpAddress(request));
        requestLog.put("contentType", request.getHeaders().getFirst("Content-Type"));
        requestLog.put("contentLength", request.getHeaders().getFirst("Content-Length"));

        if (loggingProperties.getHttp().isIncludeHeaders()) {
            requestLog.put("headers", requestUtils.extractSafeHeaders(request));
        }

        return requestLog;
    }

    private Mono<Void> logRequestWithBody(ServerWebExchange exchange, Map<String, Object> requestLog) {
        return DataBufferUtils.join(exchange.getRequest().getBody())
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return new String(bytes, StandardCharsets.UTF_8);
                })
                .doOnNext(body -> {
                    requestLog.put("body", loggingUtils.truncateString(body, 1000));
                    loggingUtils.logInfo("HTTP Request", requestLog);
                })
                .onErrorResume(throwable -> {
                    requestLog.put("bodyError", "Failed to read request body: " + throwable.getMessage());
                    loggingUtils.logInfo("HTTP Request", requestLog);
                    return Mono.empty();
                })
                .then();
    }

    private void logResponse(ServerWebExchange exchange, String requestId, long startTime) {
        long executionTime = System.currentTimeMillis() - startTime;
        ServerHttpResponse response = exchange.getResponse();

        Map<String, Object> responseLog = loggingUtils.createBaseLogEntry();
        responseLog.put("requestId", requestId);
        responseLog.put("method", exchange.getRequest().getMethod().name());
        responseLog.put("uri", exchange.getRequest().getURI().getPath());
        responseLog.put("statusCode", response.getStatusCode() != null ? response.getStatusCode().value() : "unknown");
        responseLog.put("executionTimeMs", executionTime);
        responseLog.put("contentType", response.getHeaders().getFirst("Content-Type"));

        loggingUtils.logInfo("HTTP Response", responseLog);
    }

    private void logError(ServerWebExchange exchange, String requestId, long startTime, Throwable throwable) {
        long executionTime = System.currentTimeMillis() - startTime;

        Map<String, Object> errorLog = loggingUtils.createBaseLogEntry();
        errorLog.put("requestId", requestId);
        errorLog.put("method", exchange.getRequest().getMethod().name());
        errorLog.put("uri", exchange.getRequest().getURI().getPath());
        errorLog.put("executionTimeMs", executionTime);
        errorLog.put("errorType", throwable.getClass().getSimpleName());
        errorLog.put("errorMessage", throwable.getMessage());

        loggingUtils.logError("HTTP Error", errorLog, throwable);
    }

    private static class CachingServerHttpRequestDecorator extends ServerHttpRequestDecorator {
        private final Flux<DataBuffer> cachedBody;

        public CachingServerHttpRequestDecorator(ServerHttpRequest delegate) {
            super(delegate);
            this.cachedBody = super.getBody().cache();
        }

        @Override
        public Flux<DataBuffer> getBody() {
            return cachedBody;
        }
    }
}
