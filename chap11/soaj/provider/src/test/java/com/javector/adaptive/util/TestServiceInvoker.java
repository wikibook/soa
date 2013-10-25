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
package com.javector.adaptive.util;

import junit.framework.TestCase;

import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

import com.javector.adaptive.util.dto.SOAConfigDTO;
import com.javector.adaptive.util.dto.SOAJOperationDTO;
import com.javector.adaptive.ws.processor.InputDataProcessor;
import com.javector.soaj.SoajException;
import com.javector.soaj.config.UserDefinedSchema;

import javax.xml.transform.Source;

/**
 * Created by IntelliJ IDEA.
 * User: javector
 * Date: May 26, 2006
 * Time: 7:04:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestServiceInvoker extends TestCase {

    public TestServiceInvoker(String s){
        super(s);
    }


   public void testInvokeSOAService(){
       SOAConfigReader soaConfigReader = new SOAConfigReader();
       InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/SoajConfig.xml");
       SOAConfigDTO soaConfigDTO = soaConfigReader.parserSOAConfig(resourceAsStream);
        List<UserDefinedSchema> userDefinedSchemas = soaConfigDTO.getUserDefinedSchemas();
    ArrayList<String> userDefnSchemaPaths = new ArrayList<String>();
    for (UserDefinedSchema userDefinedSchema : userDefinedSchemas) {
      String schemaLocation = userDefinedSchema.getSchemaLocation();
      userDefnSchemaPaths.add(schemaLocation);
    }

       SOAJOperationDTO operationDTO = soaConfigDTO.getOperationDTOForOperationName("OperationName");

        InputDataProcessor inputDataProcessor = new InputDataProcessor();
        List<Source> sourceList = inputDataProcessor.parseParamStrings("<ns:OperationName xmlns:ns=\"http://javector.com\"><billToType xmlns=\"http://javector.com/ser/adaptive/po\">" +
                "<street>125 Main Street</street>" +
                "<city>Canton</city>" +
                "<state>OH</state>" +
                "<zip>98134</zip>" +
                "<phone>(973) 243-8776</phone></billToType></ns:OperationName>");
       ServiceInvoker serviceInvoker = new ServiceInvoker(soaConfigDTO.getMappingXml(),userDefnSchemaPaths);
       try {
           String o =(String) serviceInvoker.invokeSOAService(operationDTO, sourceList);
           System.out.println("o = " + o);
           String[] splitStrings = o.split("<zip>");
           String zipString = splitStrings[1].split("</zip>")[0];
           assertEquals("1222",zipString);
       } catch (SoajException e) {
           assertTrue(false);
       }
   }


}
