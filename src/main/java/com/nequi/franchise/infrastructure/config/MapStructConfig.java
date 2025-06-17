package com.nequi.franchise.infrastructure.config;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    disableSubMappingMethodsGeneration = true
)
public interface MapStructConfig {
}
