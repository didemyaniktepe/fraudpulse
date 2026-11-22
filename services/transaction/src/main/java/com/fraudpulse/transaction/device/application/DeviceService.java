package com.fraudpulse.transaction.device.application;

import java.util.UUID;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.fraudpulse.transaction.device.persistance.Device;
import com.fraudpulse.transaction.device.persistance.DeviceRepository;
import com.fraudpulse.transaction.shared.exception.ApiException;
import com.fraudpulse.transaction.shared.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository repository;

    @Transactional
    public Device findOrCreate(String deviceRef) {
        if (deviceRef == null) {
            return null;
        }
        return repository.findByDeviceRef(deviceRef).orElseGet(() -> createIfAbsent(deviceRef));
    }

    private Device createIfAbsent(String deviceRef) {
        repository.insertIfAbsent(UUID.randomUUID(), deviceRef);
        return repository.findByDeviceRef(deviceRef).orElseThrow(() -> ApiException.of(ErrorCode.RESOLUTION_FAILED));
    }

}
