package com.skoltech.sensors.development;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skoltech.sensors.development.service.MeasureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories
@SpringBootConfiguration
@RequiredArgsConstructor
@Slf4j
public class Application {
    private final ObjectMapper objectMapper;
    private final MeasureService sensorService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
