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

public enum Store {

  EBAY("eBay", "logoEbay.gif"), YAHOO("Yahoo Shopping", "logoYahooShopping.gif"), AMAZON("Amazon", "logoAmazon.gif");

  private final String name;
  private final URL logo;

  /**
   * @param name
   *          The name of the online store.
   * @param logoPath
   *          The path of the online store's logo on the classpath.
   */
  Store(String name, String logoPath) {
    this.name = name;
    this.logo = Thread.currentThread().getContextClassLoader().getResource(logoPath);
    if (this.name == null) {
      throw new IllegalArgumentException("Source's name cannot be null.");
    }
    if (this.logo == null) {
      throw new RuntimeException(
          "The specified logo cannot be located on the classpath: " + logoPath);
    }
  }

  public String getName() {
    return name;
  }

  public URL getLogo() {
    return logo;
  }
}
