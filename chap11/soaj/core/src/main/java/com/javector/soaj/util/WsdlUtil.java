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

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Fault;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.xml.namespace.QName;

import org.eclipse.wst.wsdl.validation.internal.IValidationMessage;
import org.eclipse.wst.wsdl.validation.internal.IValidationReport;
import org.eclipse.wst.wsdl.validation.internal.WSDLValidator;

import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;

public abstract class WsdlUtil {
  
  SoajLogger logger = LoggingFactory.getLogger(WsdlUtil.class.getName());
  
  public static String compareWsdl(Definition wsdlExpected, Definition wsdlGenerated) {
  
    WsdlEquivalence wsdlEquivTester = new WsdlEquivalence(wsdlExpected, wsdlGenerated);
    return wsdlEquivTester.compare();
    
  }
  
  /**
   * Validate the WSDL using the Eclipse WSDL validator.  Thread safe.
   * 
   * @param wsdlURL a URL pointing to the WSDL
   * @param stream an InputStream for reading the WSDL
   * @return null if the WSDL is valid.  Otherwise an error report.
   */
  public static String validateWSDL(String wsdlURL, InputStream stream) {
    
    WSDLValidator wsdlValidator = new WSDLValidator();
    IValidationReport report = wsdlValidator.validate(wsdlURL, stream);
    if ( report.isWSDLValid() ) { return null; }
    String errorReport = "";
    for(IValidationMessage msg : report.getValidationMessages()) {
      errorReport = getMsg(msg, "") + IOUtil.NL;
    }
    return errorReport;  
    
  }
  
  
  private static String getMsg(IValidationMessage msg, String indent) {
    
    String sev = (msg.getSeverity() == IValidationMessage.SEV_ERROR ? "SEVERE " : "WARNING");
    String errMsg = indent + sev + "  line: "+ msg.getLine() + " col: " + msg.getColumn() + " " + msg.getMessage();
    List<IValidationMessage> nestedMsg = msg.getNestedMessages();
    if ( nestedMsg == null ) { return errMsg; }
    for (IValidationMessage nmsg : nestedMsg) {
      errMsg += IOUtil.NL + getMsg(nmsg, indent+"  ");
    }
    return errMsg;
    
  }
  
  /**
   * This class encapsulates two WSDL definitions and provides methods to compare
   * them.  It is used to determine if two WSDLs are equivalent.
   */
  private static class WsdlEquivalence {
        
    SoajLogger logger = LoggingFactory.getLogger(WsdlUtil.class.getName());

    private Definition wsdlExpected;
    private Definition wsdlGenerated;
  
    
    public WsdlEquivalence(Definition wsdlExpected, Definition wsdlGenerated) {

      this.wsdlExpected = wsdlExpected;
      this.wsdlGenerated = wsdlGenerated;
      
    }
    
    /**
     * Compare the two WSDLs 
     * @return null if the WSDLs are equivalent.  Otherwise return a report of the
     * cause for non-equivalence.
     */
    public String compare() {
      
      String targetEquivalence = validateTargetNameSpace();
      if ( targetEquivalence != null ) { return targetEquivalence; }
      return validateServices();

    }
    
    
    public String validateServices() {
      
      Map<QName, Service> expServices = wsdlExpected.getServices();
      Iterator<Map.Entry<QName, Service>> iterator = expServices.entrySet().iterator();
      while(iterator.hasNext()) {
        Map.Entry<QName, Service> entry = iterator.next();
        QName key = entry.getKey();
        logger.info("expectedService name  = " + key);
        Service genService = (Service) wsdlGenerated.getService(key);
        if( genService == null ) {
          return "There are no services for expected service name =" + key;
        }
        Service expService = entry.getValue();
        String compareServicesReport = compareServices(expService,genService);
        if ( compareServicesReport != null ) { return compareServicesReport; }
      }
      return null;
      
    }
    
    private String compareServices(Service expService, Service genService) {
      
      Map<String, Port> ports = expService.getPorts();
      Set<String> expPortNames = ports.keySet();
      for(String expPortName : expPortNames) {
        Port genPort = (Port)genService.getPorts().get(expPortName);
        logger.info("expected port name = " + expPortName);
        if(genPort == null) {
          logger.info("no mapping found for port name = " + expPortName);
          return "There are no generated port for expected port name =" +expPortName;
        }
        Port expPort = ports.get(expPortName);
        String comparePortReport = comparePorts(expPort,genPort);
        if ( comparePortReport != null ) { return comparePortReport; }
      }
      return null;      
      
    }
    
    private String comparePorts(Port expPort, Port genPort) {
      Binding expBinding = expPort.getBinding();
      Binding genBinding = genPort.getBinding();
      return compareBindings(expBinding,genBinding);

    }
    
    private String compareBindings(Binding expBinding, Binding genBinding) {
      
      List<BindingOperation> expBindingOperations = expBinding.getBindingOperations();
      if(!expBinding.getQName().equals(genBinding.getQName())) {
        logger.info("exp binding name = " + expBinding.getQName());
        logger.info("gen binding name = " +genBinding.getQName());
        return "The generated name for binding = " + genBinding.getQName() +
        "not same as the expected binding name =" + expBinding.getQName();
      }
      PortType expPortType = expBinding.getPortType();
      PortType genPortType = genBinding.getPortType();
      String comparePortTypesReport = comparePortTypes(expPortType,genPortType);
      if ( comparePortTypesReport != null ) { return comparePortTypesReport; }
      for(BindingOperation expBindingOperation : expBindingOperations) {
        String name = expBindingOperation.getName();
        logger.info("exp binding name = " + name);
        BindingOperation genBindingOperation = genBinding.getBindingOperation(name, null, null);
        String compareBindingOperationsReport = compareBindingOperations(expBindingOperation,genBindingOperation);
        if ( compareBindingOperationsReport != null ) { return compareBindingOperationsReport; }

      }
      List<SOAPBinding> expExtensibilityElements = expBinding.getExtensibilityElements();
      List genExtensibilityElements = genBinding.getExtensibilityElements();
      for(SOAPBinding expBindingExtension: expExtensibilityElements) {
        QName expElementType = expBindingExtension.getElementType();
        SOAPBinding genSoapBinding = getExtensionSOAPBindingforElementType(genExtensibilityElements,expElementType);
        if( genSoapBinding == null ) {
          return "No SoapBinding found matching the QName: " + expElementType.toString();
        }
        String genStyle = genSoapBinding.getStyle();
        String genTransportURI = genSoapBinding.getTransportURI();
        String expStyle = expBindingExtension.getStyle();
        String expTransportURI = expBindingExtension.getTransportURI();
        if(!expStyle.equals(genStyle) || !expTransportURI.equals(genTransportURI)) {
          return "Style and/or TransportURI do not match for SoapBinding: " + genSoapBinding.getElementType().toString();
        }
      }
      return null;
    }
    
    private SOAPBinding getExtensionSOAPBindingforElementType(List<SOAPBinding> genExtensibilityElements, QName expElementType) {
      for(SOAPBinding soapBinding : genExtensibilityElements) {
        if(expElementType.equals(soapBinding.getElementType())) {
          return soapBinding;
        }
      }
      return null;
    }
    
    private String comparePortTypes(PortType expPortType, PortType genPortType) {
      QName expQName = expPortType.getQName();
      QName genQName = genPortType.getQName();
      logger.info("exp port type name = " + expQName);
      logger.info("gen port type name = " + genQName);
      if(expQName.equals(genQName)) {
        return null;
      }
      return "Expected port name = " + expQName + "Generated Port name = " + genQName;

    }
    
    private String compareBindingOperations(BindingOperation expBindingOperation, BindingOperation genBindingOperation) {
      
      Operation expOperation = expBindingOperation.getOperation();
      String expName = expOperation.getName();
      logger.info("exp name for binding Operation = " + expName);
      Operation genOperation = genBindingOperation.getOperation();
      String genName = genOperation.getName();
      logger.info("gen name for binding Operation = " + genName);
      if(!expName.equals(genName)) {
        return "BindingOperations have different names: " + expName + " " + genName;
      }
      Input expInput = expOperation.getInput();
      Input genInput = genOperation.getInput();
      String compareInputsReport = compareInputs(expInput,genInput);
      if ( compareInputsReport != null ) { return compareInputsReport; }
      Output expOutput = expOperation.getOutput();
      Output genOutput = genOperation.getOutput();
      String compareOutputsReport = compareOutputs(expOutput,genOutput);
      if ( compareOutputsReport != null ) { return compareOutputsReport; }
      Map<String, Fault> expFaults = expOperation.getFaults();
      Map<String, Fault> genFaults = genOperation.getFaults();
      Set<String> expFaultNames = expFaults.keySet();
      for (String faultName : expFaultNames) {
        Fault genFault = genFaults.get(faultName);
        if ( genFault == null ) {
          return "No fault named " + faultName + " found in Operation: " + genOperation.getName();
        }
        String compareFaultReport = compareFaults(expFaults.get(faultName), genFault);
        if ( compareFaultReport != null ) { return compareFaultReport; }
      }
      Set<String> genFaultNames = genFaults.keySet();
      for (String faultName : genFaultNames) {
        Fault expFault = expFaults.get(faultName);
        if ( expFault == null ) {
          return "No fault named " + faultName + " found in Operation: " + expOperation.getName();
        }
      }
      return null;
    }
    
    private String compareFaults(Fault expFault, Fault genFault) {
      
      String expName = expFault.getName();
      String genName = genFault.getName();
      if ( expName != null ) {
        if( !expName.equals(genName)) {
          return "Fault names are not equal: " + expName + " " + genName;
        }
      } else if ( genName != null ) {
        return "Fault names are not equal: " + expName + " " + genName;
      }
      Message expMessage = expFault.getMessage();
      Message genMessage = genFault.getMessage();
      return compareMessages(expMessage, genMessage);

      
    }

    private String compareOutputs(Output expOutput, Output genOutput) {
      String expName = expOutput.getName();
      String genName = genOutput.getName();
      if ( expName != null ) {
        if( !expName.equals(genName)) {
          return "Output names are not equal: " + expName + " " + genName;
        }
      } else if ( genName != null ) {
        return "Output names are not equal: " + expName + " " + genName;
      }
      Message expMessage = expOutput.getMessage();
      Message genMessage = genOutput.getMessage();
      return compareMessages(expMessage,genMessage);
      
    }
    
    private String compareInputs(Input expInput, Input genInput) {
      String expName = expInput.getName();
      String genName = genInput.getName();
      logger.info("expName = " + expName + "  genName = " + genName);
      if ( expName != null ) {
        if( !expName.equals(genName)) {
          return "Input names are not equal: " + expName + " " + genName;
        }
      } else if ( genName != null ) {
        return "Input names are not equal: " + expName + " " + genName;
      }
      Message expMessage = expInput.getMessage();
      Message genMessage = genInput.getMessage();
      return compareMessages(expMessage,genMessage);
      
    }
    
    private String compareMessages(Message expectedMessage, Message generatedMessage) {
      
      Map parts = expectedMessage.getParts();
      Iterator<Map.Entry> iterator = parts.entrySet().iterator();
      while(iterator.hasNext()) {
        Map.Entry entry = iterator.next();
        String partName = entry.getKey().toString();
        logger.info("expected part name = " + partName);
        if(generatedMessage.getPart(partName) == null) {
          return "No part found for expected part name = " + partName + " in message: " + 
          generatedMessage.getQName().toString();
        } else {
          Part generatedPart = generatedMessage.getPart(partName);
          Part expectedPart = (Part)entry.getValue();
          if(!generatedPart.getElementName().equals(expectedPart.getElementName())) {
            return "The first part element name = " +expectedPart.getElementName() +
            " is not the same as the second part element name  = " + generatedPart.getElementName();
           }
        }
      }
      return null
      ;
    }
    
    
    private String validateTargetNameSpace() {
      
      String expectedNameSpace = wsdlExpected.getTargetNamespace();
      String generateNameSpace = wsdlGenerated.getTargetNamespace();
      if(StringUtil.stringNotEmpty(expectedNameSpace) && StringUtil.stringNotEmpty(generateNameSpace)) {
        if(generateNameSpace.equals(expectedNameSpace.toString())) {
          return null;
        }
      }
      return "Target namespaces are not equal: " + expectedNameSpace + " " + generateNameSpace;
      
    }
    
  }
  
}
