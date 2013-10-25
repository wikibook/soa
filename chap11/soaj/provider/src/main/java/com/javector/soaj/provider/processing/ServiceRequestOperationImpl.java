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

import com.javector.adaptive.util.ServiceInvoker;
import com.javector.adaptive.util.dto.SOAConfigDTO;
import com.javector.adaptive.util.dto.SOAJOperationDTO;
import com.javector.soaj.SoajException;
import com.javector.soaj.SoajRuntimeException;
import com.javector.soaj.config.UserDefinedSchema;
import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;
import com.javector.soaj.util.IOUtil;
import com.javector.soaj.util.SoapFaultUtil;

import java.util.List;
import java.util.ArrayList;

import javax.xml.soap.Detail;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;


/**
 * The concrete implementation
 * {@link RequestOperation} for SOAP
 * request processing.
 */
public class ServiceRequestOperationImpl extends RequestOperation {
  
  SoajLogger log = LoggingFactory.getLogger(ServiceRequestOperationImpl.class);
  
  Object serviceResponseObject = null;
  
  public ServiceRequestOperationImpl(WSRequest wsRequest) {
    super(wsRequest);
  }
  
  protected void doPreOperation() {
    
  }
  
  protected void doOperation() {
    
    SOAConfigDTO configDTO = wsRequest.getSoaConfigDTO(); 
    String opName = wsRequest.getOperationName();
    SOAJOperationDTO soajOperationDTO = 
      configDTO.getOperationDTOForOperationName(opName);
    
    List<UserDefinedSchema> userDefinedSchemas = wsRequest.getSoaConfigDTO().getUserDefinedSchemas();
    ArrayList<String> userDefnSchemaPaths = new ArrayList<String>();
    for (UserDefinedSchema userDefinedSchema : userDefinedSchemas) {
      String schemaLocation = userDefinedSchema.getSchemaLocation();
      userDefnSchemaPaths.add(schemaLocation);
    }
    ServiceInvoker serviceInvoker =
      new ServiceInvoker(wsRequest.getSoaConfigDTO().getMappingXml(),
          userDefnSchemaPaths);
    try {
      serviceResponseObject = serviceInvoker.invokeSOAService(soajOperationDTO, wsRequest.getXmlParameters());
    } catch (SoajRuntimeException e) {
      SOAPFault sf = SoapFaultUtil.toSOAPFault(
          "Invocation of the SOAJ Operation failed. className: " +
          soajOperationDTO.getTargetServiceClassName() + "  methodName:" +
          soajOperationDTO.getTargetServiceMethodName()
      );
      SoapFaultUtil.addToSOAPFault(e, sf);
      throw new SOAPFaultException(sf);
    } catch (SoajException e) {
      throw new RuntimeException(e);
    }
  }
  
  
  protected void doPostOperation() {
    
  }
  
  protected WSResponse getOutputParam() {         
    WSResponse wsResponse = new WSResponse();
    
    SOAConfigDTO configDTO = wsRequest.getSoaConfigDTO(); 
    String opName = wsRequest.getOperationName();
    SOAJOperationDTO soajOperationDTO = 
      configDTO.getOperationDTOForOperationName(opName);
    String opNameSpace = soajOperationDTO.getTargetNameSpace();
    // TODO potential prefix clashes ?
    String wrapperStartTag =
      "<opns:"+opName+"Response xmlns:opns=\""+opNameSpace+"\">";
    String wrapperEndTag = "</opns:"+opName+"Response>";
    String responseXml = wrapperStartTag + serviceResponseObject.toString() + wrapperEndTag;
    log.info("Setting WSResponse string to: " + IOUtil.NL + responseXml);
    wsResponse.setResponseString(responseXml);
    return wsResponse;
    
  }
}
