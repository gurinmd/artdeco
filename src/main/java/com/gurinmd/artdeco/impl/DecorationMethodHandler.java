package com.gurinmd.artdeco.impl;

import com.gurinmd.artdeco.impl.annotations.Decorates;
import com.gurinmd.artdeco.impl.util.MethodUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by MGurin on 23.01.2017.
 */
public class DecorationMethodHandler<T> implements InvocationHandler{

    private T delegateObject;
    private Object[] decoratingMethodsObjects;

    public DecorationMethodHandler(T delegateObject) {
        this.delegateObject = delegateObject;
    }

    public void setDecoratingMethodsObjects(Object... decoratingMethodsObjects){
        this.decoratingMethodsObjects = decoratingMethodsObjects;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Object targetDecoratingObject = null;
        Method targetDecoratingMethod = null;

        for (Object decoratingMethodObject : decoratingMethodsObjects){
            Method[] methods = decoratingMethodObject.getClass().getMethods();
            for (Method candidateDecoratingMethod : methods){
                if (MethodUtils.equals(candidateDecoratingMethod,method)
                        && candidateDecoratingMethod.getDeclaredAnnotations()!=null
                        && candidateDecoratingMethod.getDeclaredAnnotation(Decorates.class)!=null){
                    targetDecoratingMethod  = candidateDecoratingMethod;
                    targetDecoratingObject = decoratingMethodObject;
                }
            }
        }

        if (targetDecoratingMethod != null && targetDecoratingObject !=null){
            boolean assesible = targetDecoratingMethod.isAccessible();
            targetDecoratingMethod.setAccessible(true);
            Object value = targetDecoratingMethod.invoke(targetDecoratingObject,args);
            targetDecoratingMethod.setAccessible(assesible);
            return value;
        } else {
            return method.invoke(delegateObject,args);
        }
    }
}
