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
package com.javector.ser.framework;

import com.javector.adaptive.framework.AdaptiveContextFactory;
import com.javector.adaptive.framework.XmlTypeMapping;
import com.javector.adaptive.framework.Configurator;
import com.javector.adaptive.framework.ConfiguratorFactory;
import com.javector.adaptive.framework.interfaces.AdaptiveContext;
import com.javector.adaptive.framework.interfaces.XMLMapping;
import com.javector.adaptive.jaxb.impl.AdaptiveJaxbContext;
import com.javector.ser.adaptive.po.*;
import com.javector.soaj.SoajException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import javax.xml.namespace.QName;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Kishore G
 * Date: Jan 20, 2006
 * Time: 3:52:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestJaxBDeserliazer extends TestCase {

    public static Test suite() {
        return new TestSuite(TestJaxBDeserliazer.class);
    }

    public static final String NS_test = "http://javector.com/ser/adaptive/po";
    String xmlFile = "com/javector/ser/framework/SOAAdaptiveSample.xml";
    String handlerMappingFile = "config/SOAJaxbHandlerMapping.xml";
    //static URL[] url = new URL[]{Thread.currentThread().getContextClassLoader().getResource("config/SOA-config1.xsd")};
    static URL[] url = new URL[]{Thread.currentThread().getContextClassLoader().getResource("com/javector/ser/adaptive/po/TestAdaptiveSerializerPO.xsd")};
    AdaptiveContext adaptiveContext = null;
    Configurator configurator;

    static {
        System.out.println("url = " + url);
    }

    protected void setUp() throws Exception {
        configurator = ConfiguratorFactory.createConfigurator(handlerMappingFile);
        adaptiveContext = AdaptiveContextFactory.createNewContext(configurator, xmlFile, url);
    }



  public void testLoadShema() throws Exception {
    AdaptiveJaxbContext adaptiveJaxbContext = new AdaptiveJaxbContext();
    assertTrue(adaptiveContext instanceof AdaptiveJaxbContext);
  }

  public void testXJCTask() throws Exception {
      QName qName = new QName("http://javector.com/ser/adaptive/po", "item");
      XMLMapping typeMapping = new XMLMapping(qName,qName, MyItem.class.getName());
      Object jaxbObject = adaptiveContext.serialize(new MyItem("Dog Food", 100, 1.45), typeMapping);
      Object javaObject = adaptiveContext.deserialize(jaxbObject, typeMapping);
      System.out.println("javaObject = " + javaObject.getClass());
      assertEquals(javaObject.getClass(), MyItem.class);
      MyItem myItem = (MyItem) javaObject;

      assertEquals(myItem.amount, 100);
      assertEquals(myItem.name, "Dog Food");
      //assertEquals(myItem.price, 1.45);

  }


  public void testGetJava_PurchaseOrder() throws SoajException {

      MyPurchaseOrder po = new MyPurchaseOrder();
      Address billTo = po.getBillTo();
      billTo.setCity("San Antonio");
      billTo.setPhoneNumber(new Phone(512, "443", "7866"));
      billTo.setState(StateType.TX);
      billTo.setStreetName("Plaza de Embarcadaro");
      billTo.setStreetNum(7983);
      billTo.setZip(64883);
      String[] names = new String[]{"Potato Chips", "Pretzels", "Diet Coke"};
      int[] amounts = new int[]{2, 2, 12};
      double[] prices = new double[]{2.19, 1.09, 6.49};
      for (int i = 0; i < names.length; i++) {
          po.addItem(new MyItem(names[i], amounts[i], prices[i]));
      }


      QName QN_PurchaseOrder = new QName(NS_test, "PurchaseOrder");
      XMLMapping tm = new XMLMapping(QN_PurchaseOrder, QN_PurchaseOrder,MyPurchaseOrder.class.getName());
      Object xobj = adaptiveContext.serialize(po, tm);
      Object obj = adaptiveContext.deserialize(xobj, tm);
      assertEquals(obj.getClass(), MyPurchaseOrder.class);
      System.out.println("getItems.getClass() = "+ ((MyPurchaseOrder) obj).getItems().getClass());
      assertEquals(((MyPurchaseOrder) obj).getItems().size(), names.length);
      for (int i = 0; i < names.length; i++) {
          MyItem myItem = (MyItem) ((MyPurchaseOrder) obj).getItems().get(i);
          assertEquals(myItem.getAmount(), amounts[i]);
          assertEquals(myItem.getName(), names[i]);
          assertEquals("" + myItem.getPrice(), "" + prices[i]);
      }
      assertEquals(((MyPurchaseOrder) obj).getBillTo().getClass(), Address.class);
      assertEquals(((MyPurchaseOrder) obj).getBillTo().getCity(), billTo.getCity());
      assertEquals(((MyPurchaseOrder) obj).getBillTo().getState().getValue(), billTo.getState().getValue());
      assertEquals(((MyPurchaseOrder) obj).getBillTo().getZip(), billTo.getZip());
        assertEquals(((MyPurchaseOrder) obj).getBillTo().getStreetName(), billTo.getStreetName());
        assertEquals(((MyPurchaseOrder) obj).getBillTo().getStreetNum(), billTo.getStreetNum());
        assertTrue(((MyPurchaseOrder) obj).getBillTo().getPhoneNumber().equals( billTo.getPhoneNumber()));


  }



  public void testGetJava_BillTo() throws SoajException {
      QName qName = new QName("http://javector.com/ser/adaptive/po", "billToType");
      XMLMapping tm = new XMLMapping(qName,qName, Address.class.getName());
      Phone phone = new Phone(973, "243", "8776");
      StateType state = StateType.OH;
      Address addr = new Address
              (125, "Main Street", "Canton", state, 98134, phone);
      Object jaxbObject = adaptiveContext.serialize(addr, tm);
      Object obj = adaptiveContext.deserialize(jaxbObject, tm);
      System.out.println("javaObject = " + obj.getClass());
      assertEquals(obj.getClass(), Address.class);
      assertEquals(((Address) obj).getCity(), addr.getCity());
      assertEquals(((Address) obj).getState().getValue(), addr.getState().getValue());
      assertEquals(((Address) obj).getZip(), addr.getZip());
      assertEquals(((Address) obj).getPhoneNumber().getAreaCode(),addr.getPhoneNumber().getAreaCode());
      assertEquals(((Address) obj).getPhoneNumber().getExchange(),addr.getPhoneNumber().getExchange());
      assertEquals(((Address) obj).getPhoneNumber().getNumber(),addr.getPhoneNumber().getNumber());
      assertEquals(((Address) obj).getStreetName(),addr.getStreetName());
      assertEquals(((Address) obj).getStreetNum(),addr.getStreetNum());
  }




  public void testParsePurchaseOrder() throws Exception {
      MyPurchaseOrder po = new MyPurchaseOrder();
         Address billTo = po.getBillTo();
         billTo.setCity("San Antonio");
         billTo.setPhoneNumber(new Phone(512, "443", "7866"));
         billTo.setState(StateType.TX);
         billTo.setStreetName("Plaza de Embarcadaro");
         billTo.setStreetNum(7983);
         billTo.setZip(64883);
         String[] names = new String[]{"Potato Chips", "Pretzels", "Diet Coke"};
         int[] amounts = new int[]{2, 2, 12};
         double[] prices = new double[]{2.19, 1.09, 6.49};
         for (int i = 0; i < names.length; i++) {
             po.addItem(new MyItem(names[i], amounts[i], prices[i]));
         }
         QName QN_PurchaseOrder = new QName(NS_test, "PurchaseOrder");
         XMLMapping tm = new XMLMapping(QN_PurchaseOrder, QN_PurchaseOrder,MyPurchaseOrder.class.getName());
         Object xobj = adaptiveContext.serialize(po, tm);
         Object obj = adaptiveContext.deserialize(xobj, tm);
         assertEquals(obj.getClass(), MyPurchaseOrder.class);
         assertEquals(((MyPurchaseOrder) obj).getItems().size(), names.length);
         for (int i = 0; i < names.length; i++) {
             MyItem myItem = (MyItem) ((MyPurchaseOrder) obj).getItems().get(i);
             assertEquals(myItem.getAmount(), amounts[i]);
             assertEquals(myItem.getName(), names[i]);
             assertEquals("" + myItem.getPrice(), "" + prices[i]);
         }
         assertEquals(((MyPurchaseOrder) obj).getBillTo().getClass(), Address.class);
         assertEquals(((MyPurchaseOrder) obj).getBillTo().getCity(), billTo.getCity());
         assertEquals(((MyPurchaseOrder) obj).getBillTo().getState().getValue(), billTo.getState().getValue());
         assertEquals(((MyPurchaseOrder) obj).getBillTo().getZip(), billTo.getZip());
         assertEquals(((MyPurchaseOrder) obj).getBillTo().getStreetName(), billTo.getStreetName());
         assertEquals(((MyPurchaseOrder) obj).getBillTo().getStreetNum(), billTo.getStreetNum());
         assertTrue(((MyPurchaseOrder) obj).getBillTo().getPhoneNumber().equals( billTo.getPhoneNumber()));
  }
}
