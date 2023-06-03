package com.brucepang.myspring;

import com.brucepang.myspring.config.AppConfig;
import com.brucepang.myspring.context.BrucePangApplicationContext;
import com.brucepang.myspring.service.UserService;

/**
 * @author BrucePang
 */
public class Test {
    public static void main(String[] args) {
        // 1、扫描-->创建单例Bean
        BrucePangApplicationContext applicationContext = new BrucePangApplicationContext(AppConfig.class);
        // 2、获取配置类中的对象
        // UserService userService = (UserService) applicationContext.getBean("userService");
        System.out.println(applicationContext.getBean("userService"));
        System.out.println(applicationContext.getBean("userService"));
        System.out.println(applicationContext.getBean("orderService"));
        // 3、调用对象的方法
        //userService.test();
    }
}
