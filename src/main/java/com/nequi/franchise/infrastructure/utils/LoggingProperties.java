package com.nequi.franchise.infrastructure.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@ConfigurationProperties(prefix = "franchise.logging")
@Getter
@Setter
public class LoggingProperties {

    private Http http = new Http();
    private Method method = new Method();

    @Getter
    @Setter
    public static class Http {
        private boolean enabled = true;
        private boolean includeHeaders = true;
        private boolean includeBody = true;
        private int maxBodySize = 1024 * 1024; // 1MB
        private List<String> excludePaths = List.of("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**");
    }

    @Getter
    @Setter
    public static class Method {
        private boolean enabled = true;
        private boolean logParameters = true;
        private boolean logResult = true;
        private Set<String> excludePackages = Set.of();
    }
}
