/*
* Copyright 2006-2007 Javector Software LLC
*
* Licensed under the GNU General Public License, Version 2 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.gnu.org/copyleft/gpl.html
*
* THE SOURCE CODE AND ACCOMPANYING FILES ARE PROVIDED WITHOUT ANY WARRANTY,
* WRITTEN OR IMPLIED.
*
* The copyright holder provides this software under other licenses for those
* wishing to include it with products or systems not licensed under the GPL.
* Contact licenses@javector.com for more information.
*/
package com.javector.adaptive.framework.util;

import com.javector.adaptive.framework.model.RuleDTO;
import com.javector.soaj.util.IOUtil;
import com.javector.soaj.SoajRuntimeException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;

public class SOAReflectionUtil {
  /**
   * This as of now does not support constructors and needs default constructors
   * todo: add the support for the same
   *
   * @param javaClass
   
   * @return javaObject
   */
  public static Object createJavaObject(String javaClass) {
    try {
      return Class.forName(javaClass).newInstance();
    } catch (Exception e) {
      try {
        Class<?> aClass = Class.forName(javaClass);
        Constructor[] constructors = aClass.getConstructors();
        for(Constructor constructor : constructors) {
          Class<?>[] parameterTypes = constructor.getParameterTypes();
          if(parameterTypes.length==1&& parameterTypes[0].getName().equals("java.lang.String")){
            return constructor.newInstance(new Object[]{"defalut"});
          }
        }
      } catch (Exception e1) {
        throw new SoajRuntimeException("Exception while creating the java Object for class:" + javaClass,e1);
      }
      throw new SoajRuntimeException("Exception while creating the java Object for class:" + javaClass);
    }
    
  }
  
  public static Object getPropertyValueDefault(Object sourceObjectz, String propertyName) {
    Object value = null;
    try {
      Class<? extends Object> clazz = sourceObjectz.getClass();
      Method method = clazz.getMethod("get" + SOAUtil.convertToTitleCase(propertyName), new Class[]{});
      value = method.invoke(sourceObjectz);
    } catch (Exception e) {
      throw new SoajRuntimeException("Exception while getting the Property value for propertyName =" +propertyName, e);
    }
    return value;
  }
  
  public static Object getPropertyValue(Object sourceObjectz, String getterMethodName) {
    Object value = null;
    try {
      Class<? extends Object> clazz = sourceObjectz.getClass();
      Method method = clazz.getMethod(getterMethodName, new Class[]{});
      value = method.invoke(sourceObjectz);
    } catch (Exception e) {
      throw new SoajRuntimeException("Exception while getting the Property value for propertyName = " +getterMethodName, e);
    }
    return value;
  }
  
  
  
  
  
  static HashMap<String, Class> map = new HashMap<String, Class>();
  
  static {
    map.put(float.class.getName(), Float.class);
    map.put(double.class.getName(), Double.class);
    map.put(byte.class.getName(), Byte.class);
    map.put(int.class.getName(), Integer.class);
    map.put(short.class.getName(), Short.class);
    map.put(long.class.getName(), Long.class);
    map.put(boolean.class.getName(), Boolean.class);
    
  }
  
  public static Class<?> getWrapperClass(String className) {
    return map.get(className);
  }
  
  
  
  public static void setPropertyValue(Object sourceJavaObject, String overRidePropertyName, Object childJavaObject) {
    String javaClass = sourceJavaObject.getClass().getName();
    Class<? extends Object> clazz = sourceJavaObject.getClass();
    Method[] methods = clazz.getMethods();
    int count = 0;
    Method setterMethod = null;
    for(Method method: methods ) {
      if(method.getName().equals(overRidePropertyName)) {
        setterMethod = method;
        count++;
      }
    }
    if(count==1) {
      invokeSetterMethod(setterMethod,childJavaObject,sourceJavaObject);
    }
  }
  
  public static void setPropertyValueDefault(Object sourceJavaObject, String propertyName, Object childJavaObject) {
    
    Method settermethod = null;
    try {
      Class<? extends Object> clazz = sourceJavaObject.getClass();
      Method[] methods = clazz.getMethods();
      
      int count = 0;
      for (Method method : methods) {
        if (method. getName().equals("set" + SOAUtil.convertToTitleCase(propertyName))) {
          settermethod = method;
          count++;
        }
      }
      if(count==1) {
        invokeSetterMethod(settermethod, childJavaObject, sourceJavaObject);
      }
    }catch(Exception ex) {
      throw new SoajRuntimeException("unable to invoke setter method on sourceJavaObject = " +sourceJavaObject.getClass().getName()
          + IOUtil.NL + "for setter method = " +settermethod.getName() +IOUtil.NL +
          "for value = " +childJavaObject.getClass().getName());
    }
    
  }
  
  
  private static void invokeSetterMethod(Method settermethod, Object childJavaObject, Object sourceJavaObject) {
    try {
      Class wrapperClass = settermethod.getParameterTypes()[0];
      if (wrapperClass.isPrimitive()) {
        wrapperClass = getWrapperClass(wrapperClass.getName());
      }
      if (childJavaObject.getClass().isAssignableFrom(wrapperClass)) {
        settermethod.invoke(sourceJavaObject, new Object[]{childJavaObject});
      } else {
        settermethod.invoke(sourceJavaObject, new Object[]{castMe(wrapperClass, childJavaObject)});
      }
      
      
    } catch (Exception e) {
      e.printStackTrace();
      Class<?>[] parameterTypes = settermethod.getParameterTypes();
      for (Class<?> parameterType : parameterTypes) {
        System.out.println("parameterType = " + parameterType);
      }
      throw new SoajRuntimeException("Exception while setting the Property= " +settermethod.getName()+" on object = " + sourceJavaObject.getClass().getName()+"" +
          "for property values =" + childJavaObject.getClass().getName() + " expected " + parameterTypes, e);
    }
  }
  
  public static boolean isInterface(Class toClassType) {
    if(toClassType.getConstructors().length == 0) {
      return true;
    }
    return false;
  }
  
  public static Object castMe(Class toClazzType, Object value) {
    if(isInterface(toClazzType)) {
      return value;
    }
    Object objectz;
    Constructor constructor;
    Class inClazz[] = new Class[]{String.class};
    Object paramObjec[] = new Object[]{value.toString()};
    //TODO handle Calender type
    try {
      constructor = toClazzType.getConstructor(inClazz);
      objectz = constructor.newInstance(paramObjec);
    } catch (Exception e) {
      throw new SoajRuntimeException("Exception while casting the Property value " + value+ " in class name =" + toClazzType.getName(), e);
    }
    
    return objectz;
  }
  
  public static boolean isArrayType(Object propertyValue) {
    return propertyValue.getClass().isArray();
  }
  
  public static boolean isArray(Object targetJavaObject, String javaField, String setterORgetter) {
    
    Field[] fields = targetJavaObject.getClass().getFields();
    for (Field field : fields) {
      if (field.getName().equals(javaField)) {
        return field.getType().isArray();
      }
    }
    javaField = SOAUtil.convertToTitleCase(javaField);
    Method[] methods = targetJavaObject.getClass().getMethods();
    for (Method method : methods) {
      if (method.getName().equals(setterORgetter + javaField)) {
        if ("set".equals(setterORgetter)) {
          return method.getParameterTypes()[0].isArray();
        } else if ("get".equals(setterORgetter)) {
          return method.getReturnType().isArray();
        }
      }
    }
    throw new SoajRuntimeException("No field or method found " + javaField + " in " + targetJavaObject);
    
  }
  
  public static boolean isCollection(Object targetJavaObject, String javaField,String setterORgetter) {
    
    Field[] fields = targetJavaObject.getClass().getFields();
    for (Field field : fields) {
      if (field.getName().equals(javaField)) {
        return field.getType().isArray();
      }
    }
    javaField = SOAUtil.convertToTitleCase(javaField);
    Method[] methods = targetJavaObject.getClass().getMethods();
    for (Method method : methods) {
      if (method.getName().equals(setterORgetter + javaField)) {
        if ("set".equals(setterORgetter)) {
          return method.getParameterTypes()[0].isAssignableFrom(Collection.class);
        } else if ("get".equals(setterORgetter)) {
          return method.getParameterTypes()[0].isAssignableFrom(Collection.class);
        }
      }
    }
    throw new SoajRuntimeException("No field or method found " + javaField + " in " + targetJavaObject);
    
  }
  
  public static String getPropertyClassName(Object javaObject, RuleDTO ruleDTO) {
    
    Field[] fields = javaObject.getClass().getFields();
    String javaField = ruleDTO.getJavaName();
    for (Field field : fields) {
      if (field.getName().equals(javaField)) {
        return field.getType().getName();
      }
    }
    javaField = SOAUtil.convertToTitleCase(javaField);
    Method[] methods = javaObject.getClass().getMethods();
    for (Method method : methods) {
      if (method.getName().equals("get" + javaField)) {
        return method.getReturnType().getName();
      }
    }
    throw new SoajRuntimeException("No field or method found " + javaField + " in " + javaObject);
    
  }
  
  
  public static boolean isCollectionType(Object propertyValue) {
    return propertyValue.getClass().isAssignableFrom(Collection.class) ||
    Collection.class.isInstance(propertyValue);
  }
  
}

