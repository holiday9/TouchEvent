package com.sevent.toucheventverification;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by htyuan on 15-3-27.
 */
public class ReflectUtil {
    public static Object getFiledValue(Object obj, String filedName) {
        Exception ee = null;
        try {
            Field nameFiled = getDeclaredField(obj, filedName);
            nameFiled.setAccessible(true);

            return nameFiled.get(obj);
        } catch (Exception e) {
            ee = e;
            ee.printStackTrace();
        }

        return null;
    }

    public static void setFiledValue(Object object, String filedName, Object value) {
        Exception ee = null;
        try {
            Field filed = getDeclaredField(object, filedName);
            filed.setAccessible(true);

            filed.set(object, value);
        } catch (Exception e) {
            ee = e;
            ee.printStackTrace();
        }
    }

    public static Object invokeMethod(Object receiver, String funcName, Object[] args, Class<?>[] argClasses)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = getDeclaredMethod(receiver, funcName, argClasses);
        method.setAccessible(true);

        return method.invoke(receiver, args);
    }

    private static Field getDeclaredField(Object object, String fieldName) {
        Field field = null;
        for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (Exception e) {

            }
        }

        return field;
    }

    private static Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
        Method method = null;
        for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                break;
            } catch (Exception e) {

            }
        }

        return method;
    }
}
