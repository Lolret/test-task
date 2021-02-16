package com.skoltech.sensors.development.repository;

import com.skoltech.sensors.development.domain.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepo extends JpaRepository<Sensor, Long> {
}
