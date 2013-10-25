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

/**
 * {@link http://developer.yahoo.com/shopping/departments.html}
 */
public enum YahooShoppingDepartment {

  BARGAINS("Bargains", 47), BEAUTY("Beauty", 55), BOOKS("Books", 56), CLOTHING(
      "Clothing", 51), COMPUTERS_AND_OFFICE("Computers & Office", 57), DVD_AND_VIDEO(
      "DVD & Video", 65), ELECTRONICS("Electronics", 59), FLOWERS_AND_GIFTS(
      "Flowers & Gifts", 60), HEALTH("Health", 62), HOME_AND_GARDEN(
      "Home & Garden", 63), JEWELRY_AND_WATCHES("Jewelry & Watches", 64), MUSIC(
      "Music", 66), SPORTS_AND_OUTDOORS("Sports & Outdoors", 68), TOYS_AND_BABY(
      "Toys & Baby", 69);

  private final String name;
  private final int code;

  public String getName() {
    return name;
  }

  public int getCode() {
    return code;
  }

  YahooShoppingDepartment(String name, int code) {
    this.name = name;
    this.code = code;
  }

  public static YahooShoppingDepartment fromCode(int c) {
    for (YahooShoppingDepartment d : YahooShoppingDepartment.values()) {
      if (d.getCode() == c) {
        return d;
      }
    }
    throw new IllegalArgumentException("No such code: " + c);
  }

  public static YahooShoppingDepartment fromName(String n) {
    for (YahooShoppingDepartment d : YahooShoppingDepartment.values()) {
      if (d.name().equals(n)) {
        return d;
      }
    }
    throw new IllegalArgumentException("No such name: " + n);
  }

}
