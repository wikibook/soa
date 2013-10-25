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
import com.javector.adaptive.framework.dto.DeserializeBindingRuleDTO;
import com.javector.adaptive.framework.model.RuleDTO;
import com.javector.adaptive.framework.model.WrapDTO;
import com.javector.adaptive.framework.util.SOAReflectionUtil;
import com.javector.adaptive.framework.constants.SOAConstant;
import com.javector.soaj.SoajException;
import com.javector.soaj.SoajRuntimeException;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: rohit.agarwal
 * Date: Apr 6, 2006
 * Time: 7:18:26 PM
 * This is the defalut handler defined in handlerMapping.xml file.
 */
public class JAXBSimpleTypeDeserializer extends BaseAdaptiveDeserializer {


    /**
     * Creats a java child Object into which will hold the value for jaxbObject
     * @param bindingRuleDTO
     * @return
     * @throws SoajException
     */
    public Object createJavaChildObject(DeserializeBindingRuleDTO bindingRuleDTO) {
        if(bindingRuleDTO.getRuleDTO().getWrapDTO()==null) {
            return bindingRuleDTO.getTargetJavaObject();
        }
        RuleDTO ruleDTO = bindingRuleDTO.getRuleDTO();
        WrapDTO wrapDTO = ruleDTO.getWrapDTO();
        Object propertyValue = bindingRuleDTO.getPropertyValue();
            if(SOAReflectionUtil.isCollectionType(propertyValue))
                propertyValue = ((Collection)propertyValue).toArray();
            bindingRuleDTO.setPropertyValue(propertyValue);
            String[] javaNames  = wrapDTO.getJavaWrapDTO().getJavaName().split(SOAConstant.ADAPTIVE_PATH_SEPERATOR);
            String[] javaClasses = wrapDTO.getJavaWrapDTO().getJavaClass().split(SOAConstant.ADAPTIVE_PATH_SEPERATOR);
            Object childJavaObject = null;
            try {
                Class<?> aClass = Class.forName(javaClasses[javaClasses.length - 1]);
                childJavaObject = aClass.newInstance();
                setPropertyValue(childJavaObject,ruleDTO.getJavaName(),propertyValue);
            } catch (Exception e) {
                throw new SoajRuntimeException("unable to initialize class = " + javaClasses[javaClasses.length-1]);
            }
            for(int i=javaNames.length-2; i==0;i++) {
                Object tempJavaObject = SOAReflectionUtil.createJavaObject(javaClasses[i]);
                setPropertyValue(tempJavaObject,javaNames[i],childJavaObject);
                childJavaObject = tempJavaObject;
            }
            return childJavaObject;
        }
    }
