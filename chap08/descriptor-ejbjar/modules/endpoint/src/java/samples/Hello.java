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

import javax.annotation.Resource;
import javax.ejb.*;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceRef;

//! <example xn="EJB_JAR_HELLO">
//! <c>chap08</c><s>descriptor</s>
@WebService
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class Hello {
  
  @WebServiceRef(value = MyWebService.class,
    wsdlLocation="http://someplace/myService?wsdl")
  MyWeb port;
  
  @EJB
  Goodbye goodbye;
    
  @Resource(name="myString")
  String injectedString = "undefined"; 
  
  public String sayHello(String s) {
    String webServiceString = port.saySomething(s); 
    String goodbyeString = goodbye.sayGoodbye(s);
    return "Hello: " + s + "[injectedString: " + injectedString + "]" +
    System.getProperty("line.separator") + webServiceString + 
    System.getProperty("line.separator") + goodbyeString;
  }
  
}
//! </example>
