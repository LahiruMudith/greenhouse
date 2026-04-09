package com.greenhouse.zone.service;

import com.greenhouse.zone.client.IotApiClient;
import com.greenhouse.zone.model.Zone;
import com.greenhouse.zone.repository.ZoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final IotApiClient iotApiClient;

    public ZoneService(ZoneRepository zoneRepository, IotApiClient iotApiClient) {
        this.zoneRepository = zoneRepository;
        this.iotApiClient = iotApiClient;
    }

    public Zone createZone(Zone zone) {
        if (zone.getMinTemp() >= zone.getMaxTemp()) {
            throw new IllegalArgumentException("minTemp must be less than maxTemp");
        }
        // Register device via IoT API
        String deviceId = iotApiClient.registerDevice(zone.getName());
        zone.setDeviceId(deviceId);
        return zoneRepository.save(zone);
    }

    public Zone getZone(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone not found: " + id));
    }

    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }

    public Zone updateZone(Long id, Zone updated) {
        Zone zone = getZone(id);
        if (updated.getMinTemp() >= updated.getMaxTemp()) {
            throw new IllegalArgumentException("minTemp must be less than maxTemp");
        }
        zone.setName(updated.getName());
        zone.setMinTemp(updated.getMinTemp());
        zone.setMaxTemp(updated.getMaxTemp());
        zone.setDescription(updated.getDescription());
        return zoneRepository.save(zone);
    }

    public void deleteZone(Long id) {
        zoneRepository.deleteById(id);
    }
}
