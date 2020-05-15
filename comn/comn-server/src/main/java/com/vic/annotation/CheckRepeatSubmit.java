package com.vic.annotation;

import java.lang.annotation.*;

/**
 * 校验重复提交
 * @author 罗利华
 * date: 2020/3/30 16:16
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckRepeatSubmit {

    int delaySeconds() default 5;
}
