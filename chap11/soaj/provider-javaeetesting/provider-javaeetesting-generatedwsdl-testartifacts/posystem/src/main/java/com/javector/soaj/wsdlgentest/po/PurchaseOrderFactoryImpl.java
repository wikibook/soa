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
package com.javector.soaj.wsdlgentest.po;

import com.javector.adaptive.framework.AdaptiveContextFactory;
import com.javector.adaptive.framework.Configurator;
import com.javector.adaptive.framework.ConfiguratorFactory;
import com.javector.adaptive.framework.interfaces.AdaptiveContext;
import com.javector.adaptive.framework.interfaces.XMLMapping;
import com.javector.soaj.util.ResourceFinder;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.net.URL;
import java.util.Date;
import java.util.List;


public class PurchaseOrderFactoryImpl {

  private static String XML = "podatabase.xml";
  private static String XSD = "purchaseOrder.xsd";
  private static String ADAPTIVE_CONFIG = "adaptiveconfig.xml";
  private static String ADAPTIVE_MAP = "adaptivemap.xml";
  private static String NS = "http://javector.com/ser/adaptive/po";
  
  public static PurchaseOrder newPurchaseOrder(List<Item> items, Address billingAddr) {
    
    System.out.println(items.get(0).getClass());
    PurchaseOrder po = new PurchaseOrder();
    for (Item item : items) {
      po.addItem(item);
    }
    po.setBillTo(billingAddr);
    po.setPonum(Long.toString(new Date().getTime()));
    return po;
    
  }
  
  public static PODatabase loadPODatabase() {
  
    URL[] xsdURL = new URL[]{Thread.currentThread().getContextClassLoader().getResource(XSD)};
    if ( xsdURL == null || xsdURL[0] == null ) {
      throw new RuntimeException("Failed to get resource: " + XSD);
    }
    Configurator configurator = ConfiguratorFactory.createConfigurator(ADAPTIVE_CONFIG);
    AdaptiveContext adaptiveContext = AdaptiveContextFactory.createNewContext(configurator, ADAPTIVE_MAP, xsdURL);
    QName posQName = new QName(NS, "purchaseOrderList");
    XMLMapping tm = new XMLMapping(posQName, posQName, PODatabase.class.getName());
    Source xmlSource = new StreamSource(ResourceFinder.getResourceAsStream(XML, null));
    return (PODatabase) adaptiveContext.deserialize(xmlSource, tm);

  }
  
}
