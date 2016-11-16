package com.yuyh.easydao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Element with this notation will be a column in the database table.<br>
 * By default, NULL value is not allowed, to allow a NULL value, use (nullable = true) explicitly
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    boolean nullable() default false;
}