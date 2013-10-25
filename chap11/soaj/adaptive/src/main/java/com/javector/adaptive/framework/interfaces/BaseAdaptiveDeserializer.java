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
package com.javector.adaptive.framework.interfaces;

import com.javector.adaptive.framework.model.StrategyDTO;
import com.javector.adaptive.framework.model.RuleDTO;
import com.javector.adaptive.framework.model.WrapDTO;
import com.javector.adaptive.framework.model.XmlWrapDTO;
import com.javector.adaptive.framework.util.*;
import com.javector.adaptive.framework.dto.DeserializeBindingRuleDTO;
import com.javector.adaptive.framework.constants.SOAConstant;
import com.javector.soaj.SoajRuntimeException;
import com.javector.soaj.logging.SoajLogger;
import com.javector.soaj.logging.LoggingFactory;

import javax.xml.namespace.QName;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Kishore G,Rohit Agarwal
 * Date: Jan 15, 2006
 * Time: 1:07:49 PM
 * To change this template use File | Settings | File Templates.
 */
public  class BaseAdaptiveDeserializer {

    private static SoajLogger logger = LoggingFactory.getLogger(BaseAdaptiveDeserializer.class.getName());
    public  StrategyDTO serializerMapping;
    public  BaseSOAContext adaptiveContext;
    public  BaseReflectionUtil reflectionUtil;
    public  BaseXPathEvaluator xPathEvaluator;

    /**
     * This method takes in a dto containing mapping for a jaxbtype to javaObject
     * @param serializerMapping
     */


    private void setSerializerMapping(StrategyDTO serializerMapping) {
        this.serializerMapping = serializerMapping;
    }

    /**
     * This method will create a context for deserializer
     * Contact will have all information regarding the classloader in jaxb
     * and schema loader in case of xml bean.
     * @param adaptiveContext
     */

    private void setAdaptiveContext(BaseSOAContext adaptiveContext) {
        this.adaptiveContext = adaptiveContext;
    }

    /**
     * This is the starting method from where  the actual deserialization will start
     * This method is the top level method for all complex type, Simple type and user defined
     * handlers.
     * @param sourceXmlObject
     * @param baseSOAContext
     * @return
     * @throws SoajException
     */

    public  Object getJava(Object sourceXmlObject, BaseSOAContext baseSOAContext) {
        logger.info("deserialization started   for sourceXMlObject =" + sourceXmlObject.getClass().getName());
        Object targetJavaObject = SOAReflectionUtil.createJavaObject(serializerMapping.getJavaClass());
        logger.info("created target java Object as = " + serializerMapping.getJavaClass());
        RuleDTO[] ruleDTOs = serializerMapping.getRuleDTOs();
        for(RuleDTO ruleDTO: ruleDTOs) {
            if(ruleDTO.isDeserialization()) {
                logger.info("deserialization for rule = " + ruleDTO);
                Object propertyValue;
                Object childJavaObject;
                DeserializeBindingRuleDTO bindingRuleDTO = new DeserializeBindingRuleDTO();
                bindingRuleDTO.setCl(adaptiveContext.getClassLoader());
                bindingRuleDTO.setRuleDTO(ruleDTO);
                bindingRuleDTO.setTargetJavaObject(targetJavaObject);
                bindingRuleDTO.setSourceJaxbObject(sourceXmlObject);
                if(ruleDTO.getWrapDTO()!=null) {
                    logger.info("java wrap class/name for deserialization = " + 
                        ruleDTO.getWrapDTO().getJavaWrapDTO().getJavaClass() + "/" +
                        ruleDTO.getWrapDTO().getJavaWrapDTO().getJavaName());
                    logger.info("xml wrap type/name for deserialization = " + 
                        ruleDTO.getWrapDTO().getXmlWrapDTO().getXmlType() + "/" +
                        ruleDTO.getWrapDTO().getXmlWrapDTO().getXmlName());
                    handleWrap(bindingRuleDTO);
                }else {
                    BaseAdaptiveDeserializer deserializer = getDeserializer(ruleDTO);
                    String convertorClass = ruleDTO.getPropertyMappings().get(SOAConstant.simpleTypeConvertor);
                    if(ruleDTO.getScriptDTO()!=null) {
                        logger.info("script for deserialization = " + ruleDTO.getScriptDTO());
                        propertyValue = deserializer.handleScript(bindingRuleDTO);
                        logger.info("property value to be set after applying script = " + propertyValue);
                    } else if (SOAUtil.doesConvertorExits(convertorClass)){
                        Object xmlValue = reflectionUtil.getPropertyValue(sourceXmlObject,bindingRuleDTO.getRuleDTO().getXmlName());
                        propertyValue = SOAHelper.getConvertorValueForJava(convertorClass, ruleDTO.getJavaName(), xmlValue, serializerMapping.getJavaClass());
                    } else {
                        propertyValue = deserializer.getPropertyValue(bindingRuleDTO);
                        logger.info("property value without script = " + propertyValue);
                    }
                    if(propertyValue == null)
                        continue;
                    bindingRuleDTO.setPropertyValue(propertyValue);
                    childJavaObject = deserializer.createJavaChildObject(bindingRuleDTO);
                    if(childJavaObject!= targetJavaObject) {
                        logger.info("setting value for complex child Object = " + childJavaObject + " in target java Object =" + targetJavaObject);
                        targetJavaObject = deserializer.setPropertyValue(targetJavaObject,ruleDTO,childJavaObject);
                    } else {
                        logger.info("setting value for simple child Object  = " + propertyValue + "in target java Object  = " + childJavaObject);
                        childJavaObject = deserializer.setPropertyValue(childJavaObject,ruleDTO,propertyValue);
                    }
                }
            }
        }
        return targetJavaObject;

    }

    public Object setPropertyValue(Object targetJavaObject, RuleDTO ruleDTO , Object childJavaObject) {
        String preferIndex = ruleDTO.getPropertyMappings().get(SOAConstant.PERFER_INDEX);
        if(preferIndex!=null) {
            if(SOAReflectionUtil.isCollectionType(childJavaObject) ) {
                Object[] objects = ((Collection) childJavaObject).toArray();
                childJavaObject = objects[new Integer(preferIndex)];
            }else  {
                Object[] objects = ((Collection) childJavaObject).toArray();
                childJavaObject = objects[new Integer(preferIndex)];
            }
        }
        String setterMethodName = ruleDTO.getPropertyMappings().get(SOAConstant.JAVA_SETTER_METHOD_OVERRIDE);
        if(setterMethodName!=null) {
            SOAReflectionUtil.setPropertyValue(targetJavaObject,setterMethodName,childJavaObject);
        }
        String successor = SOAUtil.convertToTitleCase(ruleDTO.getJavaName());
        setPropertyValue(targetJavaObject,"set" +successor,childJavaObject);
        return targetJavaObject;
    }

     public  void setPropertyValue(Object targetJavaObject, String  name, Object childJavaObject) {
            SOAReflectionUtil.setPropertyValue(targetJavaObject,name,childJavaObject);

    }

    /**
     * This method can be overwritten in user defined handler classes
     * to plug in the logic for handling the scripts in a cutom manner
     * By default the scripts will be handled using xPath APi where the
     * source Object will be String and written type will be XString after
     * appying Xpath
     * @param bindingRuleDTO
     * @return
     * @throws SoajException
     */

    public  Object handleScript(DeserializeBindingRuleDTO bindingRuleDTO) {
        QName xmlName = bindingRuleDTO.getRuleDTO().getXmlName();
        Object propertyValue;
        if(xmlName ==null) {
            propertyValue = bindingRuleDTO.getSourceJaxbObject();
        } else {
            propertyValue = reflectionUtil.getPropertyValue(bindingRuleDTO.getSourceJaxbObject(),xmlName);
        }
        bindingRuleDTO.setReflectionUtil(reflectionUtil);
        return  xPathEvaluator.applyXPath(bindingRuleDTO,propertyValue);
    }

    /**
     * This method will handle any wrap associated to any rule.
     * The callback will be made on simpleType, Complextype, customType deserializer
     * for getting property value for wrapped jaxbObject
     * @param bindingRuleDTO
     * @throws SoajException
     */

    // todo: setting of property value needs to be done in a proper manner.
    private void handleWrap(DeserializeBindingRuleDTO bindingRuleDTO) {
        BaseAdaptiveDeserializer deserializer = getDeserializer(bindingRuleDTO.getRuleDTO());
        String[] javaNames = bindingRuleDTO.getRuleDTO().getWrapDTO().getJavaWrapDTO().getJavaName().split(SOAConstant.ADAPTIVE_PATH_SEPERATOR);
        String propertyName = javaNames[0];
        Object propertyValue = deserializer.getPropertyValue(bindingRuleDTO);
        if(propertyValue == null) {
            return;
        }
        bindingRuleDTO.setPropertyValue(propertyValue);
        Object childJavaObject = deserializer.createJavaChildObject(bindingRuleDTO);
        SOAReflectionUtil.setPropertyValueDefault(bindingRuleDTO.getTargetJavaObject(),propertyName,childJavaObject);
    }

    /**
     * This method must always be overridden in simple, complex or custom type deserializer
     * Will be called by this class getJava method for creating appropriate java Object for
     * a perticular rule.
     * @param bindingRuleDTO
     * @return
     * @throws SoajException
     */


    public Object createJavaChildObject(DeserializeBindingRuleDTO bindingRuleDTO) {
        throw new SoajRuntimeException("no implementation for serializer selected");
    }


    public Object getJavaFromXML(String xmlData, BaseSOAContext baseSOAContext) {
        Object xmlObjectFromXMLSource = reflectionUtil.createXMLObjectFromXMLSource(xmlData, serializerMapping.getXmlType(), baseSOAContext.getClassLoader());
        return getJava(xmlObjectFromXMLSource,baseSOAContext);
    }

    /**
     * This method will return propertyValue for a perticalar jaxbObject for a perticular
     * xmlName.This method can be overritten in simple type and complex type deserilaizer
     * for abtaining a perticular property value. The default property value will be obtained
     * using this method. A user handler must override this method in case he wants to give
     * a custom implementation for property value.
     * @param bindingRuleDTO
     * @return
     * @throws SoajException
     */
    public Object getPropertyValue(DeserializeBindingRuleDTO bindingRuleDTO) {
        if(bindingRuleDTO.getRuleDTO().getWrapDTO() ==null) {
            Object sourceJaxbObject = bindingRuleDTO.getSourceJaxbObject();
            return reflectionUtil.getPropertyValue(sourceJaxbObject,bindingRuleDTO.getRuleDTO().getXmlName());
        }else {
            WrapDTO wrapDTO = bindingRuleDTO.getRuleDTO().getWrapDTO();
            String[] xmlNames = wrapDTO.getXmlWrapDTO().getXmlName().split(SOAConstant.ADAPTIVE_PATH_SEPERATOR);
            Object sourceJaxbObject = bindingRuleDTO.getSourceJaxbObject();
            Object propertyValue =null;
            Object currentSourceJaxbObject = sourceJaxbObject;
            for (String xmlName : xmlNames) {
                String nameSpaceURI = reflectionUtil.getNameSpaceUri(bindingRuleDTO.getSourceJaxbObject());
                QName currentXMLName = new QName(nameSpaceURI, xmlName);
                propertyValue = reflectionUtil.getPropertyValue(currentSourceJaxbObject, currentXMLName);
                currentSourceJaxbObject = propertyValue;
            }
            if(propertyValue ==null) {
                return null;
            }
            XmlWrapDTO xmlWrapDTO = bindingRuleDTO.getRuleDTO().getWrapDTO().getXmlWrapDTO();
            if(!bindingRuleDTO.getRuleDTO().getXmlName().getLocalPart().equals(xmlWrapDTO.getXmlName()))
           propertyValue = reflectionUtil.getPropertyValue(propertyValue,bindingRuleDTO.getRuleDTO().getXmlName());
            return propertyValue;
        }
    }


    /**
     * This method will be called by top level get java for choosing appropriate
     * instance of derserilaizer
     * The startegy for choosing deserilaizer will be
     * First deserializer handler defined at rule level in adadptive.xml will be tried to load if defined
     * then deserializer at strategy level will be tried loading
     * if both are not present default handler for complex or simple type will be loaded
     * base on the rule mapping
     * @param ruleDTO
     * @return
     * @throws SoajException
     */

    private BaseAdaptiveDeserializer getDeserializer(RuleDTO ruleDTO) {
        BaseAdaptiveDeserializer deserializer;
        if(reflectionUtil.isSimpleJavaType(ruleDTO.getJavaClass())||ruleDTO.getJavaClass()==null) {
            deserializer = SOASerializerUtil.getDeserilaizerInstance(serializerMapping,adaptiveContext.getConfigurationDetailDTO().getSimpleTypeDeserializer(),ruleDTO);
        }else {
            deserializer = SOASerializerUtil.getDeserilaizerInstance(serializerMapping,adaptiveContext.getConfigurationDetailDTO().getComplexTypeDeserializer(),ruleDTO);
        }
        deserializer.init(adaptiveContext,serializerMapping);
        return deserializer;

    }



    public BaseAdaptiveDeserializer() {
        this.adaptiveContext = null;
        this.serializerMapping = null;
        this.reflectionUtil = null;
        this.xPathEvaluator = null;
    }

    /**
     * Will be invoked by context before starting deserialization
     * Will do the necessary initialization for any deserliazer
     * @param sourceXmlObject
     * @param baseSOAContext
     * @param serializerMapping
     * @return
     * @throws SoajException
     */

    public Object getJavaTemplate(Object sourceXmlObject, BaseSOAContext baseSOAContext, StrategyDTO serializerMapping) {
        init(baseSOAContext,serializerMapping);
        return  getJava(sourceXmlObject,baseSOAContext);
    }

    public Object getJavaFromXMLTemplate(String xmlData, BaseSOAContext baseSOAContext,StrategyDTO serializerMapping) {
        init(baseSOAContext,serializerMapping);
        return getJavaFromXML(xmlData,baseSOAContext);
    }

    /**
     * Inititialization of instance when Object instance for deserializer gets loaded
     * @param context
     * @param serializerMapping
     */
    private  void init(BaseSOAContext context, StrategyDTO serializerMapping ){
        setAdaptiveContext(context);
        setSerializerMapping(serializerMapping);
        setReflectionUtil(context);
        setXPathEveluator(context);
    }

    /**
     * Setting xpath evalutor instance to be used in handle script the instance mapping
     * will be obtained from handlerMappping.xml file
     * @param context
     */

    private void setXPathEveluator(BaseSOAContext context) {
        this.xPathEvaluator = getXPathEvaluatorInstance(context);
    }

    /**
     * Setting reflection util instance to be used in for obtaning property values the instance mapping
     * will be obtained from handlerMappping.xml file
     * @param context
     */

    private void setReflectionUtil(BaseSOAContext context) {
        this.reflectionUtil = getReflectionUtilInstance(context);
    }

    private BaseXPathEvaluator getXPathEvaluatorInstance(BaseSOAContext soaContext) {
        String xpathEvalutorName = soaContext.getConfigurationDetailDTO().getXpathImplClassName();
        try {
            xPathEvaluator = (BaseXPathEvaluator)Class.forName(xpathEvalutorName).newInstance();
        }catch(Exception ex) {
            throw new SoajRuntimeException("unable to create instance of class = " + xpathEvalutorName,ex);
        }
        return xPathEvaluator;
    }

    private BaseReflectionUtil getReflectionUtilInstance(BaseSOAContext soaContext) {
        String reflectionUtilClassName  = soaContext.getConfigurationDetailDTO().getReflectionUtilClassName();
        try {
            reflectionUtil = (BaseReflectionUtil)Class.forName(reflectionUtilClassName).newInstance();
        }catch(Exception ex) {
            throw new SoajRuntimeException("Unable to instantiate class = "+reflectionUtilClassName,ex);
        }
        return reflectionUtil;
    }

}

