package com.brucepang.myspring.factory;

/**
 * @author BrucePang
 */
public interface BeanNameAware extends Aware{

    void setBeanName(String beanName);
}
