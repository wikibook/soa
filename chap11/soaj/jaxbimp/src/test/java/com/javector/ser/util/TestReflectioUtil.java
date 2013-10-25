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
package com.javector.ser.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.Assert;
import junit.framework.TestSuite;

import com.javector.adaptive.framework.interfaces.AdaptiveContext;
import com.javector.adaptive.framework.Configurator;
import com.javector.adaptive.framework.ConfiguratorFactory;
import com.javector.adaptive.framework.AdaptiveContextFactory;
import com.javector.adaptive.jaxb.util.JAXBReflectionUtil;
import com.javector.soaj.util.IOUtil;

import javax.xml.namespace.QName;
import java.net.URL;
import java.net.URLClassLoader;
import java.io.File;

import org.jvnet.jaxb.reflection.JAXBModelFactory;
import org.jvnet.jaxb.reflection.model.runtime.RuntimeTypeInfoSet;

/**
 * Created by IntelliJ IDEA.
 * Auther: Rohit Agarwal
 * Date: May 26, 2006
 * Time: 7:28:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestReflectioUtil extends TestCase {

  public static Test suite() {
    return new TestSuite(TestReflectioUtil.class);
  }

    public static final String NS_test = "http://javector.com/ser/adaptive/po";
    String xmlFile = "com/javector/ser/framework/SOAAdaptiveNew.xml";
    String handlerMappingFile = "config/SOAJaxbHandlerMapping.xml";
    //static URL[] url = new URL[]{Thread.currentThread().getContextClassLoader().getResource("config/SOA-config1.xsd")};
    static URL[] url = new URL[]{Thread.currentThread().getContextClassLoader().getResource("com/javector/ser/adaptive/po/TestAdaptiveSerializerPO.xsd")};
    AdaptiveContext adaptiveContext = null;
    Configurator configurator;
    URLClassLoader cl;


    protected void setUp() throws Exception {
        configurator = ConfiguratorFactory.createConfigurator(handlerMappingFile);
        adaptiveContext = AdaptiveContextFactory.createNewContext(configurator, xmlFile, url);
        File workDir  = new File(System.getProperty("user.home")+"/soabook-xmlbeans/classes0");
        URL url = workDir.toURL();
//        cl = new URLClassLoader(new URL[]{url});
        cl = new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
    }

    /**
     * Checking creation of the XML Object from generated jaxb classes
     * @throws Exception
     */

    public void testCreateXMLObject() throws Exception {
        JAXBReflectionUtil util = new JAXBReflectionUtil();
        QName name = new QName(NS_test,"BillToType");
        Object xmlObject = util.createXMLObject(name, cl);
        Assert.assertTrue(xmlObject!=null);
    }

    public void testJAXBReflection() throws Exception {
        JAXBReflectionUtil util = new JAXBReflectionUtil();
        QName name = new QName(NS_test,"BillToType");
        Object xmlObject = util.createXMLObject(name, cl);
          RuntimeTypeInfoSet infoSet = JAXBModelFactory.create(xmlObject.getClass());
          infoSet.getAnyTypeInfo();

    }
}
