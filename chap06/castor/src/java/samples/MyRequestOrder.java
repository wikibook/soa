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

//! <example xn="MyRequestOrder">
//! <c>chap06</c><s>castor</s>
public class MyRequestOrder {

  protected String custno;
  protected String purchordno;
  protected CreditCard ccard;
  protected List<MyItem> itemList;
//! </example>     
  public CreditCard getCcard() {
    return ccard;
  }
  public void setCcard(CreditCard ccard) {
    this.ccard = ccard;
  }
  public String getCustno() {
    return custno;
  }
  public void setCustno(String custno) {
    this.custno = custno;
  }
  public List<MyItem> getItemList() {
    if (itemList == null) itemList = new ArrayList<MyItem>();
    return itemList;
  }
  public void setItemList(List<MyItem> itemList) {
    this.itemList = itemList;
  }
  public String getPurchordno() {
    return purchordno;
  }
  public void setPurchordno(String purchordno) {
    this.purchordno = purchordno;
  }
  
}
