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
package com.javector.soaj.provider.staticwsdl;

import com.javector.soaj.provider.SoajProviderService;
import com.javector.soaj.provider.SoajProviderPortType;
import com.javector.soaj.util.IOUtil;
import com.javector.soaj.util.XmlUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

import java.math.BigInteger;
import  com.javector.ser.adaptive.po.*;

public class TestProviderStaticWsdl extends TestCase {
  
  public static void main(String[] args) throws Exception {
    TestProviderStaticWsdl p = new TestProviderStaticWsdl();
    p.testProvider();
  }
  
  public static Test suite() {
    return new TestSuite(TestProviderStaticWsdl.class);
  }
  
  protected void setUp() throws Exception {}
  
  public void testProvider() throws Exception {
    
    SoajProviderService service = new SoajProviderService();
    SoajProviderPortType port = service.getSoajProviderPort();
    Item item = new Item();
    item.setPrice(3.99);
    item.setProductName("Diet Coke");
    item.setQuantity(BigInteger.valueOf(6));
    Items items = new Items();
    items.getItem().add(item);
    BillToType billTo = new BillToType();
    billTo.setCity("Canton");
    billTo.setPhone("(973) 243-8776");
    billTo.setState("OH");
    billTo.setStreet("125 Main Street");
    billTo.setZip("98134");
    PurchaseOrder po = new PurchaseOrder();
    po.setBillTo(billTo);
    po.setItems(items);
    BillToType response;
    try {
      response = port.getBillTo(po);
    } catch (SOAPFaultException sfe) {
      SOAPFault sf = sfe.getFault();
      System.out.println("SOAPFault:" + IOUtil.NL +
          XmlUtil.toFormattedString(sf));
      throw sfe;
    }
    assertNotNull("SOAP Response should not be null.", response);
    System.out.println("bill to city = " + response.getCity());
    assertEquals("Canton", response.getCity());
    
//    String getBillTo_soap = 
//      "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""+
//      "  xmlns:ns1=\"http://javector.com/ser/adaptive/po\""+
//      "  xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\""+
//      "  xmlns:ns2=\"http://javector.com/soaj/provider\""+
//      "  xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
//      "  <soapenv:Body>"+
//      "    <ns2:getBillTo>"+
//      "      <ns1:PurchaseOrder>"+
//      "        <ns1:billTo>"+
//      "          <ns1:street>125 Main Street</ns1:street>"+
//      "          <ns1:city>Canton</ns1:city>"+
//      "          <ns1:state>OH</ns1:state>"+
//      "          <ns1:zip>98134</ns1:zip>"+
//      "          <ns1:phone>(973) 243-8776</ns1:phone>"+
//      "        </ns1:billTo>"+
//      "        <ns1:items>"+
//      "          <ns1:item productName=\"Diet Coke\">"+
//      "            <ns1:quantity>6</ns1:quantity>"+
//      "            <ns1:price>3.99</ns1:price>"+
//      "          </ns1:item>"+
//      "        </ns1:items>"+
//      "      </ns1:PurchaseOrder>"+
//      "    </ns2:getBillTo>"+
//      "  </soapenv:Body>"+
//      "</soapenv:Envelope>";
//    
//    HttpClient client = new HttpClient();
//    //TODO factor out the hardcoded http address
//    PostMethod method = new PostMethod("http://soabookdev.scarsdale.javector.com:8080/provider-staticwsdl/test");
//    method.setRequestHeader("Content-type", "text/xml; charset=utf-8");
//    method.setRequestHeader("Accept", "text/xml, application/xop+xml, text/html. image/gif, image/jpeg, *; q=.2, */*; q=.2");
//    method.setRequestHeader("Connection", "keep-alive");
//    method.addRequestHeader("Soapaction", "");
//    method.setRequestEntity(new StringRequestEntity(getBillTo_soap));
//    client.executeMethod(method);
//    String response = method.getResponseBodyAsString();
//    System.out.println("SOAP response = " + IOUtil.NL + response);
//    assertNotNull("SOAP Response should not be null.", response);
//    assertTrue("SOAP Response should not be empty.", response.length() > 0);
//    assertEquals("HTTP Error.", -1,response.indexOf("<html>"));
//    assertEquals("SOAP Response should not be a fault.", -1,response.indexOf("<faultcode>"));
        
  }

}
