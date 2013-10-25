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
package com.javector.soashopper.ebay;

import java.net.MalformedURLException;
import java.net.URL;

import com.javector.soashopper.OfferImp;
import com.javector.soashopper.Picture;
import com.javector.soashopper.Price;
import com.javector.soashopper.Store;
import com.javector.util.TypeConverter;

import ebay.apis.eblbasecomponents.AmountType;
import ebay.apis.eblbasecomponents.BestOfferDetailsType;
import ebay.apis.eblbasecomponents.ItemType;
import ebay.apis.eblbasecomponents.ListingDetailsType;
import ebay.apis.eblbasecomponents.ListingTypeCodeType;
import ebay.apis.eblbasecomponents.PictureDetailsType;
import ebay.apis.eblbasecomponents.SellingStatusType;
import ebay.apis.eblbasecomponents.UserType;

public class EBayOfferImp extends OfferImp {

  private ItemType delegate;
  private boolean reloaded = false;

  public EBayOfferImp(ItemType delegate) {
    this.delegate = delegate;
  }

  /**
   * Reloads the instance from eBay. This can also be used to load additional
   * detail into the offer. For example, when offers are generated from a
   * search, they lack complete detail (e.g., merchant name). Reloading using
   * the GetItem call will return an item populated with all the detail.
   */
  public void reload() {
    delegate = new EBayOfferImpFactory().getItemType(delegate.getItemID());
    reloaded = true;
  }

  /**
   * @return true if and only if the reload() method has been invoked on this
   *         instance.
   */
  public boolean isReloaded() {
    return reloaded;
  }

  @Override
  public Store getSource() {
    return Store.EBAY;
  }

  @Override
  public String getSourceSpecificOfferId() {
    return delegate.getItemID();
  }

  @Override
  public Picture getThumbnail() {

    PictureDetailsType pd = delegate.getPictureDetails();
    if (pd == null) {
      return null;
    }
    String galleryUrlString = pd.getGalleryURL();
    if (galleryUrlString == null) {
      return null;
    }
    URL galleryUrl;
    try {
      galleryUrl = new URL(galleryUrlString);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
    return new Picture(galleryUrl, null, null);

  }

  //! <example xn="EBayOfferImp_getPrice">
  //! <c>chap09</c><s>integration-layer</s>
  /*
   * If this is an auction, the BuyItNow price is returned. Otherwise, the fixed
   * price.
   * 
   * @see com.javector.soashopper.OfferImpl#getPrice()
   */
  @Override
  public Price getPrice() {
    AmountType amt = null;
    if (isAuction()) {
      amt = getBuyItNowPrice();
    } else {
      amt = delegate.getStartPrice();
      if (amt == null && delegate.getSellingStatus() != null) {
        amt = delegate.getSellingStatus().getCurrentPrice();
      }
    }
    return (new TypeConverter()).toPrice(amt);

  }
  //! </example>

  @Override
  public Price minimumToBid() {
    if (!isAuction()) {
      return null;
    }
    SellingStatusType ss = delegate.getSellingStatus();
    if (ss == null) {
      return null;
    }
    AmountType amt = ss.getMinimumToBid();
    if (amt == null) {
      amt = ss.getCurrentPrice(); // no bids yet
    }
    return (new TypeConverter()).toPrice(amt);
    // if always returning null, look at CurrentPrice instead
  }

  @Override
  public boolean isAuction() {

    // on search, if from eBay.com, then lt may be null which indicates the
    // default - CHINESE
    ListingTypeCodeType lt = delegate.getListingType();
    switch (lt) {
    case CHINESE:
      return true;
    case DUTCH:
      return true;
    case LIVE:
      return true;
    default:
      return false;
    }
  }

  protected AmountType getBuyItNowPrice() {
    AmountType bin = delegate.getBuyItNowPrice();
    if (bin == null || bin.getValue() == 0.0) {
      // Determine if its a best offer auction and calculate the buy-it-now
      // price
      BestOfferDetailsType bod = delegate.getBestOfferDetails();
      if (bod != null && bod.isBestOfferEnabled()) {
        bin = delegate.getStartPrice();
      }
    }
    return bin;
  }

  /*
   * Returns the eBay seller's user ID. However, if this Offer was instantiated
   * from a search, rather than a GetItem then, this will return null as the
   * seller ID is only populated for GetItem. To get this to return non-null in
   * such a situation, you can call the refresh() method to download the full
   * offer details from eBay.
   * 
   * @see com.javector.soashopper.OfferImpl#getMerchantName()
   */
  @Override
  public String getMerchantName() {
    UserType seller = delegate.getSeller();
    if (seller == null) {
      return null;
    }
    return seller.getUserID();

  }

  @Override
  public String getSummary() {

    String summary = delegate.getTitle().trim();
    String sub = delegate.getSubTitle().trim();
    if (summary != null && sub != null && !sub.equals("")) {
      summary += " || " + sub;
    }
    if (summary == null) {
      summary = sub;
    }
    return summary;

  }

  @Override
  public URL getUrl() {
    ListingDetailsType ld = delegate.getListingDetails();
    if (ld == null || ld.getViewItemURL() == null) {
      return null;
    }
    try {
      return new URL(ld.getViewItemURL());
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  /*
   * Returns offer's associated Product ID. However, if this Offer was
   * instantiated from a search, rather than a GetItem then, this may return
   * null as the Product ID. To get this to return non-null in such a situation,
   * you can call the refresh() method to download the full offer details from
   * eBay.
   * 
   * @see com.javector.soashopper.OfferImpl#getSourceSpecificProductId()
   */
  @Override
  public String getSourceSpecificProductId() {
    if (delegate.getProductListingDetails() == null) {
      return null;
    }
    return delegate.getProductListingDetails().getProductID();
  }

  /*
   * Returns offer's detailed description. However, if this Offer was
   * instantiated from a search, rather than a GetItem then, this may return
   * null as the Description. To get this to return non-null in such a
   * situation, you can call the refresh() method to download the full offer
   * details from eBay.
   * 
   * @see com.javector.soashopper.OfferImpl#getDescription()
   */
  @Override
  public String getDescription() {
    return delegate.getDescription();
  }

}
