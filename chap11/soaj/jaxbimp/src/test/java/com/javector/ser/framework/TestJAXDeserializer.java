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

import java.net.URL;

import com.javector.adaptive.framework.interfaces.AdaptiveContext;
import com.javector.adaptive.framework.Configurator;
import com.javector.adaptive.framework.ConfiguratorFactory;
import com.javector.adaptive.framework.AdaptiveContextFactory;

/**
 * Created by IntelliJ IDEA.
 * Auther: Rohit Agarwal
 * Date: May 25, 2006
 * Time: 8:44:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestJAXDeserializer extends TestCase {

  public static Test suite() {
    return new TestSuite(TestJAXDeserializer.class);
  }
  
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

  public void testPhone() throws Exception  {
      
  }

}
