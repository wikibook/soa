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

public class Shopper {

  /**
   * @param src
   *          The online store to search.
   * @param keywords
   *          The keywords to search for. If category is null, then keywords
   *          must not be null.
   * @param category
   *          The category to search in (e.g., Computers, Movies)
   * @param lowprice
   *          The low end of a price range to search. If null, then the is no
   *          lower boundary.
   * @param highprice
   *          The high end of a price range to search. If null, then there is
   *          not upper boundary.
   * @return A list of offers meeting the search criteria.
   */
  public List<Offer> offerSearch(Store src, String keywords, Category category,
      Price lowprice, Price highprice) {

    if (src == null || (keywords == null && category == null)) {
      throw new IllegalArgumentException(
          "Either keywords or category must be non-null and src may not be null.");
    }
    if (src != Store.YAHOO && src != Store.EBAY && src != Store.AMAZON) {
      throw new RuntimeException("Source: " + src.name() + " is not supported.");
    }
    ShopperImp service = ShopperImp.newShopperImp(src);
    return service.offerSearch(keywords, category, lowprice, highprice);

  }

  //! <example xn="Shopper_offerSearch">
  //! <c>chap09</c><s>integration-layer</s>
  /**
   * Search all the online stores.
   * 
   * @param keywords
   *          The keywords to search for. If category is null, then keywords
   *          must not be null.
   * @param category
   *          The category to search in (e.g., Computers, Movies)
   * @param lowprice
   *          The low end of a price range to search. If null, then the is no
   *          lower boundary.
   * @param highprice
   *          The high end of a price range to search. If null, then there is
   *          not upper boundary.
   * @return A list of offers meeting the search criteria.
   */
  public List<Offer> offerSearch(String keywords, Category category,
      Price lowprice, Price highprice) {

    ShopperImp yahooSvc = ShopperImp.newShopperImp(Store.YAHOO);
    ShopperImp ebaySvc = ShopperImp.newShopperImp(Store.EBAY);
    ShopperImp amazonSvc = ShopperImp.newShopperImp(Store.AMAZON);
    List<Offer> offers = yahooSvc.offerSearch(keywords, category, lowprice,
        highprice);
    offers.addAll(ebaySvc.offerSearch(keywords, category, lowprice, highprice));
    offers.addAll(amazonSvc
        .offerSearch(keywords, category, lowprice, highprice));
    return offers;

  }
  //! </example>

}
