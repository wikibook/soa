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

import com.javector.adaptive.ws.processor.InputDataProcessor;
import com.javector.soaj.SoajException;
import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;

import javax.xml.transform.Source;
import javax.xml.ws.WebServiceContext;
import javax.servlet.http.HttpServletRequest;

public class RequestController {
  
  private SoajLogger log = LoggingFactory.getLogger(RequestController.class);
  
  InputDataProcessor inputDataProcessor ;
  FlowInterpreter flowInterpreter;
  
  public RequestController() {
    
    inputDataProcessor = new InputDataProcessor();
    flowInterpreter = new FlowInterpreter();
    
  }
  
  /**
   * Handles a service request that is received by a transport listener such as
   * SoajProvider.  For a SOAP request, this means dispatching it to the
   * SOAJ framework for invocation of the operation.  If the operation
   * exceutes normally, this returns the XML payload that is the unique
   * child of the SOAP response body.  (Header elements are not supported.)
   * If the operation generated a SOAP Fault, it gets thrown as a 
   * SOAPFaultException which must be handled by the transport listener.
   * 
   * @param webServiceContext contains HTTP specific context information that
   * is required for SOAP dispatching (e.g., context-path).
   * @param source the XML payload from the SOAP request (Header elements are
   * not supported.)
   * @return the XML payload to be included in a SOAP response.
   * @throws SOAPFaultException when a SOAP Fault is generated
   * @throws SoajRuntimeException when SOAJ error occurs that should not be
   * returned as a SOAP Fault
   */
  public Source handleServiceRequest(
      WebServiceContext webServiceContext, Source source) {

    WSRequest wsRequest = inputDataProcessor.processServiceRequest(webServiceContext, source);
    RequestOperation serviceOperation = flowInterpreter.selectOperation(wsRequest);
    WSResponse wsResponse = serviceOperation.perform();
    return wsResponse.getAsSource();
    
  }
  
  public String handleWsdlRequest(
      HttpServletRequest httpServletRequest) throws SoajException {

    WSRequest wsRequest = inputDataProcessor.processWsdlRequest(httpServletRequest);
    RequestOperation wsdlOperation = flowInterpreter.selectOperation(wsRequest);
    WSResponse wsResponse = wsdlOperation.perform();
    return wsResponse.getResponseString();
    
  }
  
  
}
