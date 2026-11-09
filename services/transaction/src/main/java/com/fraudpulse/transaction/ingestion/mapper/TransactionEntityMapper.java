package com.fraudpulse.transaction.ingestion.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import com.fraudpulse.transaction.ingestion.api.dto.request.CreateTransactionRequest;
import com.fraudpulse.transaction.ingestion.persistance.Transaction;
import com.fraudpulse.transaction.account.persistance.Account;
import com.fraudpulse.transaction.config.MapStructConfig;
import com.fraudpulse.transaction.device.persistance.Device;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, config = MapStructConfig.class)
public interface TransactionEntityMapper {

    @Mapping(target = "account", source = "account")
    @Mapping(target = "device", source = "device")
    @Mapping(target = "createdAt", ignore = true)
    Transaction toEntity(CreateTransactionRequest createTransactionRequest, Account account, Device device);

}