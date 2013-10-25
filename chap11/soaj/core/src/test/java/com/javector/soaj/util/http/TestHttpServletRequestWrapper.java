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
package com.javector.soaj.util.http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.javector.soaj.util.IOUtil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestHttpServletRequestWrapper extends TestCase {
  
  public static Test suite() {
    return new TestSuite(TestHttpServletRequestWrapper.class);
  }
  
  protected void setUp() throws Exception {}
  
  
  public void testGetInputStream() throws Exception {
    
    String msg = 
      "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""+
      "  xmlns:ns1=\"http://javector.com/ser/adaptive/po\""+
      "  xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\""+
      "  xmlns:ns2=\"http://javector.com/soaj/provider\""+
      "  xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
      "  <soapenv:Body>"+
      "    <ns2:getBillTo>"+
      "      <ns1:PurchaseOrder>"+
      "        <ns1:billTo>"+
      "          <ns1:street>125 Main Street</ns1:street>"+
      "          <ns1:city>Canton</ns1:city>"+
      "          <ns1:state>OH</ns1:state>"+
      "          <ns1:zip>98134</ns1:zip>"+
      "          <ns1:phone>(973) 243-8776</ns1:phone>"+
      "        </ns1:billTo>"+
      "        <ns1:items>"+
      "          <ns1:item productName=\"Diet Coke\">"+
      "            <ns1:quantity>6</ns1:quantity>"+
      "            <ns1:price>3.99</ns1:price>"+
      "          </ns1:item>"+
      "        </ns1:items>"+
      "      </ns1:PurchaseOrder>"+
      "    </ns2:getBillTo>"+
      "  </soapenv:Body>"+
      "</soapenv:Envelope>";
    HttpServletRequestMock mock = new HttpServletRequestMock(msg);    
    HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(mock);
    InputStream is = wrapper.getInputStream();
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    IOUtil.copy(is, os);
    assertEquals(msg, os.toString());
    
  }
  
}
