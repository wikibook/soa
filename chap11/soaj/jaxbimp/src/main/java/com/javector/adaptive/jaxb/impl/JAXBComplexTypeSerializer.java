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

import com.javector.adaptive.framework.dto.SerializeBindingRuleDTO;
import com.javector.adaptive.framework.interfaces.BaseAdaptiveSerializer;
import com.javector.adaptive.framework.interfaces.XMLMapping;
import com.javector.adaptive.framework.model.RuleDTO;
import com.javector.adaptive.framework.model.XmlWrapDTO;
import com.javector.adaptive.framework.util.SOAReflectionUtil;
import com.javector.adaptive.framework.constants.SOAConstant;
import com.javector.soaj.SoajException;
import com.javector.soaj.SoajRuntimeException;
import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;

import javax.xml.namespace.QName;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rohit agarwal
 * Date: Apr 8, 2006
 * Time: 2:57:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class JAXBComplexTypeSerializer extends BaseAdaptiveSerializer {



    private static SoajLogger logger = LoggingFactory.getLogger(JAXBComplexTypeSerializer.class.getName());

    /**
     * CallBack from baseAdaptiveSerializer for handling complex type wraps
     * @param serializeBindingRuleDTO
     * @throws SoajException
     */
    public Object handleArray(SerializeBindingRuleDTO serializeBindingRuleDTO) {
            Object propertyValue = serializeBindingRuleDTO.getPropertyValue();
            RuleDTO ruleDTO = serializeBindingRuleDTO.getRuleDTO();
        String[] xmlTypes = ruleDTO.getWrapDTO().getXmlWrapDTO().getXmlType().split(SOAConstant.ADAPTIVE_PATH_SEPERATOR);
        String uri = reflectionUtil.getNameSpaceUri(serializeBindingRuleDTO.getTargetJaxbObject());
        QName name = new QName(uri,xmlTypes[xmlTypes.length-1]);
        Object childJaxbObject = reflectionUtil.createXMLObject(name,serializeBindingRuleDTO.getCl());
            if(propertyValue==null) {
                throw new SoajRuntimeException("Property value is null set a proper property value for rule =" +serializeBindingRuleDTO.getRuleDTO().getJavaName());
            }
            List<Object> childList;
            int length = Array.getLength(propertyValue);
            QName childXmlType;
            childXmlType = ruleDTO.getXmlType();
                childList = reflectionUtil.getList(childJaxbObject, ruleDTO.getXmlName());
                for (int i = 0; i < length; i++) {
                    String className = Array.get(propertyValue, i).getClass().getName();
                    XMLMapping typeMapping = new XMLMapping(childXmlType, childXmlType, className);
                    Object obj = adaptiveContext.serialize(Array.get(propertyValue, i), typeMapping);
                    childList.add(obj);
                }
                reflectionUtil.setProperty(childJaxbObject, childList, ruleDTO.getXmlName());

            return childJaxbObject;
        }

    /**
     * Call back from BaseAdaptiveSerializer for creating xml child Object for
     * complex type
     * @param serializeBindingRuleDTO
     * @return
     * @throws SoajException
     */

    public Object createXMLChildObject(SerializeBindingRuleDTO serializeBindingRuleDTO) {
        RuleDTO ruleDTO  = serializeBindingRuleDTO.getRuleDTO();
        Object targetJaxbObject = serializeBindingRuleDTO.getTargetJaxbObject();
        Object propertyValue = serializeBindingRuleDTO.getPropertyValue();
        Object childJaxbObject;
        if(ruleDTO.getWrapDTO()!=null) {
            if(SOAReflectionUtil.isCollectionType(propertyValue) || propertyValue.getClass().isArray()) {
                if(SOAReflectionUtil.isCollectionType(propertyValue))
                propertyValue = ((Collection)propertyValue).toArray();
                serializeBindingRuleDTO.setPropertyValue(propertyValue);
                childJaxbObject = handleArray(serializeBindingRuleDTO);
            }else {
                XMLMapping mapping = new XMLMapping(ruleDTO.getXmlType(),ruleDTO.getXmlName(),propertyValue.getClass().getName());
                childJaxbObject = adaptiveContext.serialize(propertyValue,mapping);
            }
            XmlWrapDTO xmlWrapDTO = ruleDTO.getWrapDTO().getXmlWrapDTO();
            String[] xmlNames = xmlWrapDTO.getXmlName().split(SOAConstant.ADAPTIVE_PATH_SEPERATOR);
            String[] xmlTypes = xmlWrapDTO.getXmlType().split(SOAConstant.ADAPTIVE_PATH_SEPERATOR);
            String uri = reflectionUtil.getNameSpaceUri(serializeBindingRuleDTO.getTargetJaxbObject());

            for(int i= xmlNames.length-2;i>=0;i--) {
                         QName currentXMLName = new QName(uri,xmlTypes[i]);
                         Object tempJavaObject = reflectionUtil.createXMLObject(currentXMLName,serializeBindingRuleDTO.getCl());
                         SOAReflectionUtil.setPropertyValueDefault(tempJavaObject,xmlNames[i+1],childJaxbObject);
                         childJaxbObject = tempJavaObject;                         
                     }

            return childJaxbObject;
        }
        QName childXmlType;
        childXmlType = ruleDTO.getXmlType();
        if (childXmlType == null) {
            childXmlType = reflectionUtil.getPropertySchemaType(targetJaxbObject, ruleDTO.getXmlName());
        }
        XMLMapping typeMapping = new XMLMapping(childXmlType, childXmlType, propertyValue.getClass().getName());
        return adaptiveContext.serialize(propertyValue, typeMapping);
    }
}
