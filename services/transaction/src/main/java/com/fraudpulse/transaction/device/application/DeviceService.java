package com.fraudpulse.transaction.device.application;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.fraudpulse.transaction.device.persistance.Device;
import com.fraudpulse.transaction.device.persistance.DeviceRepository;


@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository repository;

    @Transactional
    public Device create(String deviceRef) {
        Device device = new Device(deviceRef);
        return repository.save(device);
    }

    @Transactional(readOnly = true)
    public Optional<Device> findByDeviceRef(String deviceRef) {
        return repository.findByDeviceRef(deviceRef);
    }

}
