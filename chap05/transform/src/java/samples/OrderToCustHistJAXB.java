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

import java.net.URI;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;
import com.example.css.CustomerHistoryEntriesType;
import com.example.css.CustomerHistoryEntryType;
import com.example.oms.BUSOBJITEM;
import com.example.oms.OrderType;
import com.example.oms.OrdersType;

public class OrderToCustHistJAXB {

  public static void main(String[] args) throws Exception {
    
    if (args.length != 2) {
      System.out.println
      ("Usage: java " + OrderToCustHistJAXB.class + 
      " <GetNewOrders URL> <PostCustomerHistory URL>");
      return;
    }   
    QName svcQName = new QName("http://sample", "svc");
    QName orderQName = new QName("http://sample", "getNewOrders");
    QName histQName = new QName("http://sample", "addCustomerHistory");
    String newOrdersUrl = args[0];
    String addCustHistUrl = args[1];
    JAXBContext jc = 
      JAXBContext.newInstance("com.example.oms:com.example.css");
    Unmarshaller u = jc.createUnmarshaller();
    // Get the new orders
    Service svc = Service.create(svcQName);
    svc.addPort(orderQName, HTTPBinding.HTTP_BINDING, newOrdersUrl);
    // should use JAXBContext version, but it doesn't work with HTTP GET
    // Create customer histories from the new orders
    //! <example xn="TransformOrdersToCustHistWithJAXB">
    //! <c>chap05</c><s>transform</s>
    Dispatch<Source> getOrdersDispatch =
      svc.createDispatch(orderQName, Source.class, Service.Mode.PAYLOAD);
    Map<String, Object> requestContext = getOrdersDispatch.getRequestContext();
    requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "GET");
    Source s =  (Source) getOrdersDispatch.invoke(null);
    JAXBElement<OrdersType> newOrdersElt = 
      (JAXBElement<OrdersType>) u.unmarshal(s);
    OrdersType newOrders = newOrdersElt.getValue();
    List<OrderType> newOrderList = newOrders.getOrder();
    CustomerHistoryEntriesType ch = new CustomerHistoryEntriesType();
    CustomerHistoryEntryType che = null;
    // for each order, create and add a customer history to the list
    for (OrderType newOrder : newOrderList) {
      che = new CustomerHistoryEntryType();
      che.setCustomerNumber(newOrder.getOrderHeader().getCUSTNO());
      CustomerHistoryEntryType.OrderLookupInfo orderLookupInfo = 
        new CustomerHistoryEntryType.OrderLookupInfo();
      orderLookupInfo.setOrderNumber(newOrder.getOrderKey());
      orderLookupInfo.setPURCHORDNO(newOrder.getOrderHeader().getPURCHORDNO());
      orderLookupInfo.setOrderText(newOrder.getOrderText());
      // add the item numbers
      for (BUSOBJITEM boItem : newOrder.getOrderItems().getItem()) {
        orderLookupInfo.getITMNUMBER().add(boItem.getITMNUMBER());
      }
      // add to the the list history entries
      ch.getCustomerHistoryEntry().add(che);
    }
    JAXBElement<CustomerHistoryEntriesType> chElt = 
      new JAXBElement<CustomerHistoryEntriesType>(
          new QName("http://www.example.com/css","CustomerHistoryEntries"),
          CustomerHistoryEntriesType.class,
          JAXBElement.GlobalScope.class,
          ch);
    //! </example>
    // Update the customer histories
    //! <example xn="POSTDispatchJAXB">
    //! <c>chap05</c><s>transform</s>
    svc.addPort(histQName, HTTPBinding.HTTP_BINDING, addCustHistUrl);
    Dispatch<Object> postCustomerHistoryDispatch =
      svc.createDispatch(histQName, jc, Service.Mode.PAYLOAD);
    postCustomerHistoryDispatch.invoke(chElt);
    //! </example>
  }

}
