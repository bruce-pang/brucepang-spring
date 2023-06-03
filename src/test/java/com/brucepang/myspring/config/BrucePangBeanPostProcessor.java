package com.brucepang.myspring.config;

import com.brucepang.myspring.annotation.Component;
import com.brucepang.myspring.annotation.MyValue;
import com.brucepang.myspring.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author BrucePang
 */
@Component
public class BrucePangBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        System.out.println(beanName + "前置处理器执行...");
        for (Field f : bean.getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(MyValue.class)) {
                MyValue annotation = f.getAnnotation(MyValue.class);
                String value = annotation.value();
                f.setAccessible(true); // 设置私有属性可访问
                f.set(bean, value);
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        //System.out.println(beanName + "后置处理器执行...");

        if (beanName.equals("userServiceImpl")) {
           return Proxy.newProxyInstance(BrucePangBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
               @Override
               public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                   // 切面逻辑
                   System.out.println("切面逻辑前...");
                   Object invoke = method.invoke(bean, args);
                   System.out.println("切面逻辑后...");
                   return invoke;
               }
           });
        }

        return bean;
    }
}
