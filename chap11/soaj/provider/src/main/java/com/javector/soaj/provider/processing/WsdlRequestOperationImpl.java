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

import java.net.MalformedURLException;
import java.net.URL;

import com.javector.WsdlCache;
import com.javector.soaj.SoajException;
import com.javector.soaj.SoajRuntimeException;
import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;

/**
 * Handle request processing specific to a WSDL request.
 *
 */
public class WsdlRequestOperationImpl extends RequestOperation {
  Object serviceResponseObject = null;
  
  private SoajLogger log = LoggingFactory.getLogger(WsdlRequestOperationImpl.class);
  
  public WsdlRequestOperationImpl(WSRequest wsRequest) {
    super(wsRequest);
  }
  
  protected void doPreOperation() { }
  
  /* 
   * Get the WSDL from the cache (the cache generates it if necessary) and set
   * the generatedWSDL property on the wsRequest DTO.
   * 
   * @see com.javector.soaj.provider.processing.RequestOperation#doOperation()
   */
  protected void doOperation() { 
    
    // get the path of the requested WSDL
    String requestedEndpoint = wsRequest.getRequestURL();
    log.info("getting WSDL deployed at wsRequest.getRequestURL() = " + requestedEndpoint);
    URL requestedUrl;
    try {
      requestedUrl = new URL(requestedEndpoint);
    } catch (MalformedURLException e) {
      throw new SoajRuntimeException(e); // shouldn't ever happen
    }
    String requestedWsdlPath = requestedUrl.getPath();
    try {
      wsRequest.setGeneratedWsdl(WsdlCache.getWsdl(requestedWsdlPath, wsRequest.getSoaConfigDTO()).getWsdl());
    } catch (SoajException e) {
      wsRequest.setGeneratedWsdl("No Web service is deployed at: " + requestedEndpoint);
    }
    
  }
  
  protected void doPostOperation() { }
  
  protected WSResponse getOutputParam() {
    
    WSResponse wsResponse = new WSResponse();
    wsResponse.setResponseString(wsRequest.getGeneratedWsdl());
    return wsResponse;
    
  }
  
}
