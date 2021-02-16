package com.skoltech.sensors.development.config;

import com.skoltech.sensors.development.domain.entity.Measure;
import org.bitbucket.gt_tech.spring.data.querydsl.value.operators.experimental.QuerydslHttpRequestContextAwareServletFilter;
import org.bitbucket.gt_tech.spring.data.querydsl.value.operators.experimental.QuerydslPredicateArgumentResolverBeanPostProcessor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Order
public class QueryDslValueOperatorsConfig {

    @Bean
    public FilterRegistrationBean querydslHttpRequestContextAwareServletFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new QuerydslHttpRequestContextAwareServletFilter(
                querydslHttpRequestContextAwareServletFilterMappings()));
        bean.setAsyncSupported(false);
        bean.setEnabled(true);
        bean.setName("querydslHttpRequestContextAwareServletFilter");
        bean.setUrlPatterns(Arrays.asList(new String[]{"/api/*"}));
        bean.setOrder(Ordered.LOWEST_PRECEDENCE);
        return bean;
    }

    private Map<String, Class<?>> querydslHttpRequestContextAwareServletFilterMappings() {
        Map<String, Class<?>> mappings = new HashMap<>();
        mappings.put("/api/history", Measure.class);
        return mappings;
    }

    @Bean
    public QuerydslPredicateArgumentResolverBeanPostProcessor querydslPredicateArgumentResolverBeanPostProcessor(
            QuerydslBindingsFactory factory) {
        ConversionService conversionService = DefaultConversionService.getSharedInstance();
        return new QuerydslPredicateArgumentResolverBeanPostProcessor(factory, conversionService,
                new Class[]{Date.class, LocalDate.class, Timestamp.class, Boolean.class, boolean.class});
    }
}
