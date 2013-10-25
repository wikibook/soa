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
package com.javector.soashopper.endpoint.soap;

import java.util.List;

import javax.jws.WebService;

import com.example.retail.CategoryType;
import com.example.retail.OfferType;
import com.example.retail.PriceType;
import com.javector.soashopper.services.ShopperService;
import com.soabook.soashopper.InputFault;
import com.soabook.soashopper.ShopperPort;

//! <example xn="ShopperPortImp">
//! <c>chap09</c><s>wsdl</s>
@WebService(wsdlLocation = "WEB-INF/wsdl/soashopper.wsdl", 
    endpointInterface = "com.soabook.soashopper.ShopperPort")
public class ShopperPortImp implements ShopperPort {

  public List<OfferType> offerSearch(String keywords, CategoryType category,
      PriceType lowprice, PriceType highprice) throws InputFault {
    return (new ShopperService()).offerSearch(keywords, category.toString(),
        lowprice.getCurrencyId().toString(), lowprice.getValue().doubleValue(),
        highprice.getValue().doubleValue());
  }

}
//! </example>
