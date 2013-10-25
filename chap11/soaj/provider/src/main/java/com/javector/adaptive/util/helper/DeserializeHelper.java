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
package com.javector.adaptive.util.helper;

import com.javector.adaptive.framework.interfaces.AdaptiveContext;
import com.javector.adaptive.framework.interfaces.XMLMapping;
import com.javector.adaptive.framework.util.BaseReflectionUtil;
import com.javector.adaptive.framework.util.SOAUtil;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: javector
 * Date: May 27, 2006
 * Time: 4:57:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeserializeHelper {
    static AdaptiveContext adaptiveContext = null;
    /**
     * Deserializes the XML parameters into Java parameters accoring to the
     * parameter type mappings specified by the SOAJ operation.  The rules for
     * performing the deserialization are contained in the AdaptiveContext
     * local to this ServiceInvoker instance.
     *
     * @param xmlParamObjects The parameters as XML
     * @param parameterMap
     * @param paramClasses
     * @param adaptiveCtx
     * @return The parameters as Java objects.
     */
    public static List<Object> getJaxbParamObjects(List<Source> xmlParamObjects, List<XMLMapping> parameterMap,List<String> paramClasses, AdaptiveContext adaptiveCtx) {
        adaptiveContext = adaptiveCtx;
        List<Object> jaxbParamObjects = new ArrayList<Object>();
        Source param = null;
        String paramClass = null;
        QName paramQnameType = null;
        QName paramQnameName = null;
        for (int i = 0; i < xmlParamObjects.size(); i++) {
            param = xmlParamObjects.get(i);
            paramClass = paramClasses.get(i);
            // if(parameterMap.get(i).getXmlType() != null){
                  paramQnameType = parameterMap.get(i).getXmlType();
            //  }else {

                  paramQnameName = parameterMap.get(i).getXmlName();
            //  }
            boolean simpleType = BaseReflectionUtil.isSimpleType(paramQnameType);
            if(simpleType) {
                String nodeValue = SOAUtil.getXMLNodeFromSource(param).getFirstChild().getNodeValue();
                jaxbParamObjects.add(nodeValue);
            }else
            jaxbParamObjects.add(getJaxbObjectFromXml(param, paramQnameType, paramQnameName, paramClass));
        }
        return jaxbParamObjects;
    }

    private static Object getJaxbObjectFromXml(Source xmlSource, QName mappingQnameType, QName mappingQnameName, String javaClassName) {
        // TODO what about case when this is XmlNameMapping and not XmlTypeMapping ??
        // XmlTypeMapping mapping = new XmlTypeMapping(mappingQname, javaClassName);
        XMLMapping mapping = new XMLMapping(mappingQnameType,mappingQnameName,javaClassName);
        return adaptiveContext.deserialize(xmlSource, mapping);
    }

}
