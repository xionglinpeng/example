package com.xlp.example.configuration;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class HelloWorldSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        //这里可以根据逻辑加载不同的自动配置
        return new String[]{HelloWorldConfiguration.class.getName()};
    }
}
