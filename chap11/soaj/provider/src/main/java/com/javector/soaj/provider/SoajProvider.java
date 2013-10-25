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
package com.javector.soaj.provider;

import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;
import com.javector.soaj.provider.processing.RequestController;
import com.javector.soaj.util.IOUtil;
import com.javector.soaj.util.SoapFaultUtil;
import com.javector.soaj.util.XmlUtil;

import javax.annotation.Resource;
import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.*;

import org.w3c.dom.Document;

@ServiceMode(value=Service.Mode.PAYLOAD)
@WebServiceProvider(
    serviceName = "SoajProviderService",
    portName = "SoajProviderPort",
    targetNamespace = "http://javector.com/soaj/provider",
    wsdlLocation = "WEB-INF/wsdl/SoajProvider.wsdl")
    public class SoajProvider implements Provider<Source> {
  
  private static SoajLogger log = LoggingFactory.getLogger(SoajProvider.class);
  
  @Resource(type=Object.class)
  protected WebServiceContext webServiceContext;
  public Source invoke(Source source) {
    
    Source returnSource;
    try{
      // TODO converting Source to String is an expensive operation.  Need
      // to be able to turn it off in production.  Maybe a tracing on/off
      // switch?
      // convert payload to a DOMSource to support multiple reads.
      // this is a kludge used only to preserve the code in the examples
      // below after GlassFish changed the implementation of JAX-WS.
      DocumentBuilderFactory dbfac1 = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder1 = dbfac1.newDocumentBuilder();
      Document sourceDoc1 = docBuilder1.newDocument();
      Transformer xformer1 = TransformerFactory.newInstance().newTransformer();
      xformer1.transform(source, new DOMResult(sourceDoc1));
      source = new DOMSource(sourceDoc1);
      log.info("SoajProvider processing this source:" + IOUtil.NL + 
          XmlUtil.toString(source));
      returnSource =
        new RequestController().handleServiceRequest(webServiceContext,source);
    } catch(Throwable t) {
      SOAPFaultException sfe;
      if ( SOAPFaultException.class.isInstance(t) ) {
        sfe = (SOAPFaultException) t;
      } else {
        /*
         * If we end up here, it means that an unhandled SOAJ problem or interal
         * GlassFish error is happening. Excpetions that are throw by deployed
         * methods (e.g., InvocationTargetException) should have been handled by
         * the fault processing system and already converted to 
         * SOAPFaultException.
         */
        SOAPFault sf = SoapFaultUtil.toSOAPFault(
            "An unexpected error occured on the server.  This is either "+
            "a Java EE 5 implementation problem or a SOAJ error that is "+
        "not getting handled properly.");
        SoapFaultUtil.addToSOAPFault(t, sf);
 
        sfe = new SOAPFaultException(sf);
      }
      log.info("SoajProvider returning this SOAP Fault:" + IOUtil.NL + 
          XmlUtil.toString(sfe.getFault()));
      throw sfe;
    }
    log.info("SoajProvider returning this source:" + IOUtil.NL + 
        XmlUtil.toString(returnSource));
    return returnSource;
    
  }
  
}