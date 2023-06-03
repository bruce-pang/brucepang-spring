package com.brucepang.myspring.service;

import com.brucepang.myspring.annotation.Component;
import com.brucepang.myspring.annotation.ComponentScan;
import com.brucepang.myspring.annotation.Scope;

/**
 * @author BrucePang
 */
@Component("userService")
//@Scope("prototype")
@Scope("singleton")
public class UserService {

    public void test(){
        System.out.println("test");
    }
}
