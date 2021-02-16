package com.skoltech.sensors.development.dto;

import lombok.Data;

/**
 * alexander.adamov created on 16.02.2021
 */

public interface LatestValueDTO {
    Long getSensorId();
    Double getValue();
}
