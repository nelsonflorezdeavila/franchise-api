package com.nequi.franchise.infrastructure.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingUtils {

    private final ObjectMapper objectMapper;

    public void logInfo(String message, Map<String, Object> logData) {
        try {
            log.info("{}: {}", message, objectMapper.writeValueAsString(logData));
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize log data for: {}", message, e);
            log.info("{}: {}", message, logData.toString());
        }
    }

    public void logError(String message, Map<String, Object> logData, Throwable throwable) {
        try {
            log.error("{}: {}", message, objectMapper.writeValueAsString(logData), throwable);
        } catch (JsonProcessingException e) {
            log.error("{}: {} - Serialization failed", message, logData.toString(), throwable);
        }
    }

    public void logDebug(String message, Map<String, Object> logData) {
        if (log.isDebugEnabled()) {
            try {
                log.debug("{}: {}", message, objectMapper.writeValueAsString(logData));
            } catch (JsonProcessingException e) {
                log.debug("{}: {}", message, logData.toString());
            }
        }
    }

    public void logWarn(String message, Map<String, Object> logData) {
        try {
            log.warn("{}: {}", message, objectMapper.writeValueAsString(logData));
        } catch (JsonProcessingException e) {
            log.warn("{}: {}", message, logData.toString());
        }
    }

    public Map<String, Object> createBaseLogEntry() {
        Map<String, Object> logEntry = new LinkedHashMap<>();
        logEntry.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return logEntry;
    }

    public String truncateString(String value, int maxLength) {
        if (value == null) return null;
        return value.length() > maxLength ? value.substring(0, maxLength) + "..." : value;
    }
}
