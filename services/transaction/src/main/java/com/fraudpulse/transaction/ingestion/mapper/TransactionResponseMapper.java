package com.fraudpulse.transaction.ingestion.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.fraudpulse.transaction.config.MapStructConfig;
import com.fraudpulse.transaction.ingestion.api.dto.response.TransactionResponse;
import com.fraudpulse.transaction.ingestion.persistance.Transaction;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, config = MapStructConfig.class)
public interface TransactionResponseMapper {
    TransactionResponse toResponse(Transaction transaction);
}