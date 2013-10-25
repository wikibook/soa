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
package com.javector.soashopper.amazon;

public enum AmazonStoreCategory {

  CELLPHONES("Cell Phones", "Wireless", "301187"),
  COMPUTERS_AND_PC_HARDWARE("Computers & PC Hardware", "PCHardware", "541966"),
  MOVIES("Movies", "Photo", "139452");
  
  private final String categoryName;
  private final String searchIndex;
  private final String browseNode;
  

  AmazonStoreCategory(String categoryName, String searchIndex, 
      String browseNode) {
    this.categoryName = categoryName;
    this.searchIndex = searchIndex;
    this.browseNode = browseNode;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public String getSearchIndex() {
    return searchIndex;
  }
  
  public String getBrowseNode() {
    return browseNode;
  }

  public static AmazonStoreCategory fromCategoryName(String categoryName) {
    for (AmazonStoreCategory c : AmazonStoreCategory.values()) {
      if (c.categoryName == categoryName) {
        return c;
      }
    }
    throw new IllegalArgumentException("No such categoryName: " + categoryName);
  }

}
