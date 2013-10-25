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

import java.util.ArrayList;
import java.util.List;

//! <example xn="MySimpleOrder">
//! <c>chap05</c><s>customjava</s>
public class MySimpleOrder {

  private MyAddress billTo;
  private List<MyItem> itemList;
  
  public MySimpleOrder(String name, String street, String city, String state,
      String zip, String phone) {
    this(new MyAddress(name, street, city, state, zip, phone));
  }
  
  public MySimpleOrder(MyAddress addr) {
    this.billTo = addr;
    itemList = new ArrayList<MyItem>();
  }

  public MyAddress getBillTo() {
    return billTo;
  }

  public List<MyItem> getItemList() {
    return itemList;
  }
  
}
//! </example>

