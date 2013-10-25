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
import java.util.List;

import com.javector.soashopper.Offer;
import com.javector.soashopper.Picture;
import com.javector.soashopper.Price;
import com.javector.soashopper.ProductImp;
import com.javector.soashopper.Store;
import com.javector.util.TypeConverter;

import yahoo.prods.CatalogType;
import yahoo.prods.ThumbnailType;

public class YahooProductImpl extends ProductImp {

  private CatalogType delegate;

  public YahooProductImpl(CatalogType delegate) {
    this.delegate = delegate;
  }

  @Override
  public String getSourceSpecificId() {
    return delegate.getId().toString();
  }
  
  @Override
  public Store getSource() {
    return Store.YAHOO;
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
  public String getSummary() {
    return delegate.getSummary();
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
  public List<Offer> getSourceSpecificOffers() {
    // TODO Auto-generated method stub
    return null;
  }

}
