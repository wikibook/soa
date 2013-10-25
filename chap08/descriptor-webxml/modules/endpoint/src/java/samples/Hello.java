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
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

//! <example xn="WEB_XML_HELLO">
//! <c>chap08</c><s>descriptor</s>
@HandlerChain(file="myhandler.xml")
@WebService
public class Hello {
  
  @Resource
  WebServiceContext context;
  
  public String sayHello(String s) {
    String appendString = 
      (String) context.getMessageContext().get(HelloHandler.APPEND_STRING);
    return "Hello: " + s + "[appended by handler: " + appendString + "]";
  }
  
}
//! </example>