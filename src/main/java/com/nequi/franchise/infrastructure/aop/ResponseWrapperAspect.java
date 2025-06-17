package com.nequi.franchise.infrastructure.aop;

import com.nequi.franchise.infrastructure.dto.ApiResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ResponseWrapperAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restController() {
        // Pointcut for all controller methods
    }

    @Around("restController()")
    public Object wrapResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object result = joinPoint.proceed();
            
            // If the result is already an ApiResponse, return as is
            if (result instanceof ApiResponse) {
                return result;
            }
            
            // If the result is a ResponseEntity, extract the body
            if (result instanceof ResponseEntity) {
                ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
                Object body = responseEntity.getBody();
                
                // If the body is already an ApiResponse, return as is
                if (body instanceof ApiResponse) {
                    return responseEntity;
                }
                
                // Create a new ApiResponse with the response body
                ApiResponse<?> apiResponse = ApiResponse.success(body);
                return new ResponseEntity<>(apiResponse, responseEntity.getHeaders(), responseEntity.getStatusCode());
            }
            
            // For other types, wrap in ApiResponse
            return ApiResponse.success(result);
            
        } catch (Exception e) {
            // Handle exceptions and wrap in error response
            return ApiResponse.error(e.getMessage());
        }
    }
}
