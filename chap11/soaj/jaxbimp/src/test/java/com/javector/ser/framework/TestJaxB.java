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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by IntelliJ IDEA.
 * User: Kishore G
 * Date: Jan 20, 2006
 * Time: 3:52:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestJaxB extends TestCase {
//
    public static Test suite() {
        return new TestSuite(TestJaxB.class);
    }


    public void testDummy(){
        assertTrue(true);
    }
//
//    public static final String NS_test = "http://javector.com/ser/adaptive/po";
//      String xmlFile = "com/javector/ser/framework/SOAAdaptiveSample.xml";
//      String handlerMappingFile = "config/SOAJaxbHandlerMapping.xml";
//      //static URL[] url = new URL[]{Thread.currentThread().getContextClassLoader().getResource("config/SOA-config1.xsd")};
//      static URL[] url = new URL[]{Thread.currentThread().getContextClassLoader().getResource("com/javector/ser/adaptive/po/TestAdaptiveSerializerPO.xsd")};
//      AdaptiveContext adaptiveContext = null;
//      Configurator configurator;
//
//      static {
//          System.out.println("url = " + url);
//      }
//
//      protected void setUp() throws Exception {
//          configurator = ConfiguratorFactory.createConfigurator(handlerMappingFile);
//          adaptiveContext = AdaptiveContextFactory.createNewContext(configurator, xmlFile, url);
//      }
//
//
//    public void testXJCTask() throws Exception {
//        XmlTypeMapping typeMapping = new XmlTypeMapping( new QName("http://javector.com/ser/adaptive/po", "item"), MyItem.class.getName());
//        Object jaxbObject = adaptiveContext.serialize(new MyItem("Dog Food", 100, 1.45), typeMapping);
//        Object javaObject = adaptiveContext.deserialize(jaxbObject , typeMapping);
//        System.out.println("javaObject = " + javaObject.getClass());
//        assertEquals(javaObject.getClass(),MyItem.class);
//        MyItem myItem = (MyItem) javaObject;
//
//        assertEquals(myItem.quantity,100);
//        assertEquals(myItem.name,"Dog Food");
//        assertEquals(myItem.price,1.45);
//
//    }
//
//
//
//    public void testGetXml_PurchaseOrder() throws SOAException {
//
//        XmlTypeMapping tm = new XmlTypeMapping( new QName("http://javector.com/ser/adaptive/po", "PurchaseOrder"), MyPurchaseOrder.class.getName());
//        MyPurchaseOrder po = new MyPurchaseOrder();
//        MyItem []items = new MyItem[2];
//        items[0] = new MyItem("A", 100, 1.45);
//        items[1] = new MyItem("B", 200, 2.45);
//        po.setItems(items);
//        Phone phone = new Phone(973, "243", "8776");
//        StateType state = StateType.OH;
//        Address addr = new Address
//                (125, "Main Street", "Canton", state, 98134, phone);
//        po.setBillTo(addr);
//        Object jaxbObject = adaptiveContext.serialize(po, tm);
//    }
//    public void testGetJava_PurchaseOrder() throws SOAException {
//
//        XmlTypeMapping tm = new XmlTypeMapping( new QName("http://javector.com/ser/adaptive/po", "PurchaseOrder"), MyPurchaseOrder.class.getName());
//        MyPurchaseOrder po = new MyPurchaseOrder();
//        MyItem []items = new MyItem[2];
//        items[0] = new MyItem("A", 100, 1.45);
//        items[1] = new MyItem("B", 200, 2.45);
//        po.setItems(items);
//        Phone phone = new Phone(973, "243", "8776");
//        StateType state = StateType.OH;
//        Address addr = new Address
//                (125, "Main Street", "Canton", state, 98134, phone);
//        po.setBillTo(addr);
//        Object jaxbObject = adaptiveContext.serialize(po, tm);
//        Object javaObject = adaptiveContext.deserialize(jaxbObject , tm);
//        System.out.println("javaObject = " + javaObject.getClass());
//    }
//    public void testGetJava_BillTo() throws SOAException {
//
//        XmlTypeMapping tm = new XmlTypeMapping( new QName("http://javector.com/ser/adaptive/po", "billToType"), Address.class.getName());
//        Phone phone = new Phone(973, "243", "8776");
//        StateType state = StateType.OH;
//        Address addr = new Address
//                (125, "Main Street", "Canton", state, 98134, phone);
//        Object jaxbObject = adaptiveContext.serialize(addr, tm);
//        Object javaObject = adaptiveContext.deserialize(jaxbObject , tm);
//        System.out.println("javaObject = " + javaObject.getClass());
//    }
//

}
