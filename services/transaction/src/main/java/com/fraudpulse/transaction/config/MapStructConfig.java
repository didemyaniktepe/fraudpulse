package com.fraudpulse.transaction.config;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.fraudpulse.transaction.shared.mapper.DateTimeMapper;
import com.fraudpulse.transaction.shared.mapper.JsonPayloadMapper;

@MapperConfig(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {DateTimeMapper.class, JsonPayloadMapper.class}
)
public interface MapStructConfig {
}