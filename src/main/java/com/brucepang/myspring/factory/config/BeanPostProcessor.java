package com.brucepang.myspring.factory.config;

/**
 * @author BrucePang
 */
public interface BeanPostProcessor {

    default Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    default Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }
}
