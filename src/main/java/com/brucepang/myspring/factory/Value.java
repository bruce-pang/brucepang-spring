package com.brucepang.myspring.factory;

import java.lang.annotation.*;

/**
 * @author BrucePang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
public @interface Value {

    String value();

}