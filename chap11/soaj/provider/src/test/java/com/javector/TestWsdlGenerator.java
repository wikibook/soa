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
package com.javector;

import junit.framework.TestCase;
import com.javector.adaptive.util.dto.SOAConfigDTO;
import com.javector.adaptive.util.dto.WsdlDTO;
import com.javector.adaptive.util.SOAConfigReader;
import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;
import com.javector.soaj.provider.SoajInstanceConfiguration;
import com.javector.soaj.util.IOUtil;
import com.javector.soaj.util.ResourceFinder;
import com.javector.soaj.util.WsdlUtil;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

import javax.wsdl.Definition;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class TestWsdlGenerator extends TestCase {
  
  private static SoajLogger logger = LoggingFactory.getLogger(TestWsdlGenerator.class.getName());
  
  private static String VOID_RETURN_TYPE_WSDL = "config/voidReturnType.wsdl";
  private static String VOID_RETURN_TYPE_CONFIG = "config/voidReturnTypeConfig.xml";
  private static String ELEMENT_NAME_AND_TYPE_WSDL = "config/elementNameAndType.wsdl";
  private static String ELEMENT_NAME_AND_TYPE_CONFIG = "config/elementNameAndTypeConfig.xml";
  
  public TestWsdlGenerator(String s){
    super(s);
  }
  
  // gets the config from the standard location (config/SoajConfig.xml) using
  // SoajInstanceConfiguration, generated a WSDL from it and check that the
  // targetNamespace is correct.
  public void testGenerateWsdl_checkSoajInstanceConfiguration() throws Exception {
    
    WsdlGenerator wsdlGenerator = new WsdlGenerator();
    InputStream resourceAsStream = SoajInstanceConfiguration.getSoajConfigAsStream();
    SOAConfigReader soaConfigReader = new SOAConfigReader();
    SOAConfigDTO soaConfigDTO = soaConfigReader.parserSOAConfig(resourceAsStream);
    String requestedWsdl = wsdlGenerator.generateWsdl("/provider-dummy/test", soaConfigDTO);
    String string1= requestedWsdl.split("targetNamespace=")[1];
    String  targetNamespace = string1.split("\"")[1];
    assertEquals(targetNamespace,"http://www.javector.com");
    
  }
  
  // generate a WSDL from config/SoajConfig1.xml and check that its target namespace is correct
  public void testGenerateWsdl_simpleTargetNamespaceCheck() throws Exception {
    
    WsdlGenerator wsdlGenerator = new WsdlGenerator();
    InputStream resourceAsStream = ResourceFinder.getResourceAsStream("config/SoajConfig1.xml");
    SOAConfigReader soaConfigReader = new SOAConfigReader();
    SOAConfigDTO soaConfigDTO = soaConfigReader.parserSOAConfig(resourceAsStream);
    String requestedWsdl = wsdlGenerator.generateWsdl("/provider-staticwsdl/test", soaConfigDTO);
    String string1= requestedWsdl.split("targetNamespace=")[1];
    String  targetNamespace = string1.split("\"")[1];
    assertEquals(targetNamespace,"http://javector.com/soaj/provider");
    
  }
  
  public void testWsdlGenerator_VoidReturnType() throws Exception {
    validateAndCompareWsdls(VOID_RETURN_TYPE_WSDL, VOID_RETURN_TYPE_CONFIG);
  }
  
  public void testWsdlGenerator_ElementNameAndType() throws Exception {
    validateAndCompareWsdls(ELEMENT_NAME_AND_TYPE_WSDL, ELEMENT_NAME_AND_TYPE_CONFIG);
  }
  
  private void validateAndCompareWsdls(String staticWsdlPath, String configPath) throws Exception {
    
    // validate the static wsdl that we compare with
    String errorReport1 = WsdlUtil.validateWSDL(ResourceFinder.getResourceUrl(staticWsdlPath).toString(),
        ResourceFinder.getResourceAsStream(staticWsdlPath));
    assertNull(errorReport1, errorReport1);
    // generate the wsdl that should match the static
    InputStream voidReturnTypeConfig = ResourceFinder.getResourceAsStream(configPath);
    SOAConfigDTO configDTO = (new SOAConfigReader()).parserSOAConfig(voidReturnTypeConfig);
    ArrayList<WsdlDTO> wsdlDTOs = configDTO.getWsdlDTOs();
    WsdlDTO wsdlDTO = wsdlDTOs.get(0);
    String wsdlString = (new WsdlGenerator()).generateWsdlForWsdlDTO(wsdlDTO, configDTO);
    logger.info("Generated WSDL:"+IOUtil.NL+wsdlString);
    assertNotNull(wsdlString);
    // validate the generated wsdl
    InputStream genWsdlInputStream = new ByteArrayInputStream(wsdlString.getBytes());
    // use static wsdl location for url.  it will resolve relative schema paths from classpath correctly
    String genWsdlUrl = ResourceFinder.getResourceUrl(staticWsdlPath).toString();
    String errorReport2 = WsdlUtil.validateWSDL(genWsdlUrl, genWsdlInputStream);
    assertNull(errorReport2, errorReport2);
    // get DOMS needed to use wsdl4j
    DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
    fac.setNamespaceAware(true);
    DocumentBuilder domBuilder = fac.newDocumentBuilder();
    Document staticWsdlDoc;
    Document generatedWsdlDoc;
    staticWsdlDoc = domBuilder.parse(ResourceFinder.getResourceAsStream(staticWsdlPath));
    generatedWsdlDoc = domBuilder.parse(new ByteArrayInputStream(wsdlString.getBytes()));
    // Now, use WSDL4J to compare WSDLs
    URL staticWsdlUrl = ResourceFinder.getResourceUrl(staticWsdlPath);
    WSDLFactory wsdlFac = WSDLFactory.newInstance();
    WSDLReader wsdlReader = wsdlFac.newWSDLReader();
    Definition staticWsdl = wsdlReader.readWSDL(staticWsdlUrl.toExternalForm(), staticWsdlDoc);
    Definition generatedWsdl = wsdlReader.readWSDL(staticWsdlUrl.toExternalForm(), generatedWsdlDoc);
    String wsdlCompareReport = WsdlUtil.compareWsdl(staticWsdl,generatedWsdl);
    assertNull(wsdlCompareReport, wsdlCompareReport);
    
  }
  
}
