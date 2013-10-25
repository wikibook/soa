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
 * {@link http://developer.yahoo.com/shopping/topcategories.html}
 */
public enum YahooShoppingTopLevelCategory {

  AUTOMOTIVE("Automotive", "Automotive"), BEAUTY_AND_FRAGRANCES("Beauty & Fragrances",
      "Beauty%20%26%20Fragrances"), CLOTHING_ACCESSORIES_AND_SHOES("Clothing, Accessories & Shoes",
      "Clothing%2C%20Accessories%20%26%20Shoes"), COMPUTERS_AND_SOFTWARE(
      "Computers & Software", "Computers%20%26%20Software"), ELECTRONICS_AND_CAMERA(
      "Electronics & Camera", "Electronics%20%26%20Camera"), FLOWERS_AND_GIFTS(
      "Flowers & Gifts", "Flowers%20%26%20Gifts"), GOURMET_FOODS("Gourmet Foods",
      "Gourmet%20Foods"), HEALTH_AND_PERSONAL_CARE("Health & Personal Care",
      "Health%20%26%20Personal%20Care"), HOME_AND_GARDEN("Home & Garden",
      "Home%20%26%20Garden"), JEWELRY_AND_WATCHES("Jewelry & Watches",
      "Jewelry%20%26%20Watches"), MUSICAL_INSTRUMENTS("Musical Instruments",
      "Musical%20Instruments"), SPORTS_AND_OUTDOORS("Sports & Outdoors",
      "Sports%20%26%20Outdoors"), TOYS_AND_BABY("Toys & Baby", "Toys%20%26%20Baby"), VIDEO_GAMES(
      "Video Games", "Video%20Games");

  private final String categoryName;
  private final String categoryId;

  YahooShoppingTopLevelCategory(String categoryName, String categoryId) {
    this.categoryId = categoryId;
    this.categoryName = categoryName;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public String getCategoryId() {
    return categoryId;
  }

  public static YahooShoppingTopLevelCategory fromCategoryName(
      String categoryName) {
    for (YahooShoppingTopLevelCategory c : YahooShoppingTopLevelCategory
        .values()) {
      if (c.categoryName == categoryName) {
        return c;
      }
    }
    throw new IllegalArgumentException("No such categoryName: " + categoryName);
  }

  public static YahooShoppingTopLevelCategory fromCategoryId(String categoryId) {
    for (YahooShoppingTopLevelCategory c : YahooShoppingTopLevelCategory
        .values()) {
      if (c.categoryId == categoryId) {
        return c;
      }
    }
    throw new IllegalArgumentException("No such categoryId: " + categoryId);
  }

}
