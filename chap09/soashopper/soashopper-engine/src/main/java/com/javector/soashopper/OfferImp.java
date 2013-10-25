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

public abstract class OfferImp {

  public abstract String getSourceSpecificOfferId();

  public abstract Store getSource();

  public abstract Picture getThumbnail();

  /**
   * @return TRUE if and only if this offer is an auction.
   */
  public abstract boolean isAuction();

  /**
   * @return the minimum allowed bid for this auction. Returns null if this item
   *         is not an auction item.
   */
  public abstract Price minimumToBid();

  /**
   * @return The price for this particular offer. If its an auction offer, then
   *         this returns the price that it takes to end the auction (e.g., on
   *         eBay this is the BuyItNow price). If its not an auction, then this
   *         is just the normal sale price exclusive of taxes and shipping.
   */
  public abstract Price getPrice();

  public abstract String getMerchantName();

  /**
   * @return The summary description for this particular offer that is provided
   *         by the underlying source.
   */
  public abstract String getSummary();

  public abstract URL getUrl();

  public abstract String getSourceSpecificProductId();

  public abstract String getDescription();

}
