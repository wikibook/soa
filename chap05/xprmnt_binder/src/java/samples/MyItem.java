/*
* Copyright 2006 Javector Software LLC
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
package samples;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

//! <example xn="MyItem">
//! <c>chap05</c><s>annotations</s>
@XmlType(name = "ItemType", propOrder = {"quantity", "price"})
public class MyItem {
  
  private int quantity;
  private float price;
  private String productName;

  // need a no-arg constructor
  public MyItem() {};
  
  public MyItem(int quantity, float price, String productName) 
  throws Exception {
    if (productName == null) {
      throw new Exception("productName cannot be null");
    }
      this.productName = productName;
      this.price = price;
      this.quantity = quantity;
  }

  public float getPrice() {
    return price;
  }

  public void setPrice(float price) {
    this.price = price;
  }

  @XmlAttribute
  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

}
//! </example>
