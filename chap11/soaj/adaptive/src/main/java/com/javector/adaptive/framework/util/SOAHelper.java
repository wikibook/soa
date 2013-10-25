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

import com.javector.adaptive.framework.interfaces.SimpleTypeConvertor;
import com.javector.adaptive.framework.constants.SOAConstant;
import com.javector.adaptive.framework.model.RuleDTO;
import com.javector.soaj.SoajRuntimeException;
import com.javector.soaj.logging.SoajLogger;
import com.javector.soaj.logging.LoggingFactory;

import javax.xml.namespace.QName;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * Auther: Rohit Agarwal
 * Date: Jun 3, 2006
 * Time: 3:03:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class SOAHelper {


    private static SoajLogger logger = LoggingFactory.getLogger(SOAHelper.class.getName());


    public static Object getConvertorValueForXML(String convertorClass, RuleDTO ruleDTO , Object sourceObject, QName xmlType) {
        if(!SOAUtil.validateHandler(convertorClass, SimpleTypeConvertor.class))
            throw new SoajRuntimeException("check the convertor defined for rule = " + ruleDTO.getJavaName() +
                    "it must extend " + SimpleTypeConvertor.class.getName());
        SimpleTypeConvertor  convertor = (SimpleTypeConvertor)SOAUtil.getDefaultInstance(convertorClass);
        String getterMethod = ruleDTO.getPropertyMappings().get(SOAConstant.JAVA_GETTER_METHOD_OVERRIDE);
        String localPart = xmlType.getLocalPart();
        Object javaValue;
        if(getterMethod!=null) {
            javaValue = SOAReflectionUtil.getPropertyValue(sourceObject,getterMethod);
        } else {
        javaValue = SOAReflectionUtil.getPropertyValueDefault(sourceObject,ruleDTO.getJavaName());
        }
        String javaClassName = BaseReflectionUtil.getJavaClassNameForXmlType(localPart);
        Class<?> classParam;
        logger.info("java class for corresponding xml tyep  = " + javaClassName);
        try {
            classParam = Class.forName(javaClassName);
        } catch (ClassNotFoundException e) {
            throw new SoajRuntimeException("unable to create object for the class name =" + javaClassName, e);
        }
        Object convertedValue = convertor.serialize(javaValue);
        if(convertedValue.getClass().getName().equals(javaClassName)) {
            return convertedValue;
        }
        logger.info("converted java value to be set = " +convertedValue);
        try {
            Constructor[] constructors = classParam.getConstructors();
            for(Constructor constructor: constructors) {
                if(constructor.getParameterTypes().length == 1&& constructor.getParameterTypes()[0].equals(String.class)) {
                    Class<?> parameterClass = Class.forName(javaClassName);
                    return parameterClass.getConstructor(String.class).newInstance(new Object[]{convertedValue.toString()});
                }
            }

        } catch (Exception e) {
            throw new SoajRuntimeException("unable to create instance for the class = " + classParam.getName(),e);
        }
        return null;
    }


    public static Object getConvertorValueForJava(String convertorClass, String javaName, Object xmlValue, String javaClass) {
        SimpleTypeConvertor convertor = null;
        try {
            convertor = (SimpleTypeConvertor)Class.forName(convertorClass).newInstance();
        } catch (Exception e) {
            throw new SoajRuntimeException("class not found or cannot be instantiated please check if the class in classPath and default constructor exits for class = " + javaClass,e);
        }
        Class<?> javaClassDef = null;
        try {
            javaClassDef = Class.forName(javaClass);
        } catch (ClassNotFoundException e) {
            throw new SoajRuntimeException("class not found please check if the class in classPath  = " + javaClass,e);
        }
        Method[] methods = javaClassDef.getMethods();
        Class paramClass = null ;
        for(Method method: methods) {
            String setterMethod = "set" +javaName;
            if(method.getName().equalsIgnoreCase(setterMethod)) {
                paramClass = method.getParameterTypes()[0];
            }
        }
        try {
            Method[] convertorMethods = convertor.getClass().getDeclaredMethods();
            Class<? extends Class> serializerParamClass = null;
            for(Method method: convertorMethods) {
                if(method.getName().equals(SOAConstant.CONVERTOR_DESERIALIZE_METHOD_NAME)){
                    serializerParamClass = method.getParameterTypes()[0].getClass();
                }
            }

            Constructor[] paramConstructors = serializerParamClass.getConstructors();
            for(Constructor paramConstructor: paramConstructors) {
                if(paramConstructor.getParameterTypes().length == 1&& paramConstructor.getParameterTypes()[0].equals(String.class)) {
                    Class<?> parameterClass = Class.forName(paramClass.getName());
                    xmlValue =  parameterClass.getConstructor(String.class).newInstance(new Object[]{xmlValue.toString()});
                }

                Object javaFieldValue = convertor.deserialize(xmlValue);
                if(javaFieldValue.getClass().getName().equals(paramClass.getName())) {
                    return javaFieldValue;
                } else {
                    Object o = null;
                    logger.info("converted java value to be set = " +javaFieldValue);

                    Constructor[] constructors = paramClass.getConstructors();
                    for(Constructor constructor: constructors) {
                        if(constructor.getParameterTypes().length == 1&& constructor.getParameterTypes()[0].equals(String.class)) {
                            Class<?> parameterClass = Class.forName(paramClass.getName());
                            return parameterClass.getConstructor(String.class).newInstance(new Object[]{javaFieldValue.toString()});
                        }
                    }

                }
            }

        }catch (Exception e) {
            throw new SoajRuntimeException("unable to create instance for the class = " + paramClass.getName(),e);
        }
        return null;  //To change body of created methods use File | Settings | File Templates.

    }
}
