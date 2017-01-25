package com.gurinmd.artdeco.impl.util;

import com.gurinmd.artdeco.impl.annotations.TargetObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by MGurin on 23.01.2017.
 */
public class AnnotationUtils {
    public static boolean doesContainAnnotation(Annotation[] annotations, Annotation annotationToFind) {
        if (annotations == null || annotations.length == 0) {
            return false;
        }

        for (Annotation annotation : annotations) {
            if (Objects.equals(annotation, annotationToFind)) {
                return true;
            }
        }
        return false;
    }

    public static void injectTargetObject(Object decoratingObject,Object targetObject){
        Optional<Field> fieldOptional = getFieldForTargetObject(decoratingObject.getClass());
        if (fieldOptional.isPresent()){
            Field field = fieldOptional.get();
            if (field.getType().isAssignableFrom(targetObject.getClass())){
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                try {
                    field.set(decoratingObject, targetObject);
                    field.setAccessible(accessible);
                } catch (IllegalAccessException oops){}
            }
        }
    }

    public static boolean isFieldAnnotatedAsTargetObject(Field field){
        return field.getAnnotation(TargetObject.class) != null;
    }

    public static Optional<Field> getFieldForTargetObject(Class<?> clazz){
        Optional<Field> result = Optional.empty();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields){
            if (field.getAnnotation(TargetObject.class)!=null){
                result = Optional.ofNullable(field);
            }
        }
        return result;
    }

}
