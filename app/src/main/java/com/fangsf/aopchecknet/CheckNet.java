package com.fangsf.aopchecknet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by fangsf on 2018/10/25.
 * Useful:
 */

@Target(ElementType.METHOD)  // 标记是在方法上
@Retention(RetentionPolicy.RUNTIME)  // RUNTIME  标识在运行时起作用
public @interface CheckNet {


}
