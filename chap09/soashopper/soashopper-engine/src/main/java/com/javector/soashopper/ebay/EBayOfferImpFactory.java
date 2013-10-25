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

import com.javector.soashopper.OfferImp;
import com.javector.soashopper.OfferImpFactory;
import com.javector.soashopper.ShopperCredentials;

import ebay.apis.eblbasecomponents.ItemType;

public class EBayOfferImpFactory extends OfferImpFactory {
 
  private EBayShopperImp eBay;
  
  public EBayOfferImpFactory() {
    eBay = new EBayShopperImp(EBayShopperImp.EBAY_PRODUCTION_SERVER,
        EBayShopperImp.SITE_ID_US, ShopperCredentials.getEBayAppID());
  }
  
  public ItemType getItemType(String storeSpecificItemId) {
    return eBay.getItem(storeSpecificItemId);
  } 
  
  @Override
  public OfferImp getOffer(String storeSpecificItemId) {
    return new EBayOfferImp(eBay.getItem(storeSpecificItemId));
  }

}
