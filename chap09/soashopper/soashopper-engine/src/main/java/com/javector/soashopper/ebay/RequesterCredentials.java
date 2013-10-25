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
/**
 * RequesterCredentials.java An example class that demonstrates how to use the
 * eBay APIs and connect to the sandbox and production eBay servers. Copyright
 * 2006 Sun Microsystems, Inc. ALL RIGHTS RESERVED Use of this software is
 * authorized pursuant to the terms of the license found at
 * http://developers.sun.com/berkeley_license.html
 */

package com.javector.soashopper.ebay;

import javax.xml.ws.handler.*;
import javax.xml.ws.handler.soap.*;
import javax.xml.soap.*;
import javax.xml.namespace.QName;

import com.javector.soashopper.ShopperCredentials;

import java.util.Set;

public class RequesterCredentials implements SOAPHandler<SOAPMessageContext> {

  public Set<QName> getHeaders() {

    return null;
  }

  public boolean handleMessage(SOAPMessageContext smc) {

    addRequesterCredentials(smc);
    return true;
  }

  public boolean handleFault(SOAPMessageContext smc) {

    return true;
  }

  public void close(MessageContext messageContext) {

  }

  private void addRequesterCredentials(SOAPMessageContext smc) {

    if (ShopperCredentials.getEBayAppID().equals("MUST REGISTER")) {
      System.out
          .println("eBay properties have not been set. You must register as a developer");
      System.out
          .println("with eBay before you can call the production or sandbox servers.");
      System.exit(1);
    }
    // ! <example xn="RequesterCredentials_addRequesterCredentials">
    // ! <c>chap09</c><s>soapclient</s>
    Boolean outboundProperty = (Boolean) smc
        .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

    if (outboundProperty.booleanValue()) {
      SOAPMessage message = smc.getMessage();
      try {
        SOAPHeader header = message.getSOAPHeader();
        if (header == null) {
          message.getSOAPPart().getEnvelope().addHeader();
          header = message.getSOAPHeader();
        }
        SOAPElement heSecurity = header.addChildElement("RequesterCredentials",
            "ebl", "urn:ebay:apis:eBLBaseComponents");
        heSecurity.addChildElement("eBayAuthToken", "ebl",
            "urn:ebay:apis:eBLBaseComponents").addTextNode(
            ShopperCredentials.getEBayAuthToken());
        SOAPElement userNameToken = heSecurity.addChildElement("Credentials",
            "ebl", "urn:ebay:apis:eBLBaseComponents");
        userNameToken.addChildElement("AppId", "ebl",
            "urn:ebay:apis:eBLBaseComponents").addTextNode(
            ShopperCredentials.getEBayAppID());
        userNameToken.addChildElement("DevId", "ebl",
            "urn:ebay:apis:eBLBaseComponents").addTextNode(
            ShopperCredentials.getEBayDevID());
        userNameToken.addChildElement("AuthCert", "ebl",
            "urn:ebay:apis:eBLBaseComponents").addTextNode(
            ShopperCredentials.getEBayCertID());

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    // ! </example>
  }
}
