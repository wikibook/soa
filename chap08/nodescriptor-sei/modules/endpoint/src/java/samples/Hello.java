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

import javax.ejb.Stateless;
import javax.jws.WebService;

//! <example xn="SIB_WITH_SEI_SIB">
//! <c>chap08</c><s>nodescriptor</s>
@WebService(endpointInterface="samples.HelloInf")
@Stateless
public class Hello {
  
  public String sayHello(String s) {
    return "Hello: " + s;
  }
  
  public String sayGoodbye(String s) {
    return "Goodbye: " + s;
  }
  
}
//! </example>  
