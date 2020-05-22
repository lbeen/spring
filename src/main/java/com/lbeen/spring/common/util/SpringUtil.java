package com.lbeen.spring.common.util;

import org.springframework.context.ApplicationContext;

public class SpringUtil{
    private static ApplicationContext applicationContext;

//    @Override
    public static void setApplicationContext(ApplicationContext applicationContext){
        if (SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext;
        }
    }

    //获取applicationContext
    private static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过name获取 Bean.
    public static <T> T getBean(String name){
        @SuppressWarnings("unchecked")
        T bean = (T) getApplicationContext().getBean(name);
        return bean;
    }
}
