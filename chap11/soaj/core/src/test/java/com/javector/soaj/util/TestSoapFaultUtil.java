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


import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import javax.xml.soap.Detail;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

public class TestSoapFaultUtil extends TestCase {

  public static Test suite() {
    return new TestSuite(TestSoapFaultUtil.class);
}

protected void setUp() throws Exception {}


   public void testThrowableToSOAPFault() throws Exception {

     SOAPFault sf;
     try {
       throw new RuntimeException("hello world");
     } catch (Throwable t) {
       sf = SoapFaultUtil.toSOAPFault(t);
       Detail d = sf.getDetail();
       Iterator it = d.getChildElements(SoapFaultUtil.FAULT_DETAIL_ELEMENT_QNAME);
       SOAPElement detailElt = (SOAPElement) it.next();
       // has 2 children: <message> and <stackTrace> ...
       SOAPElement msgElt = (SOAPElement) detailElt.
       getChildElements(SoapFaultUtil.FAULT_MESSAGE_CHILD_QNAME).next();
       SOAPElement stackTraceElt = (SOAPElement) detailElt.
       getChildElements(SoapFaultUtil.FAULT_STACKTRACE_CHILD_QNAME).next();
       assertEquals("hello world", msgElt.getTextContent());
       assertTrue(stackTraceElt.getTextContent().contains("java.lang.RuntimeException"));
       assertTrue(stackTraceElt.getTextContent().contains(this.getClass().getCanonicalName()));
       return;
     }
     
   }
   
   public void testStringToSOAPFaultException() throws Exception {
     
     SOAPFault sf = SoapFaultUtil.toSOAPFault("hello world");
     Detail d = sf.getDetail();
     Iterator it = d.getChildElements(SoapFaultUtil.FAULT_DETAIL_ELEMENT_QNAME);
     SOAPElement detailElt = (SOAPElement) it.next();
     // has 1 child: <message>
     SOAPElement msgElt = (SOAPElement) detailElt.
     getChildElements(SoapFaultUtil.FAULT_MESSAGE_CHILD_QNAME).next();
     assertEquals("hello world", msgElt.getTextContent());
     
   }
   
  public void testAddToSOAPFaultException() throws Exception {
     
     SOAPFault sf = SoapFaultUtil.toSOAPFault("hello world");
     try {
       throw new RuntimeException("goodbye world");
     } catch (RuntimeException re) {
       SoapFaultUtil.addToSOAPFault(re, sf);
       Detail d = sf.getDetail();
       Iterator it = d.getChildElements(SoapFaultUtil.FAULT_DETAIL_ELEMENT_QNAME);
       SOAPElement detailElt1 = (SOAPElement) it.next();
       SOAPElement detailElt2 = (SOAPElement) it.next();
       Iterator i1 = detailElt1.getChildElements(SoapFaultUtil.FAULT_MESSAGE_CHILD_QNAME);
       SOAPElement msgElt1 = (SOAPElement) i1.next();
       assertEquals("hello world", msgElt1.getTextContent());
       Iterator i2 = detailElt2.getChildElements(SoapFaultUtil.FAULT_MESSAGE_CHILD_QNAME);
       SOAPElement msgElt2 = (SOAPElement) i2.next();
       SOAPElement stackTraceElt = (SOAPElement) detailElt2.
       getChildElements(SoapFaultUtil.FAULT_STACKTRACE_CHILD_QNAME).next();
       assertEquals("goodbye world", msgElt2.getTextContent());
       assertTrue(stackTraceElt.getTextContent().contains("java.lang.RuntimeException"));
       assertTrue(stackTraceElt.getTextContent().contains(this.getClass().getCanonicalName()));
     }
     
   }

   
}
