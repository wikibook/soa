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

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.soap.*;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//! <example xn="VALIDATION_HANDLER">
//! <c>chap07</c><s>handler</s>
public class ValidationHandler implements SOAPHandler<SOAPMessageContext> {
  
  public boolean handleMessage(SOAPMessageContext context) {

    if ( ((Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).
        booleanValue() ) return true;
    try {
      SOAPMessage message = context.getMessage();
      SOAPBody body = message.getSOAPBody();
      SOAPElement requestElt =
        (SOAPElement) body.getFirstChild();
      String errMsg = validateAgainstWSDL(context, requestElt);
      if ( errMsg == null ) {
        return true;
      }
      SOAPFactory fac = SOAPFactory.newInstance();
      SOAPFault sf = fac.createFault(
          "SOAP payload is invalid with respect to WSDL.",
          new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client"));
      Detail d = sf.addDetail();
      SOAPElement de = d.addChildElement(new QName(
          "http://www.example.com/faults", "inputMessageValidationFault"));
      de.addAttribute(new QName("", "msg"), errMsg.replaceAll("\"",""));
      throw new SOAPFaultException(sf);
      
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebServiceException(e);
    }
    
  }
//! </example>
  
  public Set<QName> getHeaders() {
    return null;
  }
  
  public boolean handleFault(SOAPMessageContext context) {
    return true;
  }
  
  public void close(MessageContext context) {}
  
  private String validateAgainstWSDL(MessageContext mc, 
      SOAPElement requestElt) throws Exception {
    
    ServletContext sc = 
      (ServletContext) mc.get(MessageContext.SERVLET_CONTEXT);
    if ( sc == null ) {
      throw new RuntimeException("ServletContext is null.");
    }
    Set wsdlSet = sc.getResourcePaths("/WEB-INF/wsdl/");
    String wsdlPath = null;
    for (Object o : wsdlSet) {
      wsdlPath = (String) o;
      if ( wsdlPath.endsWith(".wsdl")) break;
    }
    InputStream wsdlStream = sc.getResourceAsStream(wsdlPath);
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
    Document payloadDoc = docBuilder.newDocument();
    xformer.transform(new DOMSource(requestElt), new DOMResult(payloadDoc));
    try {
      validator.validate(new DOMSource(payloadDoc));
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
