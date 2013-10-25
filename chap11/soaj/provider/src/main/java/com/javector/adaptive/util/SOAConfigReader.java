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
package com.javector.adaptive.util;

import com.javector.adaptive.framework.interfaces.XMLMapping;
import com.javector.adaptive.util.dto.*;
import com.javector.soaj.config.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SOAConfigReader {
  
  public SOAConfigDTO parserSOAConfig(InputStream io) {
    SOAConfigDTO soaConfigDTO = new SOAConfigDTO();
    SoajConfiguration soajConfiguration;
    try {
      JAXBContext context = JAXBContext.newInstance(SoajConfiguration.class);
      soajConfiguration = (SoajConfiguration) context.createUnmarshaller().unmarshal(io);
    } catch (JAXBException e) {
      throw new SoajConfigurationRuntimeException(
          "unable to unmarshel data from file =" + io, e);
    }
    soaConfigDTO.setMappingXml(soajConfiguration.getMappingXml());
    soaConfigDTO.setUserDefinedSchemas(soajConfiguration.getUserDefinedSchemas().getUserDefinedSchema());
    List<SoajWSDL> soajWSDLs = soajConfiguration.getSoajWSDL();
    ArrayList<WsdlDTO>  wsdlDTOs = new ArrayList<WsdlDTO>();
    for (SoajWSDL wsdl : soajWSDLs) {
      WsdlDTO wsdlDTO = constructWsdlDTO(wsdl);
      wsdlDTOs.add(wsdlDTO);
    }
    soaConfigDTO.setWsdlDTOs(wsdlDTOs);
    return soaConfigDTO;
  }
  
  private WsdlDTO constructWsdlDTO(SoajWSDL soajWSDL){
    WsdlDTO wsdlDTO = new WsdlDTO();
    wsdlDTO.setWsdlNamespace(soajWSDL.getWsdlNamespace());
    wsdlDTO.setWsdlName(soajWSDL.getWsdlName());
    List<SoajService> soajService = soajWSDL.getSoajService();
    List<SoajServiceDTO> soajServiceDTOs = new ArrayList<SoajServiceDTO>();
    for (SoajService service : soajService) {
      SoajServiceDTO soajServiceDTO = new SoajServiceDTO();
      soajServiceDTO.setServiceName(service.getServiceName());
      List<SoajPort> soajPorts = service.getSoajPort();
      List<SoajPortDTO> soajPortDTOs = new ArrayList<SoajPortDTO>();
      for (SoajPort soajPort : soajPorts) {
        SoajPortDTO soajPortDTO = new SoajPortDTO();
        soajPortDTO.setEndpoint(soajPort.getEndpoint());
        soajPortDTO.setPortName(soajPort.getPortName());
        List<SoajOperation> soajOperation = soajPort.getSoajOperation();
        List<SOAJOperationDTO> soajOperationDTOs = new ArrayList<SOAJOperationDTO>();
        for (SoajOperation operation : soajOperation) {
          SOAJOperationDTO soajOperationDTO = 
            getDTOFromOpertaion(operation, wsdlDTO.getWsdlNamespace());
          soajOperationDTOs.add(soajOperationDTO);
        }
        soajPortDTO.setSoajOperationDTOs(soajOperationDTOs);
        soajPortDTOs.add(soajPortDTO);
      }
      soajServiceDTO.setSoajPortDTOs(soajPortDTOs);
      soajServiceDTOs.add(soajServiceDTO);
    }
    wsdlDTO.setSoajServiceDTOs(soajServiceDTOs);
    return wsdlDTO;
  }
  
  
  
  /**
   * Convert the XML representation of a SoajOperation into its corresponding 
   * DTO.  Note that in addition to the SoajOperation, a target namespace 
   * must be supplied.
   * 
   * @param operation
   * @param targetNS The namespace for the operation is inherited from the WSDL
   * that contains it.
   * @return
   */
  private SOAJOperationDTO getDTOFromOpertaion(
      SoajOperation operation, String targetNS) {
    
    SOAJOperationDTO operationDTO = new SOAJOperationDTO();
    operationDTO.setOperationName(operation.getOperationName());
    operationDTO = populateParamMap(operation, operationDTO);
    String returnJavaClass;
    if ( operation.getReturnMapping() != null ) {
      returnJavaClass = operation.getReturnMapping().getJavaClass();
    } else {
      returnJavaClass = void.class.getName();
    }
    QName returnType;
    QName returnTypeName;
    XmlElementType xmlElement = operation.getReturnMapping().getXmlElement();
    if (xmlElement.getEltRef() != null) {
      returnType = xmlElement.getEltRef();
       if(xmlElement.getEltRef().getLocalPart().equals("void")) {
             returnTypeName = null;
       }else
      returnTypeName=xmlElement.getEltRef();
    } else {
      returnType = xmlElement.getEltType();
      if(xmlElement.getEltType().getLocalPart().equals("void")) {
          returnTypeName = null;
      } else 
      returnTypeName =xmlElement.getEltName();
    }
    operationDTO.setReturnJavaClass(returnJavaClass);
    operationDTO.setReturnType(returnType);
    operationDTO.setReturnTypeName(returnTypeName);    // todo :hack
    JavaMethodType soajMethod = operation.getSoajMethod().getValue();
    operationDTO.setTargetServiceClassName(soajMethod.getJavaClass());
    operationDTO.setTargetServiceMethodName(soajMethod.getJavaMethod());
    operationDTO.setParamClasses(soajMethod.getParamClass());
    operationDTO.setTargetNameSpace(targetNS);
    if ( POJOMethodType.class.isInstance(soajMethod) ) {
      operationDTO.setMethodType(SOAJOperationDTO.JavaMethodTypeEnum.POJO);
      operationDTO.setClasspathReference(
          ((POJOMethodType) soajMethod).getClasspath());
    } else if ( EJB21MethodType.class.isInstance(soajMethod) ) {
      operationDTO.setMethodType(SOAJOperationDTO.JavaMethodTypeEnum.EJB21);
      EJB21MethodType m = (EJB21MethodType) soajMethod;
      operationDTO.setJndiName(m.getJndiName());
      operationDTO.setHomeInterface(m.getHomeInterface());
      operationDTO.setLocal(m.isIsLocal());
    } else if ( EJB30MethodType.class.isInstance(soajMethod) ) {
      operationDTO.setMethodType(SOAJOperationDTO.JavaMethodTypeEnum.EJB30);
      operationDTO.setJndiName(
          ((EJB30MethodType) soajMethod).getJndiName());
    } else {
      operationDTO.setMethodType(SOAJOperationDTO.JavaMethodTypeEnum.UNKNOWN);
    }
    
    return operationDTO;
  }
  
  private SOAJOperationDTO populateParamMap(SoajOperation operation, SOAJOperationDTO dto) {
    
    List<WSDL2JavaMappingType> parameterMapping = operation.getParameterMapping();
    List<XMLMapping> paramTypeMap = new ArrayList<XMLMapping>();
    for (WSDL2JavaMappingType wsdl2JavaMappingType : parameterMapping) {
      QName qnameType = null;
      QName qnameName = null;
      if (wsdl2JavaMappingType.getXmlElement().getEltRef() != null) {
        // the XmlElement is a reference to a global element
        qnameName = wsdl2JavaMappingType.getXmlElement().getEltRef();
        paramTypeMap.add(
            new XMLMapping(qnameType, qnameName, wsdl2JavaMappingType.getJavaClass()));
      } else {
        qnameType = wsdl2JavaMappingType.getXmlElement().getEltType();
        qnameName = wsdl2JavaMappingType.getXmlElement().getEltName();
        paramTypeMap.add(
            new XMLMapping(qnameType, qnameName, wsdl2JavaMappingType.getJavaClass()));
      }
    }
      dto.setParamMapping(paramTypeMap);
    return dto;
    
  }
  
  
}
