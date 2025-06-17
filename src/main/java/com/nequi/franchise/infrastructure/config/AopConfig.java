package com.nequi.franchise.infrastructure.config;

import com.nequi.franchise.infrastructure.aop.ResponseWrapperAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AopConfig {

    @Bean
    public ResponseWrapperAspect responseWrapperAspect() {
        return new ResponseWrapperAspect();
    }
}
