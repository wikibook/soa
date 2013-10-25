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

import java.util.Currency;
import java.util.List;

import org.apache.log4j.Logger;

import com.javector.util.Util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestSOAShopperService extends TestCase {

  Logger log = Logger.getLogger(TestSOAShopperService.class);

  public static Test suite() {
    return new TestSuite(TestSOAShopperService.class);
  }

  private Shopper service;

  public TestSOAShopperService(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    service = new Shopper();
  }

  public void testAmazonOfferSearch() {
    runOfferSearch(Store.AMAZON);
    assertTrue(true);
  }

  public void testYahooOfferSearch() {
    runOfferSearch(Store.YAHOO);
    assertTrue(true);
  }

  public void testEBayOfferSearch() {
    runOfferSearch(Store.EBAY);
    assertTrue(true);
  }

  private void runOfferSearch(Store s) {
    try {
      Util util = new Util();
      List<Offer> offers;
      // Cellphone
      offers = service.offerSearch(s, 
          "razr", 
          Category.CELLPHONES,
          new Price(Currency.getInstance("USD"), 200.00), 
          new Price(Currency.getInstance("USD"), 300.00)); 
      for (Offer o : offers) {
        log.info(util.toString(o));
      }
      // Computer
      offers = service.offerSearch(s, 
          "lenovo t60", 
          Category.COMPUTERS,
          new Price(Currency.getInstance("USD"), 800.00), 
          new Price(Currency.getInstance("USD"), 1200.00)); 
      for (Offer o : offers) {
        log.info(util.toString(o));
      }
      // Movies
      offers = service.offerSearch(s, 
          "titanic", 
          Category.MOVIES,
          null, 
          null); 
      for (Offer o : offers) {
        log.info(util.toString(o));
      }
    } catch (RuntimeException re) {
      re.printStackTrace();
      throw re;
    }
  }
}
