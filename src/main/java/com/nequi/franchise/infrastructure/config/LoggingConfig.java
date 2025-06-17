package com.nequi.franchise.infrastructure.config;

import com.nequi.franchise.infrastructure.utils.LoggingProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
public class LoggingConfig {
}
