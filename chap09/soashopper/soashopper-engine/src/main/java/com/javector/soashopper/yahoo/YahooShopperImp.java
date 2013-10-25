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
package com.javector.soashopper.yahoo;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.javector.soashopper.Category;
import com.javector.soashopper.Offer;
import com.javector.soashopper.Price;
import com.javector.soashopper.ShopperImp;
import com.javector.soashopper.yahoo.api.YahooRESTInterface;
import com.javector.util.TypeConverter;

import yahoo.merchoffers.CatalogListing;
import yahoo.merchoffers.OfferType;
import yahoo.prods.CatalogType;
import yahoo.prods.ProductSearch;
import yahoo.prods.ProductType;
import yahoo.prods.ProductsType;

public class YahooShopperImp extends ShopperImp {

  private Logger log = Logger.getLogger(YahooShopperImp.class);

  protected YahooRESTInterface port;

  /**
   * @param appid
   *          The Yahoo Shopping application ID that uniquely identifies the
   *          application making request. You must request it from Yahoo
   *          Shopping. See
   *          {@link http://api.search.yahoo.com/webservices/register_application}.
   */
  public YahooShopperImp(String appid) {
    port = new YahooRESTInterface(appid);
  }

  @Override
  public List<Offer> offerSearch(String keywords, Category category,
      Price lowprice, Price highprice) {

    TypeConverter tc = new TypeConverter();
    YahooShoppingTopLevelCategory yahooCat = null;
    YahooShoppingDepartment yahooDept = null;
    if (category != null) {
      yahooCat = tc.toYahooShoppingTopLevelCategory(category);
    }
    if (yahooCat == null) { // try to assign a department
      yahooDept = tc.toYahooShoppingDepartment(category);
    }
    log.debug("invoking productSearch(" + keywords + ", " + yahooCat + ", "
        + null + ", " + yahooDept + ", " + tc.toDouble(highprice) + ", "
        + tc.toDouble(lowprice) + ", " + null + ", " + null + ", " + null
        + ", " + Integer.valueOf(50) + ", " + null + ", " + null + ", " + null
        + ", " + null + ")");
    ProductSearch productSearch = port.productSearch(keywords, yahooCat, null,
        yahooDept, tc.toDouble(highprice), tc.toDouble(lowprice), null, null,
        null, Integer.valueOf(50), null, null, null, null);
    ProductsType pt = productSearch.getProducts();
    if (pt == null) {
      log.debug("productSearch returned 0 products.");
      return null;
    }
    log.debug("productSearch returned " + pt.getProduct().size() + " products.");
    List<Offer> retVal = new ArrayList<Offer>();
    for (ProductType p : pt.getProduct()) {
      CatalogType cat = p.getCatalog();
      if (cat != null) { // process a catalog listing
        CatalogListing cl = port.getCatalogListing(cat.getId().toString(),
            null, null, null, null, null);
        if (cl != null) {
          for (OfferType o : cl.getOffer()) {
            retVal.add(new Offer(new YahooCatalogOfferImp(o, cat)));
          }
        }
      } else { // process an offer
        retVal.add(new Offer(new YahooNonCatalogOfferImp(p.getOffer())));
      }
    }
    return retVal;
  }

}
