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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import com.example.oms.BUSOBJCCARD;
import com.example.oms.BUSOBJHEADER;
import com.example.oms.BUSOBJITEM;
import com.example.oms.OrderType;
import com.example.req.InputFault;
import com.example.faults.InputMessageValidationFaultType;
import com.example.req.RequestOrderPort;

//! <example xn="WEBSERVICE_CLASS_DEFINITION">
//! <c>chap07</c><s>sei</s>
@WebService(targetNamespace = "http://www.example.com/req", 
    endpointInterface="com.example.req.RequestOrderPort")
public class RequestOrder implements RequestOrderPort {
  
  @Resource
  WebServiceContext wscontext;

  public OrderType requestOrder(String custNum, String poNum, BUSOBJCCARD ccard,
      List<BUSOBJITEM> itemList) throws InputFault {
//! </example>  
    
    // check for invalid input
//! <example xn="THROWING_MAPPED_FAULT">
//! <c>chap07</c><s>faults</s>
    InputMessageValidationFaultType ft = new InputMessageValidationFaultType();
    if ( custNum == null ) {
      ft.setMsg("Customer Number cannot be null.");
      throw new InputFault("Input parameter failed validation.", ft);
    }
    if ( poNum == null && ccard == null ) {
      ft.setMsg("Must supply either a PO or a CCard.");
      throw new InputFault("Input parameter failed validation.", ft);
    }
    if ( itemList == null || itemList.isEmpty() ) {
      ft.setMsg("Must have a least one item.");
      throw new InputFault("Input parameter failed validation.", ft);
    }
//! </example>  
//! <example xn="JAXB_IS_LESS_UGLY">
//! <c>chap07</c><s>sei</s>
    //  generate a psuedo-unique 10 digit order ID
    String orderId = Long.toString((new Date()).getTime());
    orderId = orderId.substring(orderId.length()-10);
    OrderType response = new OrderType();
    response.setOrderKey(orderId);
    //  create OrderHeader
    BUSOBJHEADER hdr = new BUSOBJHEADER();
    response.setOrderHeader(hdr);
    hdr.setCUSTNO(custNum);
    GregorianCalendar cal = new GregorianCalendar();
    hdr.setPURCHDATE(dateAsString(cal));
    cal.add(Calendar.DAY_OF_MONTH, 14);
    hdr.setWARDELDATE(dateAsString(cal));
    if ( poNum != null && poNum.length()>0 ) {
      hdr.setPYMTMETH("PO");
      hdr.setPURCHORDNO(poNum);
    } else {
      hdr.setPYMTMETH("CC");
      // OrderType.OrderCcard
      OrderType.OrderCcard ordCcard = new OrderType.OrderCcard();
      ordCcard.setCcard(ccard);
      response.setOrderCcard(ordCcard);
//! </example>  
//    check for other exception types
//! <example xn="CREDIT_CARD_SOAPFAULTEXCEPTION">
//! <c>chap07</c><s>faults</s>
      if ( hasExpired(ccard) ) {
        try {
          SOAPFactory fac = SOAPFactory.newInstance(); 
          SOAPFault sf = fac.createFault("Business logic exception",
              new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client"));
          Detail d = sf.addDetail();
          DetailEntry de = d.addDetailEntry(
              new QName("", "expiredCC"));
          de.setValue("Credit card has expired");
          throw new SOAPFaultException(sf);
        } catch (SOAPException e) {
          throw new RuntimeException(
              "Failed to create SOAPFault: " + e.getMessage());
        }
      }
//! </example>  
    }
    hdr.setSALESORG("WEB");
    // OrderType.OrderItems 
    OrderType.OrderItems items = new OrderType.OrderItems();
    // no setter
    BigDecimal billAmount = BigDecimal.valueOf(0);
    for (BUSOBJITEM item : itemList ) {
      items.getItem().add(item);
      billAmount = 
        billAmount.add(item.getTARGETQTY().multiply(item.getPRICEPERUOM()));
    }
    ccard.setBILLAMOUNT(billAmount);
    response.setOrderItems(items);
    response.setOrderText("WEB Order placed via Order Management System.");
    return response;

  }
  
  private boolean hasExpired(BUSOBJCCARD ccard) {
    String today = dateAsString(Calendar.getInstance());
    if (ccard.getCCEXPIREDATE().compareTo(today) < 0 ) {
      return true;
    }
    return false;
  }

  private String dateAsString(Calendar cal) {
    
    String year = Integer.toString(cal.get(Calendar.YEAR));
    String month = Integer.toString(cal.get(Calendar.MONTH + 1));
    if ( month.length() < 2 ) month = "0" + month;
    String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH + 1));
    if ( day.length() < 2 ) day = "0" + day;
    return year+"-"+month+"-"+day;
    
  }

}
