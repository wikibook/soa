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
package com.javector.soashopper.yahoo;

public enum YahooShoppingSortStyle {

  PRICE_ASCENDING("price_ascending"),
  PRICE_DESCENDING("price_descending"),
  USERRATING_ASCENDING("userrating_ascending"),
  USERRATING_DESCENDING("userrating_descending");
  
  private final String value;
  
  YahooShoppingSortStyle(String value) {
    this.value = value;
  }
  
  public String getValue() { return value; }
  
  public static YahooShoppingSortStyle fromValue(
      String val) {
    for (YahooShoppingSortStyle c : YahooShoppingSortStyle
        .values()) {
      if (c.value == val) {
        return c;
      }
    }
    throw new IllegalArgumentException("No such sort value: " + val);
  }

}
