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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MyRequestOrder {

  protected String custno;
  protected String purchordno;
  protected MyCreditCard ccard;
  protected List<MyItem> itemList;
    
  public MyCreditCard getCcard() {
    return ccard;
  }
  public void setCcard(MyCreditCard ccard) {
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
  
  public MyRequestOrderResponse processOrder() throws PaymentException {
    MyRequestOrderResponse response = new MyRequestOrderResponse();
    MyOrderType order = new MyOrderType();
    response.setOrder(order);
    //  generate a psuedo-unique 10 digit order ID
    String ordId = Long.toString((new Date()).getTime());
    ordId = ordId.substring(ordId.length()-10);
    order.setOrderKey(ordId);
    MyHeader hdr = new MyHeader();
    order.setOrderHeader(hdr);
    hdr.setCustno(getCustno());
    hdr.setPurchdate(dateAsString(new GregorianCalendar()));
    hdr.setSalesorg("WEB");
    if ( getPurchordno() != null ) {
      hdr.setPymtmeth("PO");
      hdr.setPurchordno(getPurchordno());
    } else {
      if ( getCcard() == null ) {
        throw new PaymentException("No payment info.");
      } else {
        String today = dateAsString(Calendar.getInstance());
        if (getCcard().expireDate.compareTo(today) < 0 ) {
          throw new PaymentException("Expired ccard.");
        }
        float billAmt = 0;
        for (MyItem item : getItemList()) {
          billAmt += item.getPRICEPERUOM().floatValue() * item.getTARGETQTY();
        }
        getCcard().amount = billAmt;
        order.setOrderCcard(getCcard());
      }
    }
    order.setItem(getItemList());
    order.setOrderText("This order built with Castor.");
    return response;
  }
  
  private String dateAsString(Calendar cal) {
    
    String year = Integer.toString(cal.get(Calendar.YEAR));
    String month = Integer.toString(cal.get(Calendar.MONTH)+1);
    if ( month.length() < 2 ) month = "0" + month;
    String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
    if ( day.length() < 2 ) day = "0" + day;
    return year+"-"+month+"-"+day;
    
  }
  
}
