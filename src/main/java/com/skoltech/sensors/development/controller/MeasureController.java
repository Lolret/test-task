package com.skoltech.sensors.development.controller;

import com.querydsl.core.types.Predicate;
import com.skoltech.sensors.development.domain.entity.Measure;
import com.skoltech.sensors.development.dto.LatestValueDTO;
import com.skoltech.sensors.development.repository.MeasureRepo;
import com.skoltech.sensors.development.service.MeasureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class MeasureController {

    private final MeasureService sensorService;
    private final MeasureRepo sensorRepo;

    @GetMapping(value = "/history")
    @ResponseBody
    public ResponseEntity<List<Measure>> getSensor(
            @QuerydslPredicate(root = Measure.class)
                    Predicate predicate,
            @RequestParam(required = false)
                    MultiValueMap<String, String> paramArrays,
            @PageableDefault(page = 0, size = 1000) Pageable pageable
    ) {
        predicate = sensorService.preparePredicate(paramArrays, predicate);
        List<Measure> sensor = sensorService.getSensor(predicate, pageable);
        return ResponseEntity.ok()
                .header("Result-Count", String.valueOf(sensor.size()))
                .header("Total-Count", String.valueOf(sensorRepo.count(predicate)))
                .body(sensor);
    }


    //    @Transactional
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createResources(@RequestBody @Valid List<Measure> sensor) {
        sensorService.createSensor(sensor);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping(value = "/latest")
    @ResponseBody
    public ResponseEntity<List<LatestValueDTO>> getLatestValueBySensor(
            @RequestParam Long id
    ) {
        return ResponseEntity.ok()
                .body(sensorRepo.findAggregate(id));
    }

    @GetMapping(value = "/avg")
    @ResponseBody
    public ResponseEntity<Map<Long, Double>> getAvgValueGroupBySubject(
            @QuerydslPredicate(root = Measure.class)
                    Predicate predicate
    ) {
        return ResponseEntity.ok()
                .body(sensorService.getAvgValueGroupBySubject(predicate));
    }
}
