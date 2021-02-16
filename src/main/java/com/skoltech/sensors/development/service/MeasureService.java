package com.skoltech.sensors.development.service;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Predicate;
import com.skoltech.sensors.development.domain.entity.Measure;
import com.skoltech.sensors.development.domain.entity.QMeasure;
import com.skoltech.sensors.development.repository.MeasureRepo;
import com.skoltech.sensors.development.utils.PredicateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MeasureService extends QuerydslRepositorySupport {
    private final MeasureRepo sensorRepo;

    @Autowired
    public MeasureService(MeasureRepo sensorRepo) {
        super(Measure.class);
        this.sensorRepo = sensorRepo;
    }

    public List<Measure> getSensor(Predicate predicate, Pageable pageable) {
        return IterableUtils.toList(sensorRepo.findAll(predicate, pageable));
    }

    public List<Measure> createSensor(List<Measure> sensor) {
        return sensorRepo.saveAll(sensor);
    }

    public Predicate preparePredicate(MultiValueMap<String, String> paramArrays, Predicate predicate) {
        if (!paramArrays.isEmpty()) {
            String from = paramArrays.getFirst("from");
            String to = paramArrays.getFirst("to");
            if (from != null) {
                predicate = PredicateUtil.mergePredicates(predicate, QMeasure.measure.time.goe(Long.parseLong(from)));
            }
            if (to != null) {
                predicate = PredicateUtil.mergePredicates(predicate, QMeasure.measure.time.loe(Long.parseLong(to)));
            }
        }
        return predicate;
    }

    public void createResourcesBatch(List<Measure> resources) {
        AtomicInteger counter = new AtomicInteger();
        int idx = 0;
        for (List<Measure> x : resources
                .stream()
                .collect(Collectors
                        .groupingBy(it -> counter.getAndIncrement() / 500))
                .values()) {
            log.info("Saving batch: " + x.size() + ". Progress: " + idx++ * 500 + "/" + resources.size() + ". %: " + (idx * 500) / resources.size());
            sensorRepo.saveAll(x);
            if ((idx * 500) > 1_000_000) break;
        }

    }

    public Map<Long, Double> getAvgValueGroupBySubject(Predicate predicate) {
        QMeasure measure = QMeasure.measure;
        return from(measure).where(predicate)
                .groupBy(measure.subject.id)
                .transform(GroupBy.groupBy(measure.subject.id).as(measure.value.avg()));
    }
}
