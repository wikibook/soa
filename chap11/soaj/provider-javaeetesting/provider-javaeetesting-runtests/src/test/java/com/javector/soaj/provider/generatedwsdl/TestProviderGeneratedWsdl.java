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
package com.javector.soaj.provider.generatedwsdl;

import com.javector.soaj.util.IOUtil;
import com.javector.soaj.util.ResourceFinder;
import com.javector.soaj.util.XmlUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.InputStream;
import java.math.BigInteger;
import  com.javector.ser.adaptive.po.*;

public class TestProviderGeneratedWsdl extends TestCase {
  
  
  public static Test suite() {
    return new TestSuite(TestProviderGeneratedWsdl.class);
  }
  
  protected void setUp() throws Exception {}
  
  public void testProvider() throws Exception {
    SoajProviderService service = new SoajProviderService();
    SoajProviderPortType port =
      service.getSoajProviderPort();
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
      sf.getDetail();
      throw sfe;
    }
    assertNotNull("SOAP Response should not be null.", response);
    System.out.println("bill to city = " + response.getCity());
    assertEquals("Canton", response.getCity());
  }


// TODO Broken.  Giving RMI/CORBA error on server.  Need to fix.  
// public void testProviderEJB30Invocation() throws Exception {
//
//    SoajProviderService service = new SoajProviderService();
//    SoajProviderPortType port =
//      service.getSoajProviderPort();
//    Item item = new Item();
//    item.setPrice(3.99);
//    item.setProductName("Diet Coke");
//    item.setQuantity(BigInteger.valueOf(6));
//    Items items = new Items();
//    items.getItem().add(item);
//    BillToType billTo = new BillToType();
//    billTo.setCity("Canton");
//    billTo.setPhone("(973) 243-8776");
//    billTo.setState("OH");
//    billTo.setStreet("125 Main Street");
//    billTo.setZip("98134");
//    PurchaseOrder po = new PurchaseOrder();
//    po.setBillTo(billTo);
//    po.setItems(items);
//    BillToType response;
//    try {
//      response = port.getBillToFromEJB30(po);
//    } catch (SOAPFaultException sfe) {
//      SOAPFault sf = sfe.getFault();
//      System.out.println("SOAPFault:" + IOUtil.NL +
//          XmlUtil.toFormattedString(sf));
//      throw sfe;
//    }
//    assertNotNull("SOAP Response should not be null.", response);
//    System.out.println("bill to city = " + response.getCity());
//    assertEquals("Canton", response.getCity());
//
//    }

    public void testProviderEJB21Invocation() throws Exception {

    SoajProviderService service = new SoajProviderService();
    SoajProviderPortType port =
      service.getSoajProviderPort();
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
      response = port.getBillToFromEJB21(po);
    } catch (SOAPFaultException sfe) {
      SOAPFault sf = sfe.getFault();
      System.out.println("SOAPFault:" + IOUtil.NL +
          XmlUtil.toFormattedString(sf));
      sf.getDetail();
      throw sfe;
    }
    assertNotNull("SOAP Response should not be null.", response);
    System.out.println("bill to city = " + response.getCity());
    assertEquals("Canton", response.getCity());

    }
}
