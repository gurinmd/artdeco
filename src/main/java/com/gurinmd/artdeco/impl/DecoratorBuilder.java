package com.gurinmd.artdeco.impl;

import com.gurinmd.artdeco.impl.util.AnnotationUtils;

import java.lang.reflect.Proxy;

/**
 * Created by MGurin on 23.01.2017.
 */
public class DecoratorBuilder<T> {

    private T targetObject;

    private Object[] decoratingInstances;

    private Class<T> iface;

    public static <T> DecoratorBuilder<T> forInterface(Class<T> iface){
        if (iface.isInterface()){
            DecoratorBuilder<T> builder = new DecoratorBuilder<T>();
            builder.iface = iface;
            return builder;
        } else {
            throw new IllegalArgumentException(iface.getSimpleName()+ " is not an interface!");
        }
    }

    public void setTargetObject(T object){
        targetObject = object;
    }

    public void addDecoratingMethodInstance(Object... instances){
        for (Object instance : instances){
            AnnotationUtils.injectTargetObject(instance,targetObject);
        }
        this.decoratingInstances = instances;
    }

    public T buildDecorator(){
        DecorationMethodHandler decorationMethodHandler = new DecorationMethodHandler(this.targetObject);
        decorationMethodHandler.setDecoratingMethodsObjects(this.decoratingInstances);
        return (T) Proxy.newProxyInstance(this.targetObject.getClass().getClassLoader(), new Class[]{this.iface}, decorationMethodHandler);
    }
}
