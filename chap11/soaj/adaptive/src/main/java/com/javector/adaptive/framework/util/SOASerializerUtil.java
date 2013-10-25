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
import com.javector.adaptive.framework.model.StrategyDTO;
import com.javector.adaptive.framework.interfaces.BaseAdaptiveSerializer;
import com.javector.adaptive.framework.interfaces.BaseAdaptiveDeserializer;
import com.javector.adaptive.framework.constants.SOAConstant;
import com.javector.soaj.SoajException;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Auther: Rohit Agarwal
 * Date: May 12, 2006
 * Time: 9:18:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class SOASerializerUtil {

    public static  BaseAdaptiveSerializer getSerializerInstance(StrategyDTO serializerMapping, String serializerClassName, RuleDTO ruleDTO) {
          Map<String, String> propertyMappings = ruleDTO.getPropertyMappings();
          String serializerClass = propertyMappings.get(SOAConstant.serializerHandleClass);
          if(serializerClass!= null && SOAUtil.validateHandler(serializerClass,BaseAdaptiveSerializer.class)) {
              return (BaseAdaptiveSerializer)SOAReflectionUtil.createJavaObject(serializerClass);
          }
          serializerClass = serializerMapping.getPropertyMappings().get(SOAConstant.serializerHandleClass);
          if(serializerClass!=null&& SOAUtil.validateHandler(serializerClass,BaseAdaptiveSerializer.class)) {
              return (BaseAdaptiveSerializer)SOAReflectionUtil.createJavaObject(serializerClass);
          }else {
              return (BaseAdaptiveSerializer)SOAReflectionUtil.createJavaObject(serializerClassName);
          }
      }


    /**
     * This method will create instance for derserializer class
     * the hirerchy in which the deserializer will be loaded is like
     * 1) if any deserializer handler is defined for a rule.
     * 2) if any deserializer handler is defined for startegy
     * 3) default deseriliazer handler defined for jaxb.
     * @param serializerMapping
     * @param deserializerClassName
     * @param ruleDTO
     * @return
     * @throws SoajException
     */
    public static  BaseAdaptiveDeserializer getDeserilaizerInstance(StrategyDTO serializerMapping, String deserializerClassName, RuleDTO ruleDTO) {
             Map<String, String> propertyMappings = ruleDTO.getPropertyMappings();
             String deserializerClass = propertyMappings.get(SOAConstant.deserializerHandlerClass);
             if(deserializerClass!= null && SOAUtil.validateHandler(deserializerClass,BaseAdaptiveDeserializer.class)) {
                 return (BaseAdaptiveDeserializer)SOAReflectionUtil.createJavaObject(deserializerClass);
             }
             deserializerClass = serializerMapping.getPropertyMappings().get(SOAConstant.deserializerHandlerClass);
             if(deserializerClass!=null&& SOAUtil.validateHandler(deserializerClass,BaseAdaptiveSerializer.class)) {
                 return (BaseAdaptiveDeserializer)SOAReflectionUtil.createJavaObject(deserializerClass);
             }else {
                 return (BaseAdaptiveDeserializer)SOAReflectionUtil.createJavaObject(deserializerClassName);
             }
         }


}
