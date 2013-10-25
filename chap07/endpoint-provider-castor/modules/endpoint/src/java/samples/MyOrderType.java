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

import java.util.List;

public class MyOrderType {
  
  private String OrderKey;
  private MyHeader OrderHeader;
  private List<MyItem> item;
  private MyCreditCard OrderCcard;
  private String OrderText;
  
  public List<MyItem> getItem() {
    return item;
  }
  public void setItem(List<MyItem> item) {
    this.item = item;
  }
  public MyCreditCard getOrderCcard() {
    return OrderCcard;
  }
  public void setOrderCcard(MyCreditCard orderCcard) {
    OrderCcard = orderCcard;
  }
  public MyHeader getOrderHeader() {
    return OrderHeader;
  }
  public void setOrderHeader(MyHeader orderHeader) {
    OrderHeader = orderHeader;
  }
  public String getOrderKey() {
    return OrderKey;
  }
  public void setOrderKey(String orderKey) {
    OrderKey = orderKey;
  }
  public String getOrderText() {
    return OrderText;
  }
  public void setOrderText(String orderText) {
    OrderText = orderText;
  }
  
}
