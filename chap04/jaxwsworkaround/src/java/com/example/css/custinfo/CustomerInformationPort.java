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
//! <example xn="JAX_WS_GENERATED_INTERFACE">
//! <c>chap04</c><s>jaxwsworkaround</s>
package com.example.css.custinfo;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import com.example.corp.AddressType;
import com.example.css.CustomerHistoryEntriesType;
import com.example.css.custinfo.CustomerInformationPort;

@WebService(name = "CustomerInformationPort", 
    targetNamespace = "http://www.example.com/css/custinfo", 
    wsdlLocation = " ... /CustomerInformation.wsdl")
public interface CustomerInformationPort {
  
  @WebMethod
  @WebResult(name = "address", 
      targetNamespace = "http://www.example.com/css/custinfo")
  @RequestWrapper(localName = "getAddress", 
      targetNamespace = "http://www.example.com/css/custinfo", 
      className = "com.example.css.custinfo.GetAddress")
  @ResponseWrapper(localName = "getAddressResponse", 
      targetNamespace = "http://www.example.com/css/custinfo", 
      className = "com.example.css.custinfo.GetAddressResponse")
  public AddressType getAddress(
      @WebParam(name = "custId", 
          targetNamespace = "http://www.example.com/css/custinfo")
      String custId);

  @WebMethod
  @WebResult(name = "history", 
      targetNamespace = "http://www.example.com/css/custinfo")
  @RequestWrapper(localName = "getCustomerHistory", 
      targetNamespace = "http://www.example.com/css/custinfo", 
      className = "com.example.css.custinfo.GetCustomerHistory")
  @ResponseWrapper(localName = "getCustomerHistoryResponse", 
      targetNamespace = "http://www.example.com/css/custinfo", 
      className = "com.example.css.custinfo.GetCustomerHistoryResponse")
  public CustomerHistoryEntriesType getCustomerHistory(
      @WebParam(name = "custId", 
          targetNamespace = "http://www.example.com/css/custinfo")
      String custId);
  
}
//! </example>

