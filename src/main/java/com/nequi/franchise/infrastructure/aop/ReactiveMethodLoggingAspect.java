package com.nequi.franchise.infrastructure.aop;

import com.nequi.franchise.infrastructure.utils.LoggingProperties;
import com.nequi.franchise.infrastructure.utils.LoggingUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class ReactiveMethodLoggingAspect {

    private final LoggingUtils loggingUtils;
    private final LoggingProperties loggingProperties;

    @Around("@annotation(logExecution)")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint, LogExecution logExecution) throws Throwable {
        if (!loggingProperties.getMethod().isEnabled()) {
            return joinPoint.proceed();
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = signature.getName();

        Map<String, Object> executionContext = createExecutionContext(className, methodName, joinPoint, logExecution);
        StopWatch stopWatch = new StopWatch();

        try {
            Object result = joinPoint.proceed();

            if (result instanceof Mono) {
                return enhanceMono((Mono<?>) result, executionContext, stopWatch, logExecution);
            } else if (result instanceof Flux) {
                return enhanceFlux((Flux<?>) result, executionContext, stopWatch, logExecution);
            } else {
                stopWatch.stop();
                logSuccess(executionContext, result, stopWatch.getTotalTimeMillis(), logExecution);
                return result;
            }

        } catch (Exception ex) {
            stopWatch.stop();
            logError(executionContext, ex, stopWatch.getTotalTimeMillis());
            throw ex;
        }
    }

    private Map<String, Object> createExecutionContext(String className, String methodName,
                                                       ProceedingJoinPoint joinPoint, LogExecution logExecution) {
        Map<String, Object> context = loggingUtils.createBaseLogEntry();
        context.put("class", className);
        context.put("method", methodName);
        context.put("operation", logExecution.value());

        if (logExecution.logParameters() && loggingProperties.getMethod().isLogParameters()) {
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                context.put("parameters", Arrays.toString(args));
            }
        }

        return context;
    }

    private Mono<?> enhanceMono(Mono<?> mono, Map<String, Object> context, StopWatch stopWatch, LogExecution logExecution) {
        return mono
                .doOnSubscribe(subscription -> {
                    stopWatch.start();
                    loggingUtils.logDebug("Method Started", context);
                })
                .doOnSuccess(result -> {
                    stopWatch.stop();
                    logSuccess(context, result, stopWatch.getTotalTimeMillis(), logExecution);
                })
                .doOnError(throwable -> {
                    stopWatch.stop();
                    logError(context, throwable, stopWatch.getTotalTimeMillis());
                });
    }

    private Flux<?> enhanceFlux(Flux<?> flux, Map<String, Object> context, StopWatch stopWatch, LogExecution logExecution) {
        return flux
                .doOnSubscribe(subscription -> {
                    stopWatch.start();
                    loggingUtils.logDebug("Method Started", context);
                })
                .doOnComplete(() -> {
                    stopWatch.stop();
                    logSuccess(context, "Flux completed", stopWatch.getTotalTimeMillis(), logExecution);
                })
                .doOnError(throwable -> {
                    stopWatch.stop();
                    logError(context, throwable, stopWatch.getTotalTimeMillis());
                });
    }

    private void logSuccess(Map<String, Object> context, Object result, long executionTime, LogExecution logExecution) {
        context.put("status", "SUCCESS");
        context.put("executionTimeMs", executionTime);

        if (logExecution != null && logExecution.logResult() && loggingProperties.getMethod().isLogResult() && result != null) {
            context.put("result", loggingUtils.truncateString(result.toString(), 500));
        }

        loggingUtils.logInfo("Method Completed", context);
    }

    private void logError(Map<String, Object> context, Throwable throwable, long executionTime) {
        context.put("status", "ERROR");
        context.put("executionTimeMs", executionTime);
        context.put("errorType", throwable.getClass().getSimpleName());
        context.put("errorMessage", throwable.getMessage());

        loggingUtils.logError("Method Failed", context, throwable);
    }
}
