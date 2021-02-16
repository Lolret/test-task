package com.skoltech.sensors.development.domain;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import lombok.AllArgsConstructor;

import javax.persistence.EntityManager;

/**
 * alexander.adamov created on 03.09.2020
 */
@AllArgsConstructor
public class EntityIdResolver implements ObjectIdResolver {

    private EntityManager entityManager;

    @Override
    public void bindItem(
            final ObjectIdGenerator.IdKey id,
            final Object pojo) {
    }

    @Override
    public Object resolveId(final ObjectIdGenerator.IdKey id) {
        return this.entityManager.find(id.scope, id.key);
    }

    @Override
    public ObjectIdResolver newForDeserialization(final Object context) {
        return this;
    }

    @Override
    public boolean canUseFor(final ObjectIdResolver resolverType) {
        return false;
    }
}