package com.nequi.franchise.infrastructure.utils;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RequestUtils {

    private static final String REQUEST_ID_HEADER = "X-Request-ID";

    public String getOrGenerateRequestId(ServerHttpRequest request) {
        String requestId = request.getHeaders().getFirst(REQUEST_ID_HEADER);
        return StringUtils.hasText(requestId) ? requestId : UUID.randomUUID().toString();
    }

    public String getClientIpAddress(ServerHttpRequest request) {
        return Optional.ofNullable(request.getHeaders().getFirst("X-Forwarded-For"))
                .filter(StringUtils::hasText)
                .orElse(Optional.ofNullable(request.getHeaders().getFirst("X-Real-IP"))
                        .filter(StringUtils::hasText)
                        .orElse(Optional.ofNullable(request.getRemoteAddress())
                                .map(address -> address.getAddress().getHostAddress())
                                .orElse("unknown")));
    }

    public Map<String, String> extractSafeHeaders(ServerHttpRequest request) {
        return request.getHeaders().entrySet().stream()
                .filter(entry -> !isSensitiveHeader(entry.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> String.join(",", entry.getValue())
                ));
    }

    public boolean isSensitiveHeader(String headerName) {
        String lowerName = headerName.toLowerCase();
        return lowerName.contains("authorization") ||
                lowerName.contains("password") ||
                lowerName.contains("token") ||
                lowerName.contains("secret") ||
                lowerName.contains("key");
    }

    public boolean shouldSkipPath(String path) {
        return path.startsWith("/actuator") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs");
    }
}
