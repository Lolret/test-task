package com.skoltech.sensors.development.utils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

/**
 * alexander.adamov created on 12.02.2021
 */
public class PredicateUtil {
    public static Predicate mergePredicates(Predicate predicate, Predicate additionalPredicate) {
        return new BooleanBuilder(predicate).and(additionalPredicate);
    }
}
