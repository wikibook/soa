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
package com.javector.soaj.wsdlgentest.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//! <example xn="PurchaseOrder">
//! <c>chap11</c><s>serializer</s>
public class PurchaseOrder {
  
  private Address billTo = new Address();
  private List items;
  private String ponum;
  
  public void setBillTo(Address billTo) {
    this.billTo = billTo;
  }
  
  public void setItems(List items) {
    for (Object item : items) {
      addItem((Item) item);
    }
  }
  
  public Address getBillTo() {
    return billTo;
  }
  
  public List getItems() {
    return items;
  }
  
  public void addItem(Item item) {
    if ( items == null ) {
      items = new ArrayList();
    }
    items.add(item);
  }
  
  public String getPonum() {
    return ponum;
  }
  
  public void setPonum(String ponum) {
    this.ponum = ponum;
  }
  
}
//! </example>
