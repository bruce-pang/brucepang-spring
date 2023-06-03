package com.brucepang.myspring.factory;

/**
 * @author BrucePang
 */

public interface InitializingBean {

    /**
     * Bean初始化方法
     * @throws Exception
     */
    void afterPropertiesSet() throws Exception;

}