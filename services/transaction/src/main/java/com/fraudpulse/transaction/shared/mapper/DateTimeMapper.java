package com.fraudpulse.transaction.shared.mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class DateTimeMapper {

    public OffsetDateTime toOffsetDateTime(Instant instant) {
        return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    public Instant toInstant(OffsetDateTime offsetDateTime) {
        return offsetDateTime.toInstant();
    }
}
