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

//! <example xn="GetNewOrdersProvider">
//! <c>chap03</c><s>rest-get</s>
import javax.xml.transform.Source;
import javax.xml.ws.BindingType;
import javax.xml.ws.Provider;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.http.HTTPException;

@WebServiceProvider
@BindingType(value=HTTPBinding.HTTP_BINDING)
public class GetNewOrdersProvider implements Provider<Source> {
  
  public Source invoke(Source xml) {
    OrderManager om = new OrderManager();
    try {
      return om.getNewOrders();
    } catch (Throwable t) {
      t.printStackTrace();
      throw new HTTPException(500);
    }
  }
  
}
//! </example>

