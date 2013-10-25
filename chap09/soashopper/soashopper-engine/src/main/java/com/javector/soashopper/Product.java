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

import java.net.URL;
import java.util.List;

/**
 * Product is a type of thing can be sold (e.g., XYZ Corp. Television model
 * Z71W2). A product exists independently of any particular {@link Offer}.
 */
public class Product {

  private ProductImp delegate;

  protected Product(ProductImp delegate) {
    this.delegate = delegate;
  }

  /**
   * @return an ID that uniquely identifies the product within the underlying
   *         source system's catalog.
   */
  public String getSourceSpecificId() {
    return delegate.getSourceSpecificId();
  }

  /**
   * @return the source of the prouct (e.g., Yahoo Shopping, eBay)
   */
  public Store getSource() {
    return delegate.getSource();
  }

  /**
   * @return a small image of the product that is suitable for use as a
   *         thumbnail. Returns null if no such image is available.
   */
  public Picture getThumbnail() {
    return delegate.getThumbnail();
  }

  /**
   * @return a short description of the product.
   */
  public String getSummary() {
    return delegate.getSummary();
  }

  /**
   * @return the URL pointing to the product's page in the online source
   *         catalog.
   */
  public URL getUrl() {
    return delegate.getUrl();
  }

  /**
   * @return a list of offers for this product that are available from the
   *         source associated with this product.
   */
  public List<Offer> getSourceSpecificOffers() {
    return delegate.getSourceSpecificOffers();
  }
}
