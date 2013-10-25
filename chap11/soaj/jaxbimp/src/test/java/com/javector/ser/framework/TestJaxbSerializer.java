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

import com.javector.adaptive.framework.interfaces.AdaptiveContext;
import com.javector.adaptive.framework.interfaces.XMLMapping;
import com.javector.adaptive.framework.Configurator;
import com.javector.adaptive.framework.ConfiguratorFactory;
import com.javector.adaptive.framework.AdaptiveContextFactory;
import com.javector.adaptive.jaxb.impl.AdaptiveJaxbContext;
import com.javector.ser.adaptive.po.*;
import com.javector.soaj.SoajException;

import javax.xml.namespace.QName;
import java.net.URL;
import java.util.Date;

import junit.framework.TestCase;
import junit.framework.Assert;

/**
 * Created by IntelliJ IDEA.
 * Auther: Rohit Agarwal
 * Date: May 6, 2006
 * Time: 4:09:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestJaxbSerializer extends TestCase {


    public static final String NS_test = "http://javector.com/ser/adaptive/po";
    String xmlFile = "com/javector/ser/framework/SOAAdaptiveNew.xml";
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


    public void testVendor() throws Exception {
        VendorTest vendorTest = createVendorObject();
        XMLMapping mapping = createMappingForVendor();
        Object vendorObj = adaptiveContext.serialize(vendorTest, mapping);
        VendorTest serVendorTest = (VendorTest)adaptiveContext.deserialize(vendorObj, mapping);
        Assert.assertEquals(vendorTest.getName(),serVendorTest.getName());
        Assert.assertEquals(vendorTest.getCode(),serVendorTest.getCode());
    }


     public void testItemCutomiseSerialize() throws SoajException {
      XMLMapping tm = new XMLMapping(new QName("http://javector.com/ser/adaptive/po","item"),new QName("http://javector.com/ser/adaptive/po","item"),MyOwnItem.class.getName());
      MyOwnItem  item = new MyOwnItem("rice", new Date(),90);
      Object jaxbObject = adaptiveContext.serialize(item, tm);
      adaptiveContext.deserialize(jaxbObject,tm);
          System.out.println("jaxbObject =  " + jaxbObject);
  }



    private XMLMapping createMappingForVendor() {
        QName name = new QName(NS_test,"vendor");
        XMLMapping mapping = new XMLMapping(name,name, VendorTest.class.getName());
        return mapping;
    }

    private VendorTest createVendorObject() {
        VendorTest vendorTest = new VendorTest();
        vendorTest.setName("IBM");
        vendorTest.setCode(78);
        return vendorTest;
    }


    public void testGetJava_PurchaseOrderSerializer() throws SoajException {
          MyOwnPurchaseOrder po = new MyOwnPurchaseOrder();
          String[] names = new String[]{"Potato Chips", "Pretzels", "Diet Coke"};
          Date[] amounts = new Date[]{new Date(), new Date(), new Date()};
          double[] prices = new double[]{2.19, 1.09, 6.49};
          for (int i = 0; i < names.length; i++) {
              po.addItem(new MyOwnItem(names[i], amounts[i], prices[i]));
          }
          QName QN_PurchaseOrder = new QName(NS_test, "PurchaseOrder");
          XMLMapping tm = new XMLMapping(QN_PurchaseOrder, QN_PurchaseOrder,MyOwnPurchaseOrder.class.getName());
          Object xobj = adaptiveContext.serialize(po, tm);
        System.out.println("jaxb Object = " + xobj);
    }

    public void testSerializeForAddress() throws Exception {
        Address address = new Address();
        address.setCity("agra");
        address.setState(StateType.OH);
        address.setStreetName("MG road");
        address.setStreetNum(788);
        address.setPhoneNumber(new Phone(23,"01","56656"));
        QName billToType =  new QName("http://javector.com/ser/adaptive/po", "billToType");
        XMLMapping tm = new XMLMapping(billToType, billToType, Address.class.getName());
        Object xObj = adaptiveContext.serialize(address,tm);
        System.out.println("jaxb Object = " + xObj);
        Object javaObject = adaptiveContext.deserialize(xObj,tm);
        Address addressDeserialized  = (Address)javaObject;
        Assert.assertEquals(address.getCity(),addressDeserialized.getCity());
        Assert.assertEquals(address.getState().getValue(),addressDeserialized.getState().getValue());
        Assert.assertEquals(address.getStreetNum(),addressDeserialized.getStreetNum());
        Assert.assertEquals(address.getZip(),addressDeserialized.getZip());

    }

    public void testGetJava_PurchaseOrder() throws Exception {
         MyOwnPurchaseOrder po = new MyOwnPurchaseOrder();
         Address billTo = po.getBillTo();
            billTo.setCity("San Antonio");
         billTo.setPhoneNumber(new Phone(512, "443", "7866"));
         billTo.setState(StateType.TX);
         billTo.setStreetName("Plaza de Embarcadaro");
         billTo.setStreetNum(7983);
         billTo.setZip(64883);
         String[] names = new String[]{"Potato Chips", "Pretzels", "Diet Coke"};
          Date[] amounts = new Date[]{new Date(), new Date(), new Date()};
          double[] prices = new double[]{2.19, 1.09, 6.49};
          for (int i = 0; i < names.length; i++) {
              po.addItem(new MyOwnItem(names[i], amounts[i], prices[i]));
          }
        VendorTest vendorTestObject = createVendorObject();
        po.setVendor(vendorTestObject);
        QName QN_PurchaseOrder = new QName(NS_test, "PurchaseOrder");
         XMLMapping tm = new XMLMapping(QN_PurchaseOrder,QN_PurchaseOrder, MyOwnPurchaseOrder.class.getName());
         Object xobj = adaptiveContext.serialize(po, tm);
         Object derializedJavaObject = adaptiveContext.deserialize(xobj,tm);
         System.out.println("jaxb Object = " + xobj);
         }
}
