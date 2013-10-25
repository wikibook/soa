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
package com.javector.soaj.util;

import java.io.InputStream;
import java.io.StringReader;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;

public class TestXmlUtil extends TestCase {

  private static SoajLogger logger = LoggingFactory.getLogger(TestXmlUtil.class.getName());

  public static Test suite() {
    return new TestSuite(TestXmlUtil.class);
}

protected void setUp() throws Exception {}


   public void testSourceToString() throws Exception {
     
     String xml = "<foo>bar</foo>";
     StreamSource source = new StreamSource(new StringReader(xml));
     String result = XmlUtil.toString(source);
     assertTrue(result.indexOf(xml) > -1);
     // check that you can still read source
     result = XmlUtil.toString(source);
     assertTrue(result.indexOf(xml) > -1);
     
    
   }
   
   public void testDomToString() throws Exception {
     
     String xml = "<foo>bar</foo>";
     StreamSource source = new StreamSource(new StringReader(xml));
     TransformerFactory transFac = TransformerFactory.newInstance();
     Transformer xformer = transFac.newTransformer();
     DOMResult domResult = new DOMResult();
     xformer.transform(source, domResult);
     Node n = domResult.getNode();
     String result = XmlUtil.toString(n, false);
     assertTrue(result.indexOf(xml) > -1);
     result = XmlUtil.toString(n, true);
     assertTrue(result.indexOf(xml) > -1);
     result = XmlUtil.toString(n);
     assertTrue(result.indexOf(xml) > -1);
     result = XmlUtil.toFormattedString(n);
     assertTrue(result.indexOf(xml) > -1);
     
   }
   
   public void testGetSchemaWSDL11SOAP11() throws Exception {
     
     Schema schema = XmlUtil.getSchemaWSDL11SOAP11();
     assertNotNull(schema);
     InputStream validWSDL = ResourceFinder.getResourceAsStream("AWSECommerceService_20060628.wsdl");
     assertNotNull(validWSDL);
     InputStream invalidWSDL = ResourceFinder.getResourceAsStream("AWSECommerceService_20060628_invalid.wsdl.txt");
     assertNotNull(invalidWSDL);
     Validator validator = schema.newValidator();
     SoajSAXErrorHandler errorHandler = new SoajSAXErrorHandler();
     validator.setErrorHandler(errorHandler);
     validator.validate(new SAXSource(new InputSource(validWSDL)));
     logger.info("Errors from valid WSDL"+IOUtil.NL+errorHandler.toString());
     assertFalse("Valid WSDL shows errors.", errorHandler.hasErrors() || errorHandler.hasFatalErrors());
     errorHandler = new SoajSAXErrorHandler();
     validator.setErrorHandler(errorHandler);
     validator.validate(new SAXSource(new InputSource(invalidWSDL)));
     logger.info("Errors from invalid WSDL"+IOUtil.NL+errorHandler.toString());
     assertTrue("Invalid WSDL shows errors.", errorHandler.hasErrors() || errorHandler.hasFatalErrors());
     
   }
   
   public void testFormatXml() throws Exception {
     
     String xml = "<foo><bar>xyz<car>corvair</car></bar>"+
     "<tree><size tall=\"true\"/><leaves>yes</leaves>  </tree></foo>";
     String fXml = XmlUtil.formatXml(xml);
     logger.info("Formatted XML:" + IOUtil.NL + fXml);
     assertTrue("Formatted string doesn't end in </foo>", fXml.endsWith("</foo>"));
     
   }

}
