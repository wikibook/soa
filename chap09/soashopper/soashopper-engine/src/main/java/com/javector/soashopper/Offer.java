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

/**
 * Offer is specific thing offered by a specific seller (merchant) that you can
 * buy from an online source. (e.g., XYZ Corp. Television model Z71W2.
 * Refurbished and sold by Billy's TVs).
 */
public class Offer {

  private OfferImp delegate;

  public Offer(Store store, String id) throws ItemNotFoundException {

    OfferImpFactory fac = OfferImpFactory.newItemFactory(store);
    this.delegate = fac.getOffer(id);
    if (this.delegate == null) {
      throw new ItemNotFoundException("Store: " + store.getName() + "  ID: "
          + id);
    }

  }
  
  public Offer(OfferImp delegate) {
    this.delegate = delegate;
  }

  /**
   * @return an ID that uniquely identifies the offer within the underlying
   *         source system. Returns null if the underlying source does not
   *         provide an ID for the offer.
   */
  public String getSourceSpecificOfferId() {
    return delegate.getSourceSpecificOfferId();
  }

  /**
   * @return an ID that uniquely identifies the product for sale in this offer
   *         within the underlying source system. Not all offers are associated
   *         with a product (e.g., one of a kind items). Returns null if there
   *         is no associated product.
   */
  public String getSourceSpecificProductId() {
    return delegate.getSourceSpecificProductId();
  }

  /**
   * @return the source of the offer (e.g., Yahoo Shopping, eBay)
   */
  public Store getSource() {
    return delegate.getSource();
  }

  /**
   * @return a small image of the product being offered that is suitable for use
   *         as a thumbnail. Returns null if no such image is available.
   */
  public Picture getThumbnail() {
    return delegate.getThumbnail();
  }

  //! <example xn="Offer_getPrice">
  //! <c>chap09</c><s>integration-layer</s>
  /**
   * @return The fixed price if this is not an auction, or else the minimum bid
   *         allowed. Returns null if the offer is no longer available or if the
   *         price cannot be determined by SOAShopper.
   */
  public Price getPrice() {
    if (delegate.isAuction()) {
      return delegate.minimumToBid();
    }
    return delegate.getPrice();
  }

  //! </example>
  
  public String getMerchantName() {
    return delegate.getMerchantName();
  }

  /**
   * @return a short description of the offer.
   */
  public String getSummary() {
    return delegate.getSummary();
  }

  /**
   * Warning: This method may result in the overhead of an additional call to
   * the online service providing the offer. So, if you use this method
   * repeatedly inside a loop, it can have a serious impact on performance. As a
   * general rule, when SOAShopper does a search of online stores it does not
   * request the detailed information because of the performance overhead.
   * Details are usually only requested on a per item basis.
   * 
   * @return A detailed description of the offer. May be quite large and contain
   *         HTML markup.
   */
  public String getDescription() {
    return delegate.getDescription();
  }

  public URL getUrl() {
    return delegate.getUrl();
  }

}
