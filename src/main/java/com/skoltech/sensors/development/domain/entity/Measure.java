package com.skoltech.sensors.development.domain.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skoltech.sensors.development.domain.EntityIdResolver;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;


@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public class Measure {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonIgnore
    private Long id;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id",
            resolver = EntityIdResolver.class,
            scope = Sensor.class)
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "sensor_id")
    @JsonProperty("sensorId")
    private Sensor sensor;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id",
            resolver = EntityIdResolver.class,
            scope = Subject.class)
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "subject_id")
    @JsonProperty("objectId")
    private Subject subject;

    @NotNull
    private Long time;
    @NotNull
    private Double value;
}
