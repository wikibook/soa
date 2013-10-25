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

import java.io.ByteArrayInputStream;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;

public class WSResponse {
  
  SoajLogger log = LoggingFactory.getLogger(WSResponse.class);
  
  /**
   * When doing a service operation, contains the operation wrapper to be 
   * inserted into a SOAP envelope.  When doing a WSDL request ...
   */
  private String responseString = null;
  
  public String getResponseString() {
    return responseString;
  }
  
  public void setResponseString(String responseString) {
    this.responseString = responseString;
  }
  
  public Source getAsSource() {
    
    Source source = new StreamSource(
        new ByteArrayInputStream(responseString.getBytes()));
    return source;
    
  }
  
}
