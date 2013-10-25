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
import com.javector.adaptive.framework.interfaces.BaseAdaptiveSerializer;
import com.javector.adaptive.framework.model.RuleDTO;
import com.javector.adaptive.framework.dto.SerializeBindingRuleDTO;
import com.javector.adaptive.framework.constants.SOAConstant;
import com.javector.soaj.SoajException;
import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;

import javax.xml.namespace.QName;

/**
 * Created by IntelliJ IDEA.
 * User: rohit agarwal
 * Date: Apr 8, 2006
 * Time: 2:56:50 PM
 * Default handler defined in mappingHandlers.xml file
 */
public class JAXBSimpleTypeSerializer extends BaseAdaptiveSerializer {

    private static SoajLogger logger = LoggingFactory.getLogger(JAXBSimpleTypeSerializer.class.getName());


    /**
     * Callback from BaseAdaptiveSerializer for creating simple type xml Objects
     * @param serializeBindingRuleDTO
     * @return
     * @throws SoajException
     */

    public Object createXMLChildObject(SerializeBindingRuleDTO serializeBindingRuleDTO) {
        RuleDTO ruleDTO = serializeBindingRuleDTO.getRuleDTO();
        Object targetJaxbObject = serializeBindingRuleDTO.getTargetJaxbObject();
        if(ruleDTO.getWrapDTO()== null) {
            return serializeBindingRuleDTO.getTargetJaxbObject();
        }
        Object childJaxbObject;
        String xmlTypePath = ruleDTO.getWrapDTO().getXmlWrapDTO().getXmlType();
        Object propertyValue = serializeBindingRuleDTO.getPropertyValue();
        String[] xmlObjectTypes = xmlTypePath.split(SOAConstant.ADAPTIVE_PATH_SEPERATOR);
        String[] xmlNames = ruleDTO.getWrapDTO().getXmlWrapDTO().getXmlName().split(SOAConstant.ADAPTIVE_PATH_SEPERATOR);
        String uri = reflectionUtil.getNameSpaceUri(targetJaxbObject);
        QName xmlType = new QName(uri,xmlObjectTypes[xmlObjectTypes.length-1]);
        childJaxbObject = reflectionUtil.createXMLObject(xmlType, serializeBindingRuleDTO.getCl());
        reflectionUtil.setProperty(childJaxbObject,propertyValue,ruleDTO.getXmlName());
        for(int i= xmlObjectTypes.length-2;i>=0;i--) {
            QName childType = new QName(uri,xmlObjectTypes[i]);
            QName childName = new QName(uri, xmlNames[i+1]);
            Object parentObject = reflectionUtil.createXMLObject(childType,serializeBindingRuleDTO.getCl());
            reflectionUtil.setProperty(parentObject,childJaxbObject,childName);
            childJaxbObject = parentObject;
        }
        return childJaxbObject;
    }
}