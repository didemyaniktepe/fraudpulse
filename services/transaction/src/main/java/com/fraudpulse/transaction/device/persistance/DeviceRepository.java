package com.fraudpulse.transaction.device.persistance;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {
    Optional<Device> findByDeviceRef(String deviceRef);

    @Modifying
    @Query(value = """
            INSERT INTO devices (id, device_ref)
            VALUES (:id, :deviceRef)
            ON CONFLICT (device_ref) DO NOTHING
            """, nativeQuery = true)
    void insertIfAbsent(@Param("id") UUID id, @Param("deviceRef") String deviceRef);
}
