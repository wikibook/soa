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
package com.javector.adaptive.ws.processor;

import com.javector.soaj.SoajRuntimeException;
import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;
import com.javector.soaj.provider.SoajInstanceConfiguration;
import com.javector.soaj.provider.processing.WSRequest;
import com.javector.adaptive.util.dto.SOAConfigDTO;
import com.javector.adaptive.util.SOAConfigReader;
import com.javector.soaj.util.IOUtil;
import com.javector.soaj.util.ResourceFinder;
import com.javector.soaj.util.SoapFaultUtil;
import com.javector.soaj.util.XmlUtil;

import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kishoreG
 * Date: Apr 15, 2006
 * Time: 4:04:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class InputDataProcessor {

  private static SoajLogger log = LoggingFactory.getLogger(InputDataProcessor.class);
  
    private WSRequest wsRequest = new WSRequest();

    public InputDataProcessor() {
        init();
    }

    /**
     * In this method parse the soaj-config.xml and set the configDTO
     */
    private void init() {
        SOAConfigReader soaConfigReader = new SOAConfigReader();
        URL soajConfigURL = ResourceFinder.getResourceUrl("config/SoajConfig.xml", InputDataProcessor.class);
        SOAConfigDTO soaConfigDTO =  soaConfigReader.parserSOAConfig(SoajInstanceConfiguration.getSoajConfigAsStream());
        soaConfigDTO.setLastModified(SoajInstanceConfiguration.getLastModified());
        wsRequest.setSoaConfigDTO(soaConfigDTO);
    }

    /**
     * This method will parse the data in the input source and also webservicecontext
     * and populate WSRequest object which will be in a more readable and usable format
     *
     * @param webServiceContext
     * @param source
     * @return WSRequest
     * @throws SOAPFaultException if an error occur because the request cannot
     * be processed (e.g., badly formed XML request)
     */
    public WSRequest processServiceRequest(WebServiceContext webServiceContext, Source source) {
      MessageContext mc = webServiceContext.getMessageContext();
      try {
        String query = (String) mc.get(MessageContext.QUERY_STRING);
        String path = (String) mc.get(MessageContext.PATH_INFO);
        wsRequest.setQueryString(query);
        wsRequest.setPathString(path);
        // for testing purpose
        wsRequest.setServiceRequest(false);
        wsRequest.setWSDLRequest(false);
        wsRequest.setServiceRequest(false);
        if (source != null) {
          wsRequest.setServiceRequest(true);
        } else if (query.equals("?WSDL")) {
          wsRequest.setWSDLRequest(true);
        }


            DOMResult dom = new DOMResult();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, dom);
            Node node = dom.getNode();
            Node root = node.getFirstChild();
            if ( root != null ) { // source might be an empty XML
                wsRequest.setOperationName(root.getLocalName());
                wsRequest.setXmlParameters(parseParamStrings(XmlUtil.toString(root)));
            }else{
                throw  new SoajRuntimeException("Source is empty");
            }

      } catch (SoajRuntimeException sre) {
        throw new SOAPFaultException(SoapFaultUtil.toSOAPFault(sre));
      } catch (TransformerException e) {
        throw new SOAPFaultException(SoapFaultUtil.toSOAPFault(e));
      }
          return wsRequest;
    }

    public WSRequest processWsdlRequest(HttpServletRequest httpServletRequest) {
        String queryString = httpServletRequest.getQueryString();
        wsRequest.setQueryString(queryString);
        wsRequest.setPathString(httpServletRequest.getContextPath());
        wsRequest.setRequestURL(httpServletRequest.getRequestURL().toString());
        // wsRequest.setServiceRequest(false);
           //   wsRequest.setWSDLRequest(false);
       // if( "wsdl".equalsIgnoreCase(queryString)){
            wsRequest.setServiceRequest(false);
            wsRequest.setWSDLRequest(true);
       // }
        return wsRequest;
    }

    private List<Source> getSourceListFromNodeList(NodeList nodeList){
        List<Source> sources = new ArrayList<Source>();
        for (int i = 0; i < nodeList.getLength(); i++) {
           Source source = new DOMSource( nodeList.item(i));
           sources.add(source);
        }

        return sources;
    }

    public List<Source> parseParamStrings(String soapData) {
      
      log.info("soapData = " + soapData);
        List<String>  params = new ArrayList<String>();
        ByteArrayInputStream stream = new ByteArrayInputStream(soapData.getBytes());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document document;
        try {
            document = factory.newDocumentBuilder().parse(stream);
        } catch (Exception e) {
            throw new SoajRuntimeException(
                "Failure while parsing parameters: " + IOUtil.NL + soapData, e);
        }
        Node node;
        node = document.getChildNodes().item(0);
        System.out.println("node.getNodeName() = " + node.getNodeName());
        Node paramNode;
        NodeList list = node.getChildNodes();
        List<Source> sourceList = getSourceListFromNodeList(list);
        return sourceList;
//        for (int i = 0; i < list.getLength(); i++) {
//            paramNode = list.item(i);
//            if (paramNode.getNodeName().indexOf("?xml version=") != -1) {
//                continue;
//            }
//            params.add(XmlUtil.toString(paramNode));
//        }
//        return params;
    }

}