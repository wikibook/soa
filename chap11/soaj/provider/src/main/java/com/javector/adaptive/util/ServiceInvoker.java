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
package com.javector.adaptive.util;

import com.javector.adaptive.framework.AdaptiveContextFactory;
import com.javector.adaptive.framework.Configurator;
import com.javector.adaptive.framework.ConfiguratorFactory;
import com.javector.adaptive.framework.interfaces.AdaptiveContext;
import com.javector.adaptive.util.dto.SOAJOperationDTO;
import com.javector.adaptive.util.helper.DeserializeHelper;
import com.javector.adaptive.util.helper.SerializationHelper;
import com.javector.soaj.SoajException;
import com.javector.soaj.SoajRuntimeException;
import com.javector.soaj.deploy.invocation.SoajMethod;
import com.javector.soaj.deploy.invocation.SoajMethodFactory;
import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;
import com.javector.soaj.util.IOUtil;

import javax.xml.transform.Source;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ServiceInvoker {
  
  SoajLogger log = LoggingFactory.getLogger(ServiceInvoker.class);

    Collection<Map> services = new ArrayList<Map>();
    AdaptiveContext adaptiveContext = null;


    public ServiceInvoker(String mappingXml, ArrayList<String> userDefinedSchemas) {

        URL[] urlArray = new URL[userDefinedSchemas.size()];
        for (int i = 0; i < userDefinedSchemas.size(); i++) {
            String s = userDefinedSchemas.get(i);
            URL resource = Thread.currentThread().getContextClassLoader().getResource(s);
            if (resource == null) {
                try {
                    resource = new URL(s);
                } catch (MalformedURLException e) {
                    throw new RuntimeException("Invalid URL:" + s, e);
                }
            }
            urlArray[i] = resource;
        }
        Configurator configurator = ConfiguratorFactory.DEFAULT_JAXB_CONFIGURATOR;
        adaptiveContext = AdaptiveContextFactory.createNewContext(configurator, mappingXml, urlArray);

    }


    /**
     * Invoke the target method that is referenced by the SOAJ operation.  The
     * parameters passed to this method are in XML form and must be deserialized
     * before being used for Java invocation.
     *
     * @param soajOperationDTO The data type object (DTO) representing the target method
     *                         to be invoked.
     * @param xmlParamObjects  The parameters in XML form to pass to the target
     *                         method.
     * @return the Object returned by the invoked method
     */
    public Object invokeSOAService(SOAJOperationDTO soajOperationDTO,
                                   List<Source> xmlParamObjects) throws SoajException {

        if (soajOperationDTO == null) {
            throw new SoajRuntimeException("DTO is null.");
        }
        SoajMethod method = new SoajMethodFactory().createSoajMethod(soajOperationDTO);
        // TODO we should not be passing XML around as Strings.  Should always use Source
        List<Object> paramObjects = DeserializeHelper.getJaxbParamObjects(xmlParamObjects, soajOperationDTO.getParamMapping(),soajOperationDTO.getParamClasses(),adaptiveContext);
        Object retJavaObject;
        try {
            retJavaObject = method.invoke(paramObjects);
            if(retJavaObject == null) {
                return "void";
            }
            log.info("SoajMethod returned object with runtime class = " +
                retJavaObject.getClass());
           // String xmlObjectAsString = SerializationHelper.doSerialization(retJavaObject, soajOperationDTO,adaptiveContext);
            String xmlObjectAsString = SerializationHelper.doSerialization(retJavaObject, soajOperationDTO.getReturnJavaClass(),soajOperationDTO.getReturnType(),soajOperationDTO.getReturnTypeName(),adaptiveContext);
            xmlObjectAsString = removeXMLPrologue(xmlObjectAsString);
            log.info("returning this serialized object: " + IOUtil.NL + xmlObjectAsString);
            return xmlObjectAsString;
        } catch (InvocationTargetException e) {
            throw new SoajException("Failure invoking the target method. className: " +
                    soajOperationDTO.getTargetServiceClassName() + "  methodName:" +
                    soajOperationDTO.getTargetServiceMethodName(), e);

        }
    }

    private String removeXMLPrologue(String xmlObjectAsString) {
       xmlObjectAsString = xmlObjectAsString.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>","").replaceAll("billToType","billTo");
       return xmlObjectAsString;
    }

}
