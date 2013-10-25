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
package com.javector.soashopper.services;

import java.util.ArrayList;
import java.util.List;

import com.example.retail.OfferType;
import com.javector.soashopper.Category;
import com.javector.soashopper.Offer;
import com.javector.soashopper.Price;
import com.javector.soashopper.Shopper;
import com.javector.util.TypeConverter;

public class ShopperService {

  // ! <example xn="ShopperService_offerSearch">
  // ! <c>chap09</c><s>serviceimp</s>
  public List<OfferType> offerSearch(String keywords, String categoryId,
      String currencyId, Double lowpriceVal, Double highpriceVal) {

    // convert from SOAP/REST request types to SOAShopper API types
    TypeConverter tc = new TypeConverter();
    Category category = tc.toCategory(categoryId);
    Price lowprice = tc.toPrice(currencyId, lowpriceVal);
    Price highprice = tc.toPrice(currencyId, highpriceVal);
    // invoke the SOAShopper API
    Shopper shoppingService = new Shopper();
    List<Offer> offerList = shoppingService.offerSearch(keywords, category,
        lowprice, highprice);
    // convert from SOAShopper API return type to SOAP/REST response type
    ArrayList<OfferType> offerTypeList = new ArrayList<OfferType>();
    for (Offer o : offerList) {
      offerTypeList.add(tc.toOfferType(o));
    }
    return offerTypeList;

  }
  // ! </example>

}
