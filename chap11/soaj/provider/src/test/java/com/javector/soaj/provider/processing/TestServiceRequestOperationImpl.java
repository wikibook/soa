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
package com.javector.soaj.provider.processing;

import junit.framework.TestCase;

import com.javector.adaptive.util.SOAConfigReader;
import com.javector.adaptive.util.dto.SOAConfigDTO;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: javector
 * Date: May 27, 2006
 * Time: 2:20:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestServiceRequestOperationImpl extends TestCase {

   public   TestServiceRequestOperationImpl(String s) {
       super(s);
   }


    public void testPerform(){
        WSRequest  wsRequest = new WSRequest();
        wsRequest.setRequestURL("http://www.javector.com");
        wsRequest.setOperationName("OperationName");
       SOAConfigReader soaConfigReader = new SOAConfigReader();
       InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/SoajConfig.xml");
       SOAConfigDTO soaConfigDTO = soaConfigReader.parserSOAConfig(resourceAsStream);
       wsRequest.setSoaConfigDTO(soaConfigDTO);

       ServiceRequestOperationImpl serviceRequestOperation = new ServiceRequestOperationImpl(wsRequest){
           protected void doOperation() {

           }
            protected  WSResponse getOutputParam(){
                String generatedWsdl = wsRequest.getGeneratedWsdl();
                WSResponse wsResponse = new WSResponse();
                wsResponse.setResponseString(generatedWsdl);
                return wsResponse;
            }
        };
        WSResponse wsResponse = serviceRequestOperation.perform();
        String responseString = wsResponse.getResponseString();
        System.out.println("generated wsdl as responseString = " + responseString);
    }


}
