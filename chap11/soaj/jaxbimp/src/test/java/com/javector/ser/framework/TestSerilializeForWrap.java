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
import java.util.List;
import java.util.ArrayList;

import com.javector.adaptive.framework.interfaces.AdaptiveContext;
import com.javector.adaptive.framework.interfaces.XMLMapping;
import com.javector.adaptive.framework.Configurator;
import com.javector.adaptive.framework.ConfiguratorFactory;
import com.javector.adaptive.framework.AdaptiveContextFactory;
import com.javector.ser.adaptive.powrap.*;

import javax.xml.namespace.QName;

/**
 * Created by IntelliJ IDEA.
 * Auther: Rohit Agarwal
 * Date: Jun 6, 2006
 * Time: 8:00:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestSerilializeForWrap extends TestCase {

    public static final String NS_test = "http://javector.com/ser/adaptive/po";
    String xmlFile = "com/javector/ser/framework/SOAAdaptiveWrap.xml";
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

    public void testWrapAddress() throws Exception {
        WrapAddress address = createWrapAddress();
        QName name = new QName(NS_test,"billToType");
        XMLMapping mapping = new XMLMapping(name,name,WrapAddress.class.getName());
        Object o = adaptiveContext.serialize(address, mapping);
        WrapAddress serJavaObject = (WrapAddress)adaptiveContext.deserialize(o, mapping);
        Assert.assertEquals(serJavaObject.getCity(),address.getCity());
        Assert.assertEquals(serJavaObject.getZip(),address.getZip());
        Assert.assertEquals(serJavaObject.getState().getValue(),address.getState().getValue());
        Assert.assertEquals(serJavaObject.getStreetName(),address.getStreetName());
    }

    private WrapAddress createWrapAddress() {
        WrapAddress address =  new WrapAddress();
        address.setCity("Agra");
        address.setState(WrapState.IN);
        address.setStreetName("M.G. Road");
        address.setZip(5657);
        return address;
    }


    public void testWrapItem() throws Exception {
        WrapItem item = createWrapItem();
        QName name = new QName(NS_test,"item");
        XMLMapping mapping = new XMLMapping(name,name,WrapItem.class.getName());
        Object o = adaptiveContext.serialize(item, mapping);
        WrapItem serItem = (WrapItem)adaptiveContext.deserialize(o, mapping);
        Assert.assertEquals(item.getAmount(), serItem.getAmount());
        Assert.assertEquals(item.getPrice(),serItem.getPrice());
        Assert.assertEquals(item.getName(),serItem.getName());
    }

    private WrapItem createWrapItem() {
        WrapItem item = new WrapItem();
        item.setAmount(89);
        item.setName("shoes");
        item.setPrice(90);
        return item;
    }

    public void testWrapPurchaseOrder() throws Exception {
        WrapPurchaseOrder order = new WrapPurchaseOrder();
        order.setAddress(createWrapAddress());
        WrapItem wrapItem = createWrapItem();
        WrapBagItem bagItem = new WrapBagItem();
        List itemList = new ArrayList(3);
        for (int i=0;i<3;i++ ) {
            itemList.add(wrapItem);
        }
        bagItem.setItems(itemList);
        order.setBagItem(bagItem);
        QName name = new QName(NS_test,"PurchaseOrder");
        XMLMapping mapping = new XMLMapping(name,name,WrapPurchaseOrder.class.getName());
        Object o = adaptiveContext.serialize(order, mapping);
        WrapPurchaseOrder serPurchaseOrder = (WrapPurchaseOrder)adaptiveContext.deserialize(o, mapping);
        System.out.println("data");

    }
}
