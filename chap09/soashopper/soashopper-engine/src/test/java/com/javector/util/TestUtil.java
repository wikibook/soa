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
import java.util.Currency;
import com.javector.soashopper.Price;

public class TestUtil extends TestCase {

  public static Test suite() {
    return new TestSuite(TestUtil.class);
  }

  private Util util;

  public void setUp() {
    util = new Util();
  }

  public void testFloor() {
    assertEquals("1234.5678900", util.floor(Double.valueOf("1234.56789"), 7));
    assertEquals("1234.5678", util.floor(Double.valueOf("1234.56789"), 4));
    assertEquals("1234.56", util.floor(Double.valueOf("1234.56789"), 2));
    assertEquals("1234", util.floor(Double.valueOf("1234.56789"), 0));
    assertEquals("0.050", util.floor(Double.valueOf(".05"), 3));
    assertEquals("0.00", util.floor(Double.valueOf(".002"), 2));
  }

  public void testCeling() {
    assertEquals("1234.5678900", util.ceiling(Double.valueOf("1234.56789"), 7));
    assertEquals("1234.5679", util.ceiling(Double.valueOf("1234.56789"), 4));
    assertEquals("1234.57", util.ceiling(Double.valueOf("1234.56789"), 2));
    assertEquals("1235", util.ceiling(Double.valueOf("1234.56789"), 0));
    assertEquals("0.050", util.ceiling(Double.valueOf(".05"), 3));
    assertEquals("0.01", util.ceiling(Double.valueOf(".002"), 2));
  }
  
  public void testToStringPrice() {
    assertEquals("USD 19.59", util.toString(new Price(Currency.getInstance("USD"), 19.59)));
  }
}
