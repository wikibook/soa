/*
* Copyright 2006 Javector Software LLC
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
package samples;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@WebServiceProvider(serviceName = "RequestOrderService",
    portName="RequestOrderPort",
    targetNamespace = "http://www.example.com/req", 
    wsdlLocation="WEB-INF/wsdl/RequestOrder.wsdl")
@ServiceMode(Service.Mode.PAYLOAD)
public class RequestOrderEndpoint implements Provider<Source> {
  
  @Resource
  WebServiceContext webServiceContext;
  
  public Source invoke(Source payload) {

    try {

      // convert payload to a DOMSource to support multiple reads.
      // this is a kludge used only to preserve the code in the examples
      // below after GlassFish changed the implementation of JAX-WS.
      DocumentBuilderFactory dbfac1 = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder1 = dbfac1.newDocumentBuilder();
      Document payloadDoc1 = docBuilder1.newDocument();
      Transformer xformer1 = TransformerFactory.newInstance().newTransformer();
      xformer1.transform(payload, new DOMResult(payloadDoc1));
      payload = new DOMSource(payloadDoc1);

      String errorMsg = validateAgainstWSDL(payload);
      if ( errorMsg != null ) {
        SOAPFactory fac = SOAPFactory.newInstance(); 
        SOAPFault sf = fac.createFault(
            "SOAP payload is invalid with respect to WSDL.",
            new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client"));
        Detail d = sf.addDetail();
        SOAPElement de = d.addChildElement(new QName(
            "http://www.example.com/faults", "inputMessageValidationFault"));
        de.addAttribute(new QName("", "msg"), errorMsg.replaceAll("\"",""));
        throw new SOAPFaultException(sf);
      }
      
      //! <example xn="BIND_WITH_CASTOR">
      //! <c>chap07</c><s>castor</s>
      if ( webServiceContext == null ) {
        throw new RuntimeException("WebServiceContext not injected.");
      }
      MessageContext mc = webServiceContext.getMessageContext();
      ServletContext sc = 
        (ServletContext) mc.get(MessageContext.SERVLET_CONTEXT);
      if ( sc == null ) {
        throw new RuntimeException("ServletContext is null.");
      }
      InputStream castorMappingFile = 
        sc.getResourceAsStream("/WEB-INF/castor/mapping.xml");
      if ( castorMappingFile == null ) {
        throw new IOException("Castor mapping file not found."); 
      }
      Mapping castorMapping = new Mapping();
      castorMapping.loadMapping(new InputSource(castorMappingFile));
      Unmarshaller u = new Unmarshaller(castorMapping);
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document payloadDOM = db.newDocument();
      Transformer xf = TransformerFactory.newInstance().newTransformer();
      xf.transform(payload, new DOMResult(payloadDOM));
      MyRequestOrder reqOrder = (MyRequestOrder) u.unmarshal(payloadDOM);
      //! </example>     

      //! <example xn="BUILD_RESPONSE_WITH_CUSTOM_CLASSES">
      //! <c>chap07</c><s>castor</s>
      MyRequestOrderResponse response;
      try {
        response = reqOrder.processOrder();
      } catch (PaymentException pe) {
        SOAPFactory fac = SOAPFactory.newInstance(); 
        SOAPFault sf = fac.createFault(pe.getMessage(),
            new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client"));
        throw new SOAPFaultException(sf);
      }
      //! </example>

      //! <example xn="MARSHAL_RESPONSE_WITH_CASTOR">
      //! <c>chap07</c><s>castor</s>
      ByteArrayOutputStream ba = new ByteArrayOutputStream();
      Marshaller m = new Marshaller(new OutputStreamWriter(ba));
      m.setMapping(castorMapping);
      m.marshal(response);
      return new StreamSource(new StringReader(ba.toString()));
      //! </example>
    }  catch (RuntimeException re) {
      throw re;
    }  catch (Exception e) {
      e.printStackTrace();
      throw new WebServiceException("Unexpected endpoint exception.", e);
    }

  }
  
  private String validateAgainstWSDL(Source payload) throws Exception {
    
    if ( webServiceContext == null ) {
      throw new RuntimeException("WebServiceContext not injected.");
    }
    MessageContext mc = webServiceContext.getMessageContext();
    ServletContext sc = (ServletContext) mc.get(MessageContext.SERVLET_CONTEXT);
    if ( sc == null ) {
      throw new RuntimeException("ServletContext is null.");
    }
    InputStream wsdlStream = 
      sc.getResourceAsStream("/WEB-INF/wsdl/RequestOrder.wsdl");
    if ( wsdlStream == null ) {
      throw new IOException("WSDL resource not found."); 
    }
    DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
    Document wsdlDoc = docBuilder.newDocument();
    Transformer xformer = TransformerFactory.newInstance().newTransformer();
    xformer.transform(new StreamSource(wsdlStream), new DOMResult(wsdlDoc));
    NodeList schemaNodes = wsdlDoc.getElementsByTagNameNS(
        XMLConstants.W3C_XML_SCHEMA_NS_URI, "schema");
    int numOfSchemas = schemaNodes.getLength();
    Source[] schemas = new Source[numOfSchemas];
    Document schemaDoc;
    Element schemaElt;
    for (int i=0; i < numOfSchemas; i++) {
      schemaDoc = docBuilder.newDocument();
      NamedNodeMap nsDecls = getNamespaces((Element) schemaNodes.item(i));
      schemaElt = (Element) schemaDoc.importNode(schemaNodes.item(i), true);
      for (int j=0; j<nsDecls.getLength(); j++) {
        Attr a = (Attr) schemaDoc.importNode(nsDecls.item(j), true);
        schemaElt.setAttributeNodeNS(a);
      }
      schemaDoc.appendChild(schemaElt);
      schemas[i] = new DOMSource(schemaDoc);
    }
    SchemaFactory schemaFac = SchemaFactory.
    newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Schema schema = schemaFac.newSchema(schemas);
    Validator validator = schema.newValidator();
    if (!DOMSource.class.isInstance(payload) &&
        !SAXSource.class.isInstance(payload) ) {
      Document payloadDoc = docBuilder.newDocument();
      xformer.transform(payload, new DOMResult(payloadDoc));
      payload = new DOMSource(payloadDoc);
    }
    try {
      validator.validate(payload);
    } catch (SAXException se) {
      se.printStackTrace();
      return "validation error: " + se.getMessage();
    } catch (IOException e) {
      e.printStackTrace();
      return "validation error: " + e.getMessage();
    }
    return null;
    
  }
  
  private NamedNodeMap getNamespaces(Element e) {
    
    Element n = (Element) e.cloneNode(true);
    NamedNodeMap attrs = n.getAttributes();
    for (int i=0; i< attrs.getLength(); i++) {
      Attr attr = (Attr) attrs.item(i);
      String prefix = attr.getPrefix();
      String name = attr.getLocalName();
      if ( prefix == null || !prefix.equals("xmlns")) {
        attrs.removeNamedItem(name);
      }
    }
    Node parent = e.getParentNode();
    if ( parent != null && parent.getNodeType() == Node.ELEMENT_NODE ) {
      NamedNodeMap parentAttrs = getNamespaces((Element) parent);
      for (int i=0; i<attrs.getLength(); i++) {
        parentAttrs.setNamedItem(attrs.item(i).cloneNode(true));
      }
      return parentAttrs;
    }
    return attrs;
    
  }
  
}
