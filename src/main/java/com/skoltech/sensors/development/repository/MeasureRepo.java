package com.skoltech.sensors.development.repository;

import com.skoltech.sensors.development.dto.AvgValueDTO;
import com.skoltech.sensors.development.dto.LatestValueDTO;
import com.skoltech.sensors.development.domain.entity.Measure;
import com.skoltech.sensors.development.domain.entity.QMeasure;
import org.bitbucket.gt_tech.spring.data.querydsl.value.operators.ExpressionProviderFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeasureRepo extends JpaRepository<Measure, Long>,
        QuerydslPredicateExecutor<Measure>
        , QuerydslBinderCustomizer<QMeasure> {

    @Override
    default void customize(QuerydslBindings bindings, QMeasure root) {
        bindings.bind(Number.class)
                .all(ExpressionProviderFactory::getPredicate);
        bindings.bind(root.value)
                .all(ExpressionProviderFactory::getPredicate);
        bindings.bind(root.time)
                .all(ExpressionProviderFactory::getPredicate);
        bindings.bind(root.subject.id).as("objectId")
                .all(ExpressionProviderFactory::getPredicate);
        bindings.bind(root.sensor.id).as("id")
                .all(ExpressionProviderFactory::getPredicate);
    }

    @Query(value = "SELECT sensor_id as sensorId, value " +
            "FROM ( " +
            "         SELECT sensor_id " +
            "              , last_value(value) " +
            "                OVER (PARTITION BY sensor_id " +
            "                    ORDER BY time " +
            "                    ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as value " +
            "              , ROW_NUMBER() " +
            "                OVER (PARTITION BY sensor_id " +
            "                    ORDER BY time)                                            as rn " +
            "         FROM measure " +
            "    where subject_id = :subjectId  " +
            "     ) as dt " +
            "WHERE rn = 1;",
            nativeQuery = true)
    List<LatestValueDTO>
    findAggregate(
            @Param("subjectId") Long subjectId);


    @Query(value = "select subject_id as objectId, avg(value) as avgValue from measure group by subject_id;",
            nativeQuery = true)
    List<AvgValueDTO> findAverage();

}
