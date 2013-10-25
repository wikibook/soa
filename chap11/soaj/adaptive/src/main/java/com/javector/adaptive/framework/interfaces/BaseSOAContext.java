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

import com.javector.adaptive.framework.AdaptiveMapper;
import com.javector.adaptive.framework.model.AdaptiveMapDTO;
import com.javector.adaptive.framework.model.ConfigurationDetailDTO;
import com.javector.adaptive.framework.model.RuleDTO;
import com.javector.adaptive.framework.model.StrategyDTO;
import com.javector.adaptive.framework.util.BaseReflectionUtil;
import com.javector.adaptive.framework.util.SOAContextUtil;
import com.javector.adaptive.framework.util.SOAReflectionUtil;
import com.javector.adaptive.framework.util.SOAUtil;
import com.javector.soaj.SoajException;
import com.javector.soaj.SoajRuntimeException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Kishore G
 * Date: Jan 7, 2006
 * Time: 1:22:03 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseSOAContext implements AdaptiveContext {


    protected AdaptiveMapDTO adaptiveMapDTO;
    static protected String tempWorkDir;
    protected URL[] schemaURLS;
    protected ConfigurationDetailDTO configurationDetailDTO;
    protected BaseAdaptiveSerializer baseAdaptiveSerializer;
    protected BaseAdaptiveDeserializer baseAdaptiveDeserializer;
    protected URLClassLoader classLoader;

    public abstract  URLClassLoader getClassLoader();

    public ConfigurationDetailDTO getConfigurationDetailDTO() {
        return configurationDetailDTO;
    }

    public void setConfigurationDetailDTO(ConfigurationDetailDTO configurationDetailDTO) {
        this.configurationDetailDTO = configurationDetailDTO;
    }

    static {

//        Properties systemP = System.getProperties();
//        Enumeration e = systemP.keys();
//        while (e.hasMoreElements()) {
//            Object name = e.nextElement();
//            String value = systemP.get(name).toString();
//        }

//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        URL  url = classLoader.getResource(".");
//        tempWorkDir = url.getPath()+ "/soabook-xmlbeans";
//         (new File(tempWorkDir)).mkdir();

// TODO very bad!! - create a better way to save temporary work.      
        tempWorkDir = System.getProperty("user.home") +
                "/soabook-xmlbeans";
        (new File(tempWorkDir)).mkdir();
    }

    public AdaptiveMapDTO getAdaptiveMapDTO() {
        return adaptiveMapDTO;
    }

    public void setAdaptiveMapDTO(AdaptiveMapDTO adaptiveMapDTO) {
        this.adaptiveMapDTO = adaptiveMapDTO;
    }

    public final List<StrategyDTO> getStrategyForXmlName(QName xmlName) {
        return SOAContextUtil.getSerializersForXmlName(adaptiveMapDTO, xmlName);
    }

    public final List<StrategyDTO> getStrategyForXmlType(QName xmlType) {
        return SOAContextUtil.getSerializersForXmlType(adaptiveMapDTO, xmlType);
    }

    protected BaseSOAContext() {
        adaptiveMapDTO = null;
        configurationDetailDTO = null;
    }

    public Object deserialize(Source source, XMLMapping mapping) {       
        String reflectionUtilClassName = configurationDetailDTO.getReflectionUtilClassName();
        BaseReflectionUtil reflectionUtil= (BaseReflectionUtil)SOAReflectionUtil.createJavaObject(reflectionUtilClassName);
        Node xmlNode = SOAUtil.getXMLNodeFromSource(source);
        Object xmlObjectFromXMLNode = reflectionUtil.createXMLObjectFromXMLNode(xmlNode, mapping.getTypeOrElementQName(), this.getClassLoader());
        if(xmlObjectFromXMLNode instanceof JAXBElement ) {
            xmlObjectFromXMLNode = ((JAXBElement)xmlObjectFromXMLNode).getValue();
        }
        return deserialize(xmlObjectFromXMLNode,mapping);
    }

    /**
     * This shud take the java object as the input and then return an
     * XmlObject in case of xmlBean Implementation
     * javax.xml.bind.Element in case of JAXB implementation
     */
    public final Object serialize(Object sourceObject, XMLMapping typeMapping) {
        if (sourceObject == null) {
            return null;
        }
        List<StrategyDTO> serializerList;
        serializerList = SOAContextUtil.getSerializersForMapping(adaptiveMapDTO, typeMapping);
        if(serializerList.size() == 0) {
            throw new SoajRuntimeException("No matching serialization strategy found for typemapping:"+ typeMapping);
        }
        StrategyDTO serializerMapping = selectSerializerStrategyFromList(serializerList);
        BaseAdaptiveSerializer baseAdaptiveSerializer = newSerializer();
        return baseAdaptiveSerializer.getXMLTemplate(sourceObject, this,serializerMapping);
    }

    private StrategyDTO selectSerializerStrategyFromList(List<StrategyDTO> serializerList) {
        return serializerList.get(0);
    }


    /**
     * This shud take
     * XmlObject in case of xmlBean Implementation
     * javax.xml.bind.Element in case of JAXB implementation
     * and return a java object type
     */
    public Object deserialize(Object sourceXmlObject, XMLMapping typeMapping) {
        List<StrategyDTO> deserializerList = SOAContextUtil.getDeserializersForTypeMapping(adaptiveMapDTO, typeMapping);
        if(deserializerList.size() == 0) {
            throw new SoajRuntimeException("no strategy found for mapping = " + typeMapping);
        }
        StrategyDTO deserializerMapping = selectDeserializerStrategyFromList(deserializerList);
        BaseAdaptiveDeserializer baseAdaptiveSerializer = newDeserializer();
        return baseAdaptiveSerializer.getJavaTemplate(sourceXmlObject, this,deserializerMapping);
    }

    public Source serializeAsSource(Object javaObj, XMLMapping mapping) {
        Object xmlObject = serialize(javaObj, mapping);
        String reflectionUtilClassName = configurationDetailDTO.getReflectionUtilClassName();
        BaseReflectionUtil reflectionUtil= (BaseReflectionUtil)SOAReflectionUtil.createJavaObject(reflectionUtilClassName);
        String xmlSource = reflectionUtil.convertXMLObjectToString(xmlObject, this.getClassLoader(), mapping.getXmlType());
        Document document = SOAUtil.convertStringToDocument(xmlSource);
        return new DOMSource(document);
    }


    protected StrategyDTO selectDeserializerStrategyFromList(List<StrategyDTO> deserializerList) {
        return deserializerList.get(0);
    }

    /**
     * select valid strategy. When multiple adaptiveMap tags are found for the same mapping of a java class and
     * xmlType user can override this method to return the valid or appropriate one.
     * By default the first mapping will be used
     */
    public StrategyDTO selectValidSerializationStrategy(XMLMapping typeMapping, StrategyDTO[] strategyDTOs) throws SoajException {
        for (StrategyDTO strategyDTO : strategyDTOs) {
            RuleDTO[] ruleDTOs = strategyDTO.getRuleDTOs();
            for (RuleDTO ruleDTO : ruleDTOs) {
                BaseBindingRule bindingRule = newBindingRule(ruleDTO);
                XMLMapping ruleTypeMapping;
                ruleTypeMapping = bindingRule.getTypeMappingImpliedByRule(typeMapping, this);
                List<? extends StrategyDTO> list = SOAContextUtil.getSerializersForMapping(adaptiveMapDTO, ruleTypeMapping);
                if (list.isEmpty()) {
                    throw new SoajException("No Valid Strategy");
                }
            }
        }
        throw new SoajException("No valid strategy");
    }

    public final void init(String adaptiveMappingFile, URL[] urls, ConfigurationDetailDTO detailDTO) {
      
        adaptiveMapDTO = AdaptiveMapper.parseAdaptiveXML(adaptiveMappingFile);
        this.configurationDetailDTO = detailDTO;
        schemaURLS = urls;
        loadSchemas();
        
    }

    protected abstract BaseBindingRule newBindingRule(RuleDTO ruleDTO);

    protected abstract void loadSchemas();

    protected abstract BaseAdaptiveDeserializer newDeserializer();

    protected abstract BaseAdaptiveSerializer newSerializer();

}
