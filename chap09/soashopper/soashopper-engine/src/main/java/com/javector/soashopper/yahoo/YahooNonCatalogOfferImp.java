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

import java.net.MalformedURLException;
import java.net.URL;

import yahoo.prods.MerchantType;
import yahoo.prods.OfferType;
import yahoo.prods.ThumbnailType;

import com.javector.soashopper.Picture;
import com.javector.soashopper.Price;
import com.javector.util.TypeConverter;

/**
 * Yahoo Shopping Offer instances are items for sale by merchants. They do not
 * necessarily correspond to defined products in the Yahoo Shopping catalog
 * (i.e., Catalog Listings). Catalog Listings are aggregations of offers. This
 * class is a wrapper for offers returned from a Yahoo Shopping product search.
 * Such offers do not have any product (Catalog Listings) associated with them.
 * Note that a Yahoo Shopping product search returns Catalog Listings as well as
 * Offers. However, this class is not used to wrap Catalog Listings nor the
 * Offers associated with a Catalog Listing. Offers that are associated with a
 * Catalog Listing are represented using {@link YahooCatalogOfferImp}.
 */
public class YahooNonCatalogOfferImp extends YahooOfferImp {

  private OfferType delegate;

  public YahooNonCatalogOfferImp(OfferType delegate) {
    this.delegate = delegate;
  }

  @Override
  public Picture getThumbnail() {

    ThumbnailType tt = delegate.getThumbnail();
    if (tt == null) {
      return null;
    }
    String thumbUrlString = tt.getUrl();
    URL thumbURL;
    try {
      thumbURL = new URL(thumbUrlString);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
    TypeConverter tc = new TypeConverter();
    return new Picture(thumbURL, tc.toInteger(tt.getWidth()), tc.toInteger(tt
        .getHeight()));

  }

  @Override
  public Price getPrice() {
    return (new TypeConverter()).toUSDPrice(delegate.getPrice());
  }

  /*
   * Yahoo Shopping doesn't support auctions.
   * 
   * @see com.javector.soashopper.OfferImpl#isAuction()
   */
  @Override
  public boolean isAuction() {
    return false;
  }

  /*
   * Yahoo Shopping doesn't support auctions.
   * 
   * @see com.javector.soashopper.OfferImpl#minimumToBid()
   */
  @Override
  public Price minimumToBid() {
    return null;
  }

  @Override
  public String getMerchantName() {
    MerchantType merchant = delegate.getMerchant();
    if (merchant == null) {
      return null;
    }
    return merchant.getName();
  }

  @Override
  public String getSummary() {
	  String summary =  "";
	  String productName = delegate.getProductName();
	  String productSummary = delegate.getSummary();
	  if (productName != null) { summary += productName; }
	  if (productSummary != null) { 
		  if (summary.equals("")) {
			  summary = productSummary;
		  } else {
			  summary += " - " + productSummary;
		  }
	  }
	  return summary;
  }

  @Override
  public URL getUrl() {
    String url = delegate.getUrl();
    if (url == null) {
      return null;
    }
    try {
      return new URL(url);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  /*
   * Always returns null. This type of Yahoo offer implementation does not have
   * a corresponding product in the catalog and therefore does not have a
   * product ID.
   * 
   * @see com.javector.soashopper.OfferImpl#getSourceSpecificProductId()
   */
  @Override
  public String getSourceSpecificProductId() {
    return null;
  }

  /*
   * Offers that are not aggregated as Catalog Listings do not have detailed
   * descriptions. Hence, this method alwyas returns <code>null</code>.
   * 
   * @see com.javector.soashopper.OfferImpl#getDescription()
   */
  @Override
  public String getDescription() {
    return null;
  }

}
