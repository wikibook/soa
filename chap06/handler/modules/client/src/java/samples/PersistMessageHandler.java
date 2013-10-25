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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

//! <example xn="PersistMessageHandler">
//! <c>chap06</c><s>handler</s>
public class PersistMessageHandler implements SOAPHandler<SOAPMessageContext> {

  public static final String PERSISTENCE_DIR_PROP = 
    "samples.persistence.directory";
  
  public Set<QName> getHeaders() {
    return null;
  }

  public boolean handleMessage(SOAPMessageContext ctxt) {
    
    System.out.println("Entered PersistMessageHandler.handleMessage");
    //  return if inbound message
    if ( !((Boolean)ctxt.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).
        booleanValue()) { return true; }
    SOAPMessage msg = ctxt.getMessage();
    File persistenceDir = (File) ctxt.get(PERSISTENCE_DIR_PROP);
    Iterator itr;
    try {
      itr = msg.getSOAPHeader().examineAllHeaderElements();
      String msgId = null;
      while (itr.hasNext() && msgId == null) {
        SOAPHeaderElement headerElt = (SOAPHeaderElement) itr.next();
        QName headerQName = headerElt.getElementQName();
        if (headerQName.equals(AddMessageIdHandler.MSGID_HEADER)) {
          msgId = headerElt.getAttribute("id");
        }
      }
      if ( msgId == null ) {
        System.out.println("No message ID header.");
        return false;
      }
      File msgFile = new File(persistenceDir, msgId+".xml");
      msgFile.createNewFile();
      msg.writeTo(new FileOutputStream(msgFile));
    } catch (Exception e) {
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
//! </example>
