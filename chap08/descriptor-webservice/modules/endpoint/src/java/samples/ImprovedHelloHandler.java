/*
* Copyright 2006 Javector Software LLC
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
package samples;

import java.util.Set;
import javax.annotation.Resource;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

//! <example xn="WEBSERVICE_XML_HANDLER">
//! <c>chap08</c><s>descriptor</s>
public class ImprovedHelloHandler implements SOAPHandler<SOAPMessageContext> {
  
  public static final String APPEND_STRING = "samples.HelloHandler.appendStrg";
  
  @Resource(name="appendedString")
  String injectedString = "undefined"; 
  
  public boolean handleMessage(SOAPMessageContext context) {
    
    if ( ((Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).
        booleanValue() ) return true;
    try {
      context.put(APPEND_STRING, "NEW & IMPROVED!!! "+injectedString);
      context.setScope(APPEND_STRING, MessageContext.Scope.APPLICATION);
      System.out.println("The IMPROVED handler has appended this: " + injectedString);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebServiceException(e);
    }
    
  }
//! </example>
  
  public Set<QName> getHeaders() {
    return null;
  }
  
  public boolean handleFault(SOAPMessageContext context) {
    return true;
  }
  
  public void close(MessageContext context) {}
  
}
