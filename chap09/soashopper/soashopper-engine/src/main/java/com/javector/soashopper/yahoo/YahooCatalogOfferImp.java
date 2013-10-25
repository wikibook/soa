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

import yahoo.prods.CatalogType;
import yahoo.prods.ThumbnailType;
import yahoo.merchoffers.MerchantType;
import yahoo.merchoffers.OfferType;

import com.javector.soashopper.Picture;
import com.javector.soashopper.Price;
import com.javector.util.TypeConverter;

/**
 * This is a wrapper for offers returned from a Yahoo Shopping Product search
 * that have been aggregated into a Catalog Listing. Such offers always have a
 * product (Catalog Listing) associated with them that was obtained during the
 * prduct search and is passed to the constructor here as an instance of
 * {@link CatalogType}
 */
public class YahooCatalogOfferImp extends YahooOfferImp {

  private OfferType delegate;
  private CatalogType product;

  public YahooCatalogOfferImp(OfferType delegate, CatalogType product) {
    this.delegate = delegate;
    this.product = product;
  }

  /*
   * Always returns <code>null</code> since Yahoo Shopping Offers do not have
   * unique identifiers other than the URL.
   * 
   * @see com.javector.soashopper.ItemImpl#getSourceId()
   */
  @Override
  public String getSourceSpecificOfferId() {
    return null;
  }

  @Override
  public Picture getThumbnail() {

    ThumbnailType tt = product.getThumbnail();
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
    return (new TypeConverter()).toUSDPrice(delegate.getBasePrice());
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
	  String productName = product.getProductName();
	  String productSummary = product.getSummary();
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

  @Override
  public String getSourceSpecificProductId() {
    return product.getId().toString();
  }

  protected CatalogType getProduct() {
    return product;
  }

  @Override
  public String getDescription() {
    return product.getDescription();
  }

}
