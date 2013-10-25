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

import junit.framework.TestCase;
import junit.framework.Assert;

import java.net.URL;
import java.net.URI;
import java.io.File;
import java.io.InputStream;
import java.util.Date;

import com.javector.adaptive.framework.interfaces.AdaptiveContext;
import com.javector.adaptive.framework.interfaces.XMLMapping;
import com.javector.adaptive.framework.Configurator;
import com.javector.adaptive.framework.ConfiguratorFactory;
import com.javector.adaptive.framework.AdaptiveContextFactory;
import com.javector.adaptive.framework.util.SOAUtil;
import com.javector.ser.adaptive.po.*;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.namespace.QName;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Created by IntelliJ IDEA.
 * Auther: Rohit Agarwal
 * Date: May 28, 2006
 * Time: 3:00:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestSerializerSource extends TestCase {

    public static final String NS_test = "http://javector.com/ser/adaptive/po";
    String xmlFile = "com/javector/ser/framework/SOAAdaptiveNew.xml";
    String handlerMappingFile = "config/SOAJaxbHandlerMapping.xml";
    //static URL[] url = new URL[]{Thread.currentThread().getContextClassLoader().getResource("config/SOA-config1.xsd")};
    static URL[] url = new URL[]{Thread.currentThread().getContextClassLoader().getResource("com/javector/ser/adaptive/po/TestAdaptiveSerializerPO.xsd")};
    AdaptiveContext adaptiveContext = null;
    Configurator configurator;
    String xmlSourceFilePath = "com/javector/ser/data/purchaseOrder.xml";
    Source source;

    static {
        System.out.println("url = " + url);
    }

    protected void setUp() throws Exception {
        configurator = ConfiguratorFactory.createConfigurator(handlerMappingFile);
        adaptiveContext = AdaptiveContextFactory.createNewContext(configurator, xmlFile, url);
        createSource();
    }

    private void createSource() throws Exception {
        URI uri = Thread.currentThread().getContextClassLoader().getResource(xmlSourceFilePath).toURI();
        File file = new File(uri);
        byte[] data = new byte[new Long(file.length()).intValue()];
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(xmlSourceFilePath);
        inputStream.read(data);
        Document document = SOAUtil.convertStringToDocument(new String(data));
        source = new DOMSource(document);
    }

    public void testPurchaseOrderDeserialize() throws Exception {
        QName QN_PurchaseOrder = new QName(NS_test, "PurchaseOrder");
        XMLMapping tm = new XMLMapping(QN_PurchaseOrder,QN_PurchaseOrder, MyOwnPurchaseOrder.class.getName());
        adaptiveContext.deserialize(source,tm);
    }


    public void testSerializeForAddress() throws Exception {
          Address address = new Address();
          address.setCity("agra");
          address.setState(StateType.OH);
          address.setStreetName("MG road");
          address.setStreetNum(788);
          address.setPhoneNumber(new Phone(23,"01","56656"));
          QName billToType =  new QName("http://javector.com/ser/adaptive/po", "billTo");
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


    public void testPurchaseOrderSerialize() throws Exception {
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
            QName QN_PurchaseOrder = new QName(NS_test, "PurchaseOrder");
            XMLMapping tm = new XMLMapping(QN_PurchaseOrder,QN_PurchaseOrder, MyOwnPurchaseOrder.class.getName());
        Source source = adaptiveContext.serializeAsSource(po, tm);
        Node xmlNode = SOAUtil.getXMLNodeFromSource(source);
        Assert.assertEquals(xmlNode.getFirstChild().getNodeName(),"PurchaseOrder");
    }


}
