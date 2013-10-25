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
package com.javector.adaptive.jaxb.impl;

import com.javector.adaptive.framework.interfaces.BaseAdaptiveDeserializer;
import com.javector.adaptive.framework.interfaces.XMLMapping;
import com.javector.adaptive.framework.dto.DeserializeBindingRuleDTO;
import com.javector.adaptive.framework.model.RuleDTO;
import com.javector.adaptive.framework.model.JavaWrapDTO;
import com.javector.adaptive.framework.constants.SOAConstant;
import com.javector.adaptive.framework.util.SOAReflectionUtil;
import com.javector.soaj.SoajRuntimeException;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rohit agarwal
 * Date: Apr 6, 2006
 * Time: 7:18:47 PM
 * Default handler class mentioned in adaptinMapping.xml file for handling
 * complex type deserializtion for complex java types
 */


public class JAXBComplexTypeDeserializer extends BaseAdaptiveDeserializer {


    /*** Overrriden method from BaseadaptiveDeserializer
     * for creating of java Objects which are complex type
     * @param bindingRuleDTO
     * @return
     * @throws SoajRuntimeException
     */

    public Object createJavaChildObject(DeserializeBindingRuleDTO bindingRuleDTO) {
        Object targetJavaObject = bindingRuleDTO.getTargetJavaObject();
        RuleDTO ruleDTO = bindingRuleDTO.getRuleDTO();
        Object childJavaObject;
        if(ruleDTO.getWrapDTO()== null) {
            String childClassName = ruleDTO.getJavaClass();
            XMLMapping xmlTypeMapping;
            if(childClassName != null) {
                xmlTypeMapping = new XMLMapping(ruleDTO.getXmlType(),ruleDTO.getXmlName(), childClassName);
                return adaptiveContext.deserialize(bindingRuleDTO.getPropertyValue(),xmlTypeMapping);
            }
            childClassName = SOAReflectionUtil.getPropertyClassName(targetJavaObject,ruleDTO);
            xmlTypeMapping = new XMLMapping(ruleDTO.getXmlType(),ruleDTO.getXmlName(), childClassName);
            return adaptiveContext.deserialize(bindingRuleDTO.getPropertyValue(),xmlTypeMapping);
        } else {
            String javaType = bindingRuleDTO.getRuleDTO().getJavaClass();
            Object propertyValue = bindingRuleDTO.getPropertyValue();
            if(SOAReflectionUtil.isCollectionType(propertyValue) || propertyValue.getClass().isArray()) {
                 childJavaObject =  handleCollection(bindingRuleDTO);
            }else{
                try {
                    XMLMapping mapping = new XMLMapping(ruleDTO.getXmlType(),ruleDTO.getXmlName(),javaType);
                    childJavaObject =  adaptiveContext.deserialize(propertyValue,mapping);
                } catch (Exception e) {
                    throw new SoajRuntimeException("unable to create class for java type = " + javaType,e);
                }
            }
            String[] javaNames = ruleDTO.getWrapDTO().getJavaWrapDTO().getJavaName().split(SOAConstant.ADAPTIVE_PATH_SEPERATOR);
            String[] javaClasses = ruleDTO.getWrapDTO().getJavaWrapDTO().getJavaClass().split(SOAConstant.ADAPTIVE_PATH_SEPERATOR);
            for(int i=javaNames.length-2;i>=0;i--) {
               Object tempJavaObject = SOAReflectionUtil.createJavaObject(javaClasses[i]);
               setPropertyValue(tempJavaObject,javaNames[i+1],childJavaObject);
               childJavaObject = tempJavaObject;
           }

            return childJavaObject;
        }

    }

    private Object handleCollection(DeserializeBindingRuleDTO bindingRuleDTO) {
        JavaWrapDTO javaWrapDTO = bindingRuleDTO.getRuleDTO().getWrapDTO().getJavaWrapDTO();
        String[] javaClasses = javaWrapDTO.getJavaClass().split(SOAConstant.ADAPTIVE_PATH_SEPERATOR);
        String javaType = javaClasses[javaClasses.length-1];
        ArrayList tempJavaObjectStore = new ArrayList();
        Object propertyValue = bindingRuleDTO.getPropertyValue();
            Class<?> javaObjectClass;
        if(SOAReflectionUtil.isCollectionType(propertyValue)) {
            propertyValue = ((Collection)propertyValue).toArray();
        }
            for (int i=0 ; i<Array.getLength(propertyValue); i++) {
                Object currentJaxbObject = Array.get(propertyValue, i);
                String javaName = bindingRuleDTO.getRuleDTO().getJavaClass();
                XMLMapping typeMapping = new XMLMapping(bindingRuleDTO.getRuleDTO().getXmlType(),bindingRuleDTO.getRuleDTO().getXmlName(),javaName);
                tempJavaObjectStore.add(adaptiveContext.deserialize(currentJaxbObject,typeMapping));
            }
        try {
            javaObjectClass= Class.forName(javaType);
        } catch (ClassNotFoundException e) {
            throw new SoajRuntimeException("unable to instantiate class for javaType = " + javaType);
        }
        if(!javaObjectClass.isArray()) {
            return tempJavaObjectStore;
        }else {
            Object[] javaObjectStoreArray;
            try {
                javaObjectStoreArray = (Object[]) Array.newInstance(Class.forName(javaType).getComponentType(), tempJavaObjectStore.size());
                for (int i = 0; i < tempJavaObjectStore.size(); i++) {
                    Object o1 =  tempJavaObjectStore.get(i);
                    Array.set(javaObjectStoreArray,i,o1);
                }
                return javaObjectStoreArray;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("",e);
            }
//            return tempJavaObjectStore.toArray(o);
        }
    }
}
