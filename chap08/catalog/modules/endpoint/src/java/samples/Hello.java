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

import javax.ejb.*;
import javax.jws.WebService;
import javax.xml.ws.WebServiceRef;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

//! <example xn="HELLO">
//! <c>chap08</c><s>catalog</s>
@WebService
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class Hello {
  
  @WebServiceRef(value = MyWebService.class,
      wsdlLocation="http://someplace/myService?wsdl")
    MyWeb port;
  
  public String sayHello(String s) {
    String webServiceString = port.saySomething(s); 
    return "Hello: " + s +
    System.getProperty("line.separator") + webServiceString;

  }
  
}
//! </example>  
