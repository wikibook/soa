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

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(name = "RequestOrderPort", 
    targetNamespace = "http://www.example.com/req", 
    wsdlLocation = "http://localhost:8080/oms/requestOrder?wsdl")
    public interface MyPort {
  
  @WebMethod
  @WebResult(name = "Order", targetNamespace = "http://www.example.com/oms")
  @RequestWrapper(localName = "requestOrder", 
      targetNamespace = "http://www.example.com/req", 
      className = "samples.RequestOrder")
  @ResponseWrapper(localName = "requestOrderResponse", 
      targetNamespace = "http://www.example.com/req",
      className = "samples.RequestOrderResponse")
  public OrderType requestOrder(
      @WebParam(name = "CUST_NO", targetNamespace = "http://www.example.com/req")
      String custNO,
      @WebParam(name = "PURCH_ORD_NO", targetNamespace = "http://www.example.com/req")
      String purchORDNO,
      @WebParam(name = "ccard", targetNamespace = "http://www.example.com/req")
      MyCreditCard ccard,
      @WebParam(name = "item", targetNamespace = "http://www.example.com/req")
      List<MyItem> item)
  throws InputFault
  ;
  
}
