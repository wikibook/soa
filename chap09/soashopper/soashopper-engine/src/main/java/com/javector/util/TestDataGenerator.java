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
package com.javector.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import yahoo.prods.MerchantType;

import com.example.retail.CurrencyType;
import com.example.retail.OfferType;
import com.example.retail.PriceType;
import com.example.retail.SourceType;
import com.javector.soashopper.Offer;
import com.javector.soashopper.Store;
import com.javector.soashopper.yahoo.YahooNonCatalogOfferImp;

public class TestDataGenerator {

  
  public List<OfferType> getListOfOfferTypeTestData() {
    OfferType ot = getOfferTypeTestData();
    List<OfferType> otList = new ArrayList<OfferType>();
    otList.add(ot);
    return otList;
  }
  
  public OfferType getOfferTypeTestData() {
    OfferType ot = new OfferType();
    ot.setOfferId("bogus-id-value");
    ot.setMerchantName("Bogus Merchant");
    PriceType pt = new PriceType();
    pt.setCurrencyId(CurrencyType.USD);
    pt.setValue(BigDecimal.valueOf(8.99));
    ot.setPrice(pt);
    ot.setSource(SourceType.AMAZON);
    ot.setSummary("This is a bogus offer made up for testing.");
    ot.setOfferUrl("http://bogusoffer.com");
    return ot;
  }

  public List<Offer> getListOfOfferTestData() {
    Offer o = getOfferTestData();
    List<Offer> oList = new ArrayList<Offer>();
    oList.add(o);
    return oList;
  }
  
  public Offer getOfferTestData() {
    yahoo.prods.OfferType ot = new yahoo.prods.OfferType();
    ot.setId("bogus-id-value");
    MerchantType mt = new MerchantType();
    mt.setName("Bogus Merchant");
    ot.setMerchant(mt);
    ot.setPrice(BigDecimal.valueOf(8.99));
    ot.setSummary("This is a bogus offer made up for testing.");
    ot.setUrl("http://bogusoffer.com");
    YahooNonCatalogOfferImp yo = new YahooNonCatalogOfferImp(ot);
    return new Offer(yo);
    
  }
  
}
