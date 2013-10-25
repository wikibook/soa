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

public enum YahooShoppingCatalogListingClass {
  CATALOGS("catalogs"),
  FREEOFFERS("freeoffers"),
  PAIDOFFERS("paidoffers"),
  ALL("all"),
  CATALOGS_AND_PAIDOFFERS("catalogs,paidoffers");
  
  private final String value;
  
  YahooShoppingCatalogListingClass(String val) {
    value = val;
  }
  
  public String getValue() { return value; }
  
  public static YahooShoppingCatalogListingClass fromValue(
      String val) {
    for (YahooShoppingCatalogListingClass c : YahooShoppingCatalogListingClass
        .values()) {
      if (c.value == val) {
        return c;
      }
    }
    throw new IllegalArgumentException("No such class value: " + val);
  }

}
