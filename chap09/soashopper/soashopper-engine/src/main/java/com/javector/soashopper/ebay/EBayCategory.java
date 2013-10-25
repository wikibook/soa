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

public enum EBayCategory {

  COMPUTERS ("58058", "58058", "1", "Computers & Networking"),
  CELLPHONES ("15032", "15032", "1", "Cell Phones & PDAs"),
  MOVIES ("11232", "11232", "1", "DVDs & Movies");

  private final String categoryId;
  private final String parentCategoryId;
  private final String categoryLevel;
  private final String categoryName;
  
  EBayCategory(String categoryId, String parentCategoryId, String categoryLevel, String categoryName) {
    this.categoryId = categoryId;
    this.parentCategoryId = parentCategoryId;
    this.categoryLevel = categoryLevel;
    this.categoryName = categoryName;
  }

  public String getCategoryId() {
    return categoryId;
  }

  public String getCategoryLevel() {
    return categoryLevel;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public String getParentCategoryId() {
    return parentCategoryId;
  }
  
}
