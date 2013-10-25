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

import com.javector.adaptive.framework.dto.SerializeBindingRuleDTO;
import com.javector.adaptive.framework.model.*;
import com.javector.adaptive.framework.util.*;
import com.javector.adaptive.framework.constants.SOAConstant;
import com.javector.soaj.SoajException;
import com.javector.soaj.SoajRuntimeException;
import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilationFailedException;

import javax.xml.namespace.QName;

/**
 * Created by IntelliJ IDEA.
 * User: Kishore G
 * Date: Jan 15, 2006
 * Time: 1:07:27 PM
 * To change this template use File | Settings | File Templates.
 */
public  class BaseAdaptiveSerializer {

    private static SoajLogger logger = LoggingFactory.getLogger(BaseAdaptiveSerializer.class.getName());

    protected StrategyDTO serializerMapping;
    protected BaseSOAContext adaptiveContext;
    protected BaseReflectionUtil reflectionUtil;

    public  BaseAdaptiveSerializer() {
        serializerMapping = null;
        adaptiveContext = null;
        reflectionUtil = null;
    }

    public void setReflectionUtil(BaseReflectionUtil util) {
        this.reflectionUtil = util;
    }


    /**
     * This method must always be overridden in simple, complex or custom type serializer
     * Will be called by this class getJava method for creating appropriate java Object for
     * a perticular rule.
     * @param serializeBindingRuleDTO
     * @return
     * @throws SoajException
     */
    public Object createXMLChildObject(SerializeBindingRuleDTO serializeBindingRuleDTO) {
        throw new SoajRuntimeException("no implementation for serializer selected");
    }

    /**
     * Creating initialization parameters
     * @param context
     * @param strategyMapping
     */

    protected void init(BaseSOAContext context, StrategyDTO strategyMapping) {
        this.adaptiveContext = context;
        this.serializerMapping = strategyMapping;
        this.reflectionUtil = getReflectionUtilInstance(context);
    }


    /**
     * Loading an instance of reflection util based on defination provided in
     * handlerMapping.xml file
     * @param soaContext
     * @return
     */
    private BaseReflectionUtil getReflectionUtilInstance(BaseSOAContext soaContext) {
        String reflectionUtilClassName  = soaContext.getConfigurationDetailDTO().getReflectionUtilClassName();
        try {
            reflectionUtil = (BaseReflectionUtil)Class.forName(reflectionUtilClassName).newInstance();
        }catch(Exception ex) {
            throw new SoajRuntimeException("Unable to instantiate class = "+reflectionUtilClassName,ex);
        }
        return reflectionUtil;
    }

    /**
     * Top level method which will call all complex, simple, base type serializers during
     * serialization proces
     * @param sourceObject
     * @param baseSOAContext
     * @return
     * @throws SoajException
     */

    public   Object getXml(Object sourceObject, BaseSOAContext baseSOAContext) {
        logger.info("enetering get xml for sourceObject = " + sourceObject);
        QName xmlType = serializerMapping.getXmlType();
        ClassLoader cl = adaptiveContext.getClassLoader();
        Object targetJaxbObject = reflectionUtil.createXMLObject(xmlType, cl);
        Object childJaxbObject;
        RuleDTO[] ruleDTOs = serializerMapping.getRuleDTOs();
        for (RuleDTO ruleDTO : ruleDTOs) {
            if(ruleDTO.isSerialization()){
                Object propertyValue;
                ScriptDTO scriptDTO = ruleDTO.getScriptDTO();
                SerializeBindingRuleDTO serializeBindingRuleDTO = new SerializeBindingRuleDTO();
                serializeBindingRuleDTO.setRuleDTO(ruleDTO);
                serializeBindingRuleDTO.setCl(cl);
                serializeBindingRuleDTO.setTargetJaxbObject(targetJaxbObject);
                serializeBindingRuleDTO.setSourceJavaObject(sourceObject);
                if(ruleDTO.getWrapDTO()!= null) {
                    serializeBindingRuleDTO.setSourceJavaObject(sourceObject);
                    handleWrap(serializeBindingRuleDTO);
                } else {
                    BaseAdaptiveSerializer serializer = getSerializer(ruleDTO.getXmlType(),ruleDTO);
                    String convertorClass = ruleDTO.getPropertyMappings().get(SOAConstant.simpleTypeConvertor);
                    if(scriptDTO!= null) {
                        propertyValue = handleScript(ruleDTO,sourceObject,ruleDTO.getScriptDTO());
                    } else if (SOAUtil.doesConvertorExits(convertorClass)){
                        String getterMethod = ruleDTO.getPropertyMappings().get(SOAConstant.JAVA_GETTER_METHOD_OVERRIDE);
                        propertyValue = SOAHelper.getConvertorValueForXML(convertorClass, ruleDTO, sourceObject, ruleDTO.getXmlType());
                    } else {
                        propertyValue = serializer.getPropertyValue(serializeBindingRuleDTO);
                    }
                    if(propertyValue ==null) {
                        continue;
                    }
                    serializeBindingRuleDTO.setPropertyValue(propertyValue);
                    childJaxbObject = serializer.createXMLChildObject(serializeBindingRuleDTO);
                    if(childJaxbObject!=targetJaxbObject) {
                        reflectionUtil.setProperty(targetJaxbObject,childJaxbObject,ruleDTO.getXmlName());
                    } else {
                        reflectionUtil.setProperty(childJaxbObject,propertyValue,ruleDTO.getXmlName());
                    }
                }
            }
        }

       String xmlObject = reflectionUtil.convertXMLObjectToString(targetJaxbObject, baseSOAContext.getClassLoader(),xmlType);
        System.out.println("object as String = " + xmlObject);
        return targetJaxbObject;
    }



    /**
     * This method can be overridden to return user defined property values for java Object
     * @param serializeBindingRuleDTO
     * @return
     * @throws SoajException
     */
    public Object getPropertyValue(SerializeBindingRuleDTO serializeBindingRuleDTO) {
        RuleDTO ruleDTO = serializeBindingRuleDTO.getRuleDTO();
        String javaPropertyNameOverride = SOAConstant.JAVA_PROPERTY_NAME_OVERRIDE;
        if(ruleDTO.getJavaName().equals(javaPropertyNameOverride)) {
            return serializeBindingRuleDTO.getSourceJavaObject();
        }
        if(ruleDTO.getWrapDTO()==null) {
        Object sourceJavaObject = serializeBindingRuleDTO.getSourceJavaObject();
        String javaField = ruleDTO.getJavaName();
            String getterMethod = ruleDTO.getPropertyMappings().get(SOAConstant.JAVA_GETTER_METHOD_OVERRIDE);
            if(getterMethod!=null ) {
                return SOAReflectionUtil.getPropertyValue(sourceJavaObject,getterMethod);
            }
            return SOAReflectionUtil.getPropertyValueDefault(sourceJavaObject,javaField);
        }else {
            Object propertyValue = null;
            JavaWrapDTO javaWrapDTO = ruleDTO.getWrapDTO().getJavaWrapDTO();
            String[] javaNames = javaWrapDTO.getJavaName().split(SOAConstant.ADAPTIVE_PATH_SEPERATOR);
            Object tempSourceJavaOjbect = serializeBindingRuleDTO.getSourceJavaObject();
            for(String javaName : javaNames) {
                propertyValue = SOAReflectionUtil.getPropertyValueDefault(tempSourceJavaOjbect,javaName);
                if(propertyValue==null) {
                    return null;
                }
                tempSourceJavaOjbect = propertyValue;
            }
            return propertyValue;
        }
    }


    /**
     * This method will handle any wrap associated to any rule.
     * The callback will be made on simpleType, Complextype, customType serializer
     * for getting property value for wrapped jaxbObject
     * @param serializeBindingRuleDTO
     * @throws SoajException
     */

    public void handleWrap(SerializeBindingRuleDTO serializeBindingRuleDTO) {
        BaseAdaptiveSerializer serializer = getSerializer(serializeBindingRuleDTO.getRuleDTO().getXmlType(),serializeBindingRuleDTO.getRuleDTO());
        Object propertyValue = serializer.getPropertyValue(serializeBindingRuleDTO);
        if(propertyValue==null) {
            return;
        }
        serializeBindingRuleDTO.setPropertyValue(propertyValue);
        Object childJaxbObject;
        childJaxbObject = serializer.createXMLChildObject(serializeBindingRuleDTO);
        Object sourceJavaObject = serializeBindingRuleDTO.getSourceJavaObject();
        XmlWrapDTO xmlWrapDTO = serializeBindingRuleDTO.getRuleDTO().getWrapDTO().getXmlWrapDTO();
        String[] xmlNames = xmlWrapDTO.getXmlName().split(SOAConstant.ADAPTIVE_PATH_SEPERATOR);
        String nameSpaceURI = reflectionUtil.getNameSpaceUri(serializeBindingRuleDTO.getTargetJaxbObject());
        QName qName = new QName(nameSpaceURI,xmlNames[0]);
        reflectionUtil.setProperty(serializeBindingRuleDTO.getTargetJaxbObject(),childJaxbObject,qName);
    }



    private BaseAdaptiveSerializer getSerializer(QName xmlName,RuleDTO ruleDTO) {
        BaseAdaptiveSerializer serializer;
        if(reflectionUtil.isSimpleType(ruleDTO.getXmlType())) {
            serializer =  SOASerializerUtil.getSerializerInstance(serializerMapping,adaptiveContext.getConfigurationDetailDTO().getSimpleTypeSerializer(),ruleDTO);
        }else {
            serializer = SOASerializerUtil.getSerializerInstance(serializerMapping,adaptiveContext.getConfigurationDetailDTO().getComplexTypeSerializer(),ruleDTO);
        }
        serializer.init(adaptiveContext,serializerMapping);
        return serializer;

    }

    /**
     * Top level method for serlaizing java Objects as xml String
     * @param sourceObject
     * @param baseSOAContext
     * @param serializerMapping
     * @return
     * @throws SoajException
     */
    protected  String getXMLAsStringTemplate(Object sourceObject, BaseSOAContext baseSOAContext, StrategyDTO serializerMapping) throws SoajException {
        init(baseSOAContext,serializerMapping);
        return null; // todo: to implement the method getXmlAsString(sourceObject,baseSOAContext);
    }

    /**
     * Top level method for serializing java Objects as xml Objects
     * @param sourceObject
     * @param baseSOAContext
     * @param serializerMapping
     * @return
     * @throws SoajException
     */
    protected  Object getXMLTemplate(Object sourceObject, BaseSOAContext baseSOAContext, StrategyDTO serializerMapping) {
        init(baseSOAContext,serializerMapping);
        return getXml(sourceObject,baseSOAContext);
    }


    /**
     * Will be called for getting property value for xmlObject from javaObject
     * by appling groovy defined in scriptDTO .
     * Can be overrridden in simple, complex or custom type serializer for using
     * difeerent technnique apart from groovy.
     * @param ruleDTO
     * @param sourceObject
     * @param scriptDTO
     * @return
     * @throws SoajException
     */

    protected Object handleScript(RuleDTO ruleDTO, Object sourceObject, ScriptDTO scriptDTO) {
        Object currentSourceObject = null;
        Binding binding = new Binding();
        Object tempSourceObject;
        if (ruleDTO.getJavaName() == null || ruleDTO.getJavaName().equals(".")) {
            tempSourceObject = sourceObject;
        } else {
            tempSourceObject = sourceObject;
            String[] strings = ruleDTO.getJavaName().split("\\.");
            for (String string : strings) {
                tempSourceObject = SOAReflectionUtil.getPropertyValueDefault(tempSourceObject, string);
            }
        }
        try {
            if (tempSourceObject != null) {
                binding.setVariable("source", tempSourceObject);
                GroovyShell shell = new GroovyShell(binding);
                currentSourceObject = shell.evaluate(scriptDTO.getScriptData());
            }
        } catch (CompilationFailedException e) {
            throw new SoajRuntimeException("Exception while compiling groovy script", e);
        }
        return currentSourceObject;
    }


}
