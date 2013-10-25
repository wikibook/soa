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

import java.io.IOException;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class AddMessageIdHandler implements SOAPHandler<SOAPMessageContext> {

  public static final QName MSGID_HEADER = 
    new QName("http://www.example.com/soap", "msgid");
  public static final String MSGID_PROP = 
    "samples.persistence.msgid";
  
  public Set<QName> getHeaders() {
    return null;
  }

  public boolean handleMessage(SOAPMessageContext ctxt) {
    
    String msgId = (String) ctxt.get(MSGID_PROP);
    System.out.println("Entered AddMessageIdHandler.handleMessage. msgId = " + 
        msgId);
    //  return if inbound message
    if ( !((Boolean)ctxt.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).
        booleanValue()) { return true; }
    if ( msgId == null ) {
      System.out.println("Message ID context property is null.");
      return false;
    }
    SOAPMessage msg = ctxt.getMessage();
    SOAPHeader hdr;
    try {
      hdr = msg.getSOAPHeader();
      if ( hdr == null ) {
        hdr = msg.getSOAPPart().getEnvelope().addHeader();
      }
      SOAPHeaderElement msgIdElement = hdr.addHeaderElement(MSGID_HEADER);
      msgIdElement.setAttribute("id", msgId);
    } catch (SOAPException e) {
      e.printStackTrace();
      return false;
    }
    return true;
    
  }

  public boolean handleFault(SOAPMessageContext ctxt) {
    return false;
  }

  public void close(MessageContext ctxt) {}
  

}
