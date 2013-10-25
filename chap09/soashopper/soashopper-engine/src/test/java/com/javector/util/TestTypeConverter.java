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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.math.BigInteger;
import java.util.Currency;

import com.example.retail.PriceType;
import com.javector.soashopper.Price;

public class TestTypeConverter extends TestCase {

  public static Test suite() {
    return new TestSuite(TestTypeConverter.class);
  }

  private TypeConverter tc;

  public void setUp() {
    tc = new TypeConverter();
  }

  public void testToPriceType() {
    Price p = new Price(Currency.getInstance("USD"), Double.valueOf(18.88));
    PriceType pt = tc.toPriceType(p);
    assertEquals(pt.getValue().doubleValue(), p.getValue());
    assertEquals(pt.getCurrencyId().toString(), p.getCurrencyID().getCurrencyCode());
  }

  public void testToPrice() {
    com.amazon.webservices.awsecommerceservice._2007_02_22.Price p =
      new com.amazon.webservices.awsecommerceservice._2007_02_22.Price();
    p.setAmount(BigInteger.valueOf(1888));
    p.setCurrencyCode("USD");
    p.setFormattedPrice("$18.88");
    Price price = tc.toPrice(p);
    assertEquals(price.getValue(), p.getAmount().doubleValue()/100);
    assertEquals(price.getCurrencyID().getCurrencyCode(), p.getCurrencyCode());
    
  }
  
}
