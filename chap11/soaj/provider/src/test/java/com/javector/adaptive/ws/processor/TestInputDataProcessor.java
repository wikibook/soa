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
package com.javector.adaptive.ws.processor;

import junit.framework.TestCase;

import javax.xml.transform.Source;
import java.util.List;

import com.javector.soaj.util.XmlUtil;

/**
 * Created by IntelliJ IDEA.
 * User: javector
 * Date: May 26, 2006
 * Time: 8:00:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestInputDataProcessor extends TestCase {

    public TestInputDataProcessor(String s){
        super(s);
    }

    public void testProcessServiceRequest(){
        InputDataProcessor inputDataProcessor = new InputDataProcessor();
        String soapData= "<ns:OperationName xmlns:ns=\"http://javector.com\">" +
                "<billToType xmlns=\"http://javector.com/ser/adaptive/po\">" +
                "<street>125 Main Street</street>" +
                "<city>Canton</city>" +
                "<state>OH</state>" +
                "<zip>98134</zip>" +
                "<phone>(973) 243-8776</phone></billToType>" +
                "<billToType xmlns=\"http://javector.com/ser/adaptive/po\">" +
                "<street>329 North Street</street>" +
                "<city>Canton</city>" +
                "<state>OH</state>" +
                "<zip>98134</zip>" +
                "<phone>(973) 246-5430</phone></billToType>" +
                "</ns:OperationName>";
        List<Source> paramSources = inputDataProcessor.parseParamStrings(soapData);
        String expectedParamString1 ="<?xml version=\"1.0\" encoding=\"UTF-8\"?><billToType xmlns=\"http://javector.com/ser/adaptive/po\"><street>125 Main Street</street><city>Canton</city><state>OH</state><zip>98134</zip><phone>(973) 243-8776</phone></billToType>" ;
        String expectedParamString2 ="<?xml version=\"1.0\" encoding=\"UTF-8\"?><billToType xmlns=\"http://javector.com/ser/adaptive/po\"><street>329 North Street</street><city>Canton</city><state>OH</state><zip>98134</zip><phone>(973) 246-5430</phone></billToType>" ;


        System.out.println("ParamString1 = " + paramSources.get(0));
        System.out.println("ParamString2 = " + paramSources.get(1));


         assertEquals( XmlUtil.toString(paramSources.get(0)),expectedParamString1);
         assertEquals( XmlUtil.toString(paramSources.get(1)),expectedParamString2);
    }

}
