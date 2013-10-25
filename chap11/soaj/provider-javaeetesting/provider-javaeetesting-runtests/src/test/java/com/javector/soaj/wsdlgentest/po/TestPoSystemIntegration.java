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

import com.javector.ser.adaptive.po.*;
import com.javector.soaj.util.XmlUtil;
import com.javector.soaj.util.IOUtil;
import com.javector.soaj.provider.posystem.POSystemService;
import com.javector.soaj.provider.posystem.POSystemPortType;
import junit.framework.TestCase;
import junit.framework.Assert;

import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

/**
 * Created by IntelliJ IDEA.
 * Auther: Rohit Agarwal
 * Date: Sep 24, 2006
 * Time: 10:29:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestPoSystemIntegration extends TestCase {



    public void testPurchaseOrderProcessingForAddressUpdate() throws Exception {
        POSystemService service = new POSystemService();
        POSystemPortType posystemPort = service.getPOSystemPort();
        BillToType billTo = new BillToType();
        billTo.setCity("Agra");
        billTo.setCompany("Javector");
        billTo.setState("U.P.");
        billTo.setStreet("125 Main Street");
        billTo.setZip("650034");
        billTo.setPhone("(973) 243-8776");
        BillToType billTofromWebService;
        try  {
        	posystemPort.updateAddress(billTo,"posystem");
        	billTofromWebService = posystemPort.retrieveAddress("posystem");
            System.out.println("billTofromWebService  = " + billTofromWebService);
            Assert.assertNotNull(billTofromWebService);
        }catch(SOAPFaultException sfe) {
            SOAPFault fault = sfe.getFault();
            System.out.println("SOAPFault:" + IOUtil.NL +
                    XmlUtil.toFormattedString(fault));
            throw sfe;
        }
        Assert.assertEquals(billTofromWebService.getCity(),billTo.getCity());
        Assert.assertEquals(billTofromWebService.getState(),billTo.getState());
        Assert.assertEquals(billTofromWebService.getZip(),billTo.getZip());
    }



}
