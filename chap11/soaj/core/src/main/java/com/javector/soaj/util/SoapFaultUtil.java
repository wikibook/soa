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

import com.javector.soaj.SoajRuntimeException;

import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

/**
 * Utily class for creating and manipulating SOAPFaults.  SOAJ uses these to
 * produce SOAPFaultExceptions which can be thrown at any point in the
 * code where a non-recoverable error occurs and the details should be
 * sent back to the SOAP requester.  The Java EE 5 container will automatically
 * translate the thrown SOAPFaultException into a SOAP Fault and send it back
 * to the SOAP requestor.
 * 
 * The structure of the SOAP Faults generated by SOAJ is:
 * 
 * <SOAP-ENV:Fault xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
 *  <faultstring>SOAJ Processing Error.</faultstring>
 *  <faultcode>SOAP-ENV:Server</faultcode>
 *  <detail>
 *    <faultDetail xmlns="http://javector.com/faults">
 *      <message>hello world</message>
 *    </faultDetail>
 *    <faultDetail xmlns="http://javector.com/faults">
 *      <message>goodbye world</message>
 *      <stackTrace>java.lang.RuntimeException: goodbye world
 *        at com.javector.soaj.util.TestSoapFaultUtil.testAddToSOAPFaultException(TestSoapFaultUtil.java:63)
 *        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
 *        .....
 *      </stackTrace>
 *    </faultDetail>
 *   </detail>
 * </SOAP-ENV:Fault>
 * 
 * @author mark@javector.com
 *
 */
public class SoapFaultUtil {

  public static final QName FAULT_DETAIL_ELEMENT_QNAME = 
    new QName("http://javector.com/faults", "faultDetail");
  public static final QName FAULT_MESSAGE_CHILD_QNAME = 
    new QName("http://javector.com/faults", "message");
  public static final QName FAULT_STACKTRACE_CHILD_QNAME = 
    new QName("http://javector.com/faults", "stackTrace");
  
  
  /**
   * Creates a SOAP fault message that has detail elements containing the 
   * message and stack trace of the Throwable.
   * 
   * @param t The throwable used to create the SOAP fault.  If this is an
   * instance of SOAPFaultException, it is simple returned unchanged.
   * @return
   */
  public static final SOAPFault toSOAPFault(Throwable t) {
    
    if ( SOAPFaultException.class.isInstance(t) ) { 
      return ((SOAPFaultException) t).getFault(); }
    try {
      SOAPFactory fac = SOAPFactory.newInstance();  
      SOAPFault sf = fac.createFault(
          "SOAJ Processing Error.", 
          new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server"));
      if ( t != null ){
        addToSOAPFault(t, sf);
      }
      return sf;
    } catch (SOAPException e) {
      throw new SoajRuntimeException("SAAJ implementation threw a "+
          "SOAPException while SOAJ was trying to create a SOAP Fault.", e);
    }
    
  }
  
  /**
   * Creates a SOAP fault message that has a single detail element containing
   * the message.
   * 
   * @param msg the single detail element in the SOAP fault
   * @return
   */
  public static final SOAPFault toSOAPFault(String msg) {
    
    if ( msg == null || msg.length() == 0 ) {
      throw new SoajRuntimeException("Not allowed to create a SOAP Fault "+
      "from an empty or null message.");
    }
    try {
      SOAPFactory fac = SOAPFactory.newInstance();  
      SOAPFault sf = fac.createFault(
          "SOAJ Processing Error.", 
          new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server"));
      Detail d = sf.addDetail();
      SOAPElement de = d.addChildElement(FAULT_DETAIL_ELEMENT_QNAME);
      SOAPElement messageElement = de.addChildElement(FAULT_MESSAGE_CHILD_QNAME);
      messageElement.addTextNode(msg);
      return sf;
    } catch (SOAPException e) {
      throw new SoajRuntimeException("SAAJ implementation threw a "+
          "SOAPException while SOAJ was trying to create a SOAP Fault.", e);
    }
    
  }
  
  /**
   * Add the Throwable's message and stack trace to the SOAP fault as a new
   * <faultDetail> child of the detail element.
   * @param t
   * @param sfe
   */
  public static final void addToSOAPFault(
      Throwable t, SOAPFault sf) {
    
    if ( t == null ) { return; }
    try {
      Detail d = sf.getDetail();
      if ( d == null ) {
        d = sf.addDetail();
      }
      SOAPElement de = d.addChildElement(FAULT_DETAIL_ELEMENT_QNAME);
      addThrowableToDetailElement(t, de);
    } catch (SOAPException e) {
      throw new SoajRuntimeException("SAAJ implementation threw a SOAPException " +
          "while SOAJ was editing a SOAPFaultException.", e);
    }
    
  }
  
  /**
   * Add the Throwable's message and stack trace to the detail element as child
   * nodes <message> and <stackTrace>.
   * @param t
   * @param detailElt a SOAP Fault detail element.
   */
  private static void addThrowableToDetailElement(
      Throwable t, SOAPElement detailElt) {
    
    if (t == null) { return; }
    try {
      if ( t.getMessage() != null && t.getMessage().length() > 0 ) {
        SOAPElement messageElement = 
          detailElt.addChildElement(FAULT_MESSAGE_CHILD_QNAME);
        messageElement.addTextNode(t.getMessage());
      }
      SOAPElement stackTraceElement = 
        detailElt.addChildElement(FAULT_STACKTRACE_CHILD_QNAME);
      stackTraceElement.addTextNode(IOUtil.stackTrace(t));
    } catch (SOAPException e) {
      throw new SoajRuntimeException("SAAJ implementation threw a "+
          "SOAPException while SOAJ was trying edit a detail element of a "+
          "SOAP Fault.", e);
    }
    
  }
  
  
}