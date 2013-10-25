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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import javax.xml.transform.stream.StreamResult;
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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

//! <example xn="PROVIDER_WEBSERVICECONTEXT_INJECTION">
//! <c>chap07</c><s>provider</s>
@WebServiceProvider(serviceName = "RequestOrderService",
    portName="RequestOrderPort",
    targetNamespace = "http://www.example.com/req", 
    wsdlLocation="WEB-INF/wsdl/RequestOrder.wsdl")
@ServiceMode(Service.Mode.PAYLOAD)
public class RequestOrderEndpoint implements Provider<Source> {
  
  @Resource
  WebServiceContext webServiceContext;
  
  private static final String REQ_NS = "http://www.example.com/req";
  private static final String OMS_NS = "http://www.example.com/oms";
  
    public Source invoke(Source payload) { 

//! </example>
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

      //! <example xn="CREATE_SOAP_FAULT_EXCEPTION">
      //! <c>chap07</c><s>faults</s>
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
      //! </example>      
      
      // build response
//! <example xn="DOM_IS_UGLY">
//! <c>chap07</c><s>provider</s>
      DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
      Document respDoc = docBuilder.newDocument();
      Document payloadDoc = docBuilder.newDocument();
      Transformer xformer = TransformerFactory.newInstance().newTransformer();
      xformer.transform(payload, new DOMResult(payloadDoc));
      Element responseElt = 
        respDoc.createElementNS(REQ_NS, "requestOrderResponse");
      Element orderElt = respDoc.createElementNS(OMS_NS, "Order");
      responseElt.appendChild(orderElt);
      Element orderKeyElt = respDoc.createElementNS(OMS_NS, "OrderKey");
      orderElt.appendChild(orderKeyElt);
      //  generate a psuedo-unique 10 digit order ID
      String orderId = Long.toString((new Date()).getTime());
      orderId = orderId.substring(orderId.length()-10);
      orderKeyElt.appendChild(respDoc.createTextNode(orderId));
      Element orderHeaderElt =  respDoc.createElementNS(OMS_NS, "OrderHeader");
      orderElt.appendChild(orderHeaderElt);
      // items wrapper comes after header
      Element orderItemsElt = respDoc.createElementNS(OMS_NS, "OrderItems");
      orderElt.appendChild(orderItemsElt);
      Element salesOrgElt =  respDoc.createElementNS(OMS_NS, "SALES_ORG");
      orderHeaderElt.appendChild(salesOrgElt);
      salesOrgElt.appendChild(respDoc.createTextNode("WEB"));
      Element purchDateElt = respDoc.createElementNS(OMS_NS, "PURCH_DATE");
      orderHeaderElt.appendChild(purchDateElt);
      purchDateElt.appendChild(
          respDoc.createTextNode(dateAsString(new GregorianCalendar())));
      Element custNoElt = respDoc.createElementNS(OMS_NS, "CUST_NO");
      orderHeaderElt.appendChild(custNoElt);
      // get CUST_NO from payload
      NodeList nl = payloadDoc.getElementsByTagNameNS(REQ_NS, "CUST_NO");
      custNoElt.appendChild(respDoc.createTextNode(
          ((Text)((Element) nl.item(0)).getFirstChild()).getNodeValue()));
      //! </example>
      Element paymethElt = respDoc.createElementNS(OMS_NS, "PYMT_METH");
      orderHeaderElt.appendChild(paymethElt);
      // get PURCH_ORD_NO or ccard from payload
      nl = payloadDoc.getElementsByTagNameNS(REQ_NS, "PURCH_ORD_NO");
      if ( nl.getLength() > 0 ) { // use purchase order
        paymethElt.appendChild(respDoc.createTextNode("PO"));
        Element poNumElt = respDoc.createElementNS(OMS_NS, "PURCH_ORD_NO");
        orderHeaderElt.appendChild(poNumElt);
        poNumElt.appendChild(respDoc.createTextNode(
            ((Text)((Element) nl.item(0)).getFirstChild()).getNodeValue()));
      } else { // use ccard
        paymethElt.appendChild(respDoc.createTextNode("CC"));
        nl = payloadDoc.getElementsByTagNameNS(REQ_NS, "ccard");
        if ( nl.getLength() == 0 ) {
          SOAPFactory fac = SOAPFactory.newInstance(); 
          SOAPFault sf = fac.createFault("No payment info.",
              new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client"));
          throw new SOAPFaultException(sf);
        }
        Element ccardElt = (Element) respDoc.importNode(nl.item(0), true);
        //! <example xn="CREATE_WEBSERVICE_EXCEPTION">
        //! <c>chap07</c><s>faults</s>
        Element expireDateElt = (Element) ccardElt.getElementsByTagNameNS(
            OMS_NS, "CC_EXPIRE_DATE").item(0);
        String expireDateStr =
          ((Text) expireDateElt.getFirstChild()).getNodeValue();
        String today = dateAsString(new GregorianCalendar());
        if (expireDateStr.compareTo(today) < 0 ) {
          throw new WebServiceException("Expired ccard.");
        }
        //! </example>
        Element newCcardElt = respDoc.createElementNS(OMS_NS, "ccard");
        nl = ccardElt.getChildNodes();
        for (int i=0; i<nl.getLength(); i++) {
          newCcardElt.appendChild(nl.item(i).cloneNode(true));
        }
        Element ccWrapperElt = respDoc.createElementNS(OMS_NS, "OrderCcard");
        ccWrapperElt.appendChild(newCcardElt);
        orderElt.appendChild(ccWrapperElt);
      }
      // add the items
      nl = payloadDoc.getElementsByTagNameNS(REQ_NS, "item");
      for (int i=0; i<nl.getLength(); i++) {
        Element itemElt = (Element) respDoc.importNode(nl.item(i), true);
        Element newItemElt = respDoc.createElementNS(OMS_NS, "item");
        NodeList nl2 = itemElt.getChildNodes();
        for (int j=0; j<nl2.getLength(); j++) {
          newItemElt.appendChild(nl2.item(j).cloneNode(true));
        }
        orderItemsElt.appendChild(newItemElt);
      }
      ByteArrayOutputStream ba = new ByteArrayOutputStream();
      xformer.transform(new DOMSource(responseElt), new StreamResult(ba));
      String err = validateAgainstWSDL(
          new StreamSource(new StringReader(ba.toString())));
      if (err != null) {
        throw new WebServiceException("created invalid response: " +
            err.replaceAll("\"",""));
      }
      return new StreamSource(new StringReader(ba.toString()));
    } catch (SOAPFaultException sfe) {
      throw sfe; 
    } catch (RuntimeException re) {
      throw re;
    } catch (Throwable e) {
      e.printStackTrace();
      throw new WebServiceException("Unexpected endpoint exception.", e);
    }

  }
  
  private String dateAsString(GregorianCalendar cal) {
    
    String year = Integer.toString(cal.get(Calendar.YEAR));
    String month = Integer.toString(cal.get(Calendar.MONTH)+1);
    if ( month.length() < 2 ) month = "0" + month;
    String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
    if ( day.length() < 2 ) day = "0" + day;
    return year+"-"+month+"-"+day;
    
  }
  
  private String validateAgainstWSDL(Source payload) throws Exception {
    
    //! <example xn="GET_WSDL_FROM_WEBCONTEXT">
    //! <c>chap07</c><s>faults</s>
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
    //! </example>
    if ( wsdlStream == null ) {
      throw new IOException("WSDL resource not found."); 
    }
    //! <example xn="EXTRACT_SCHEMA_FROM_WSDL_AND_VALIDATE">
    //! <c>chap07</c><s>faults</s>
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
      //! </example> 
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
