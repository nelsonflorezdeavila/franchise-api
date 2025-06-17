package com.nequi.franchise.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.i18n.AcceptHeaderLocaleContextResolver;
import org.springframework.web.server.i18n.LocaleContextResolver;

import java.util.Locale;

@Configuration
public class LocaleConfig {

    @Bean
    public LocaleContextResolver localeResolver() {
        AcceptHeaderLocaleContextResolver resolver = new AcceptHeaderLocaleContextResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }
}
