package com.xlp.example.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnSystemProperty.class)
public @interface ConditionalOnSystemProperty {

    String name();

    String value();
}
