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
package com.javector.soashopper;

import java.util.List;

import com.javector.soashopper.amazon.AmazonShopperImp;
import com.javector.soashopper.ebay.EBayShopperImp;
import com.javector.soashopper.yahoo.YahooShopperImp;

/**
 * Defines the API of common services that SOAShopper implements (to the extent
 * possible) for all the online stores.
 */
//! <example xn="ShopperImp">
//! <c>chap09</c><s>integration-layer</s>
public abstract class ShopperImp {

  public static ShopperImp newShopperImp(Store src) {
    if (src == null) {
      throw new IllegalArgumentException("src may not be null.");
    }
    switch (src) {
    case YAHOO:
      return new YahooShopperImp(ShopperCredentials.getYahooAppID());
    case EBAY:
      return new EBayShopperImp(EBayShopperImp.EBAY_PRODUCTION_SERVER,
          EBayShopperImp.SITE_ID_US, ShopperCredentials.getEBayAppID());
    case AMAZON:
      return new AmazonShopperImp(ShopperCredentials.getAmazonAccessKeyID());
    default:
      throw new RuntimeException("Unknown source: " + src.getName());
    }
  }

  public abstract List<Offer> offerSearch(String keywords, Category category,
      Price lowprice, Price highprice);
  
  //! </example>

}
