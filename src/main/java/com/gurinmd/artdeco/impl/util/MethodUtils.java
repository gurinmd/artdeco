package com.gurinmd.artdeco.impl.util;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Created by MGurin on 23.01.2017.
 */
public class MethodUtils {
    public static boolean equals(Method method1, Method method2){
        if (method1 == null || method2 == null){
            return false;
        } else if (method1 == method2){
            return true;
        } else {
            return Objects.equals(method1.getName(),method2.getName())
                    && (method1.isAccessible() == method2.isAccessible())
                    && (method1.getReturnType().equals(method2.getReturnType()))
                    && (java.util.Arrays.equals(method1.getParameters(),method2.getParameters()));
        }
    }
}
