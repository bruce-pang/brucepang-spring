package com.brucepang.myspring.service;

import com.brucepang.myspring.annotation.AutoWired;
import com.brucepang.myspring.annotation.Component;
import com.brucepang.myspring.annotation.MyValue;
import com.brucepang.myspring.annotation.Scope;
import com.brucepang.myspring.factory.BeanNameAware;
import com.brucepang.myspring.factory.InitializingBean;
import com.brucepang.myspring.factory.Value;

/**
 * @author BrucePang
 */
//@Component("userService")
@Component
//@Scope("prototype")
public class UserServiceImpl implements InitializingBean, UserService, BeanNameAware {

    @AutoWired
    private OrderService orderService;

    @MyValue("test666")
    private String test;

    private String beanName;

    public void test(){
        System.out.println(test);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("初始化..");
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
