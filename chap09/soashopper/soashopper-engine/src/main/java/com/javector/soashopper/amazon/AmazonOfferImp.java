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
package com.javector.soashopper.amazon;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.amazon.webservices.awsecommerceservice._2007_02_22.Image;
import com.amazon.webservices.awsecommerceservice._2007_02_22.Item;
import com.amazon.webservices.awsecommerceservice._2007_02_22.ItemAttributes;
import com.amazon.webservices.awsecommerceservice._2007_02_22.Merchant;
import com.amazon.webservices.awsecommerceservice._2007_02_22.Offer;
import com.amazon.webservices.awsecommerceservice._2007_02_22.OfferAttributes;
import com.amazon.webservices.awsecommerceservice._2007_02_22.OfferListing;
import com.amazon.webservices.awsecommerceservice._2007_02_22.Seller;
import com.javector.soashopper.OfferImp;
import com.javector.soashopper.Picture;
import com.javector.soashopper.Price;
import com.javector.soashopper.Store;
import com.javector.util.TypeConverter;

public class AmazonOfferImp extends OfferImp {

  private Offer delegate;
  private Item product;

  public AmazonOfferImp(Item product, Offer delegate) {
    this.delegate = delegate;
    this.product = product;
  }

  /* (non-Javadoc)
   * Construct the description from the title plus list of features at the 
   * product level.
   */
  @Override
  public String getDescription() {
    String desc = "";
    ItemAttributes atts = product.getItemAttributes();
    if (atts != null) {
      desc += atts.getTitle(); // ItemAttributes Response Group
      if (!desc.equals("")) {
        desc += " : ";
      }
      List<String> features = atts.getFeature(); // ItemAttributes Response Group
      if (features != null) {
        for (String feature : features) {
          desc += " / " + feature;
        }
      }
    }
    return desc;
  }

  @Override
  public String getMerchantName() {
    String retVal = null;
    Merchant m = delegate.getMerchant();
    if ( m != null && m.getName() != null ) {
      retVal = m.getName(); // OfferFull Response Group
    } else {
      Seller s = delegate.getSeller();
      if ( s!= null ) {
        if ( s.getSellerName() != null ) {
          retVal = s.getSellerName();
        } else if (s.getNickname() != null) {
          retVal = s.getNickname();
        }
      }
    }
    return retVal;

  }

  @Override
  public Price getPrice() {
    List<OfferListing> olist = delegate.getOfferListing();
    if (olist == null || olist.isEmpty() | olist.get(0) == null) { return null; }
    com.amazon.webservices.awsecommerceservice._2007_02_22.Price price =
      olist.get(0).getPrice();
    return (new TypeConverter()).toPrice(price);
  }

  @Override
  public Store getSource() {
    return Store.AMAZON;
  }

  @Override
  public String getSourceSpecificOfferId() {
    List<OfferListing> olist = delegate.getOfferListing();
    if (olist == null || olist.isEmpty() | olist.get(0) == null) { return null; }
    return olist.get(0).getOfferListingId();
  }

  @Override
  public String getSourceSpecificProductId() {
    return product.getASIN();
  }

  @Override
  public String getSummary() {
    ItemAttributes atts = product.getItemAttributes();
    if (atts == null) { return null; }
    String title = atts.getTitle();
    String merchantName = getMerchantName();
    String summary = title;
    if (merchantName != null && !merchantName.equals("")) {
      summary += " (Merchant: "+merchantName+")";
    }
    OfferAttributes oAtts = delegate.getOfferAttributes();
    if (oAtts == null ) {
      return summary;
    }
    String condition = oAtts.getCondition();
    if ( condition == null || condition.equals("")) { return summary; }
    return summary + " (Condition: "+condition+")";
  }

  @Override
  public Picture getThumbnail() {
    Picture pic = null;
    Image smImage = product.getSmallImage();
    if (smImage != null) {
      pic = (new TypeConverter()).toPicture(smImage);
    }
    return pic;
  }

  @Override
  public URL getUrl() {
    try {
      return new URL(product.getDetailPageURL());
    } catch (MalformedURLException e) {
      throw new RuntimeException("Amazon returned bad item URL.", e);
    }
  }

  @Override
  public boolean isAuction() {
    return false;    
  }

  @Override
  public Price minimumToBid() {
    return null;
  }

}
