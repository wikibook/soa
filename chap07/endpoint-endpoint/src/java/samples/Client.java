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


import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;

import com.example.oms.BUSOBJCCARD;
import com.example.oms.BUSOBJITEM;
import com.example.oms.OrderType;
import com.example.req.RequestOrderPort;

public class Client {
  
//! <example xn="CREATE_ENDPOINT_WITH_WSDL">
//! <c>chap07</c><s>endpoint</s>
  public static void main(String[] args) throws Exception {
    
    Endpoint endpoint = Endpoint.create(new RequestOrder());
    InputStream wsdlStream = 
      Client.class.getClassLoader().getResourceAsStream("RequestOrder.wsdl");
    URL wsdlURL = 
      Client.class.getClassLoader().getResource("RequestOrder.wsdl");
    if ( wsdlStream == null ) {
      throw new RuntimeException("Cannont find WSDL resouce file.");
    }
    ArrayList<Source> metadata = new ArrayList<Source>();
    Source wsdlSource = new StreamSource(wsdlStream);
    String wsdlId = wsdlURL.toExternalForm();
    wsdlSource.setSystemId(wsdlId);
    metadata.add(wsdlSource);
    endpoint.setMetadata(metadata);
    endpoint.publish("http://localhost:8680/oms");
//! </example>    
//! <example xn="INVOKE_ENDPOINT">
//! <c>chap07</c><s>endpoint</s>
    URL wsdlDeployURL = new URL("http://localhost:8680/oms/requestOrder?wsdl");
    System.out.println("Endpoint Returns this WSDL");
    System.out.println("===========================================");
    printWSDL(wsdlDeployURL);
    QName serviceQName =
      new QName("http://www.example.com/req", "RequestOrderService");
    QName portQName = 
      new QName("http://www.example.com/req", "RequestOrderPort");
    Service service = Service.create(wsdlURL, serviceQName);
    RequestOrderPort port = 
      (RequestOrderPort) service.getPort(portQName, RequestOrderPort.class);
    BUSOBJCCARD ccard = createCreditCard();
    ArrayList<BUSOBJITEM> itemList = createItemList();
    OrderType order = port.requestOrder(
        "ENT0072123", null, ccard, itemList);
    System.out.println();
    System.out.println();
    System.out.println("Webservice Returns this Order");
    System.out.println("===========================================");
    printReturnedOrder(order);
    endpoint.stop();
    
  }
//! </example>
  
  private static ArrayList<BUSOBJITEM> createItemList() {
    
    ArrayList<BUSOBJITEM> itemList = new ArrayList<BUSOBJITEM>();
    BUSOBJITEM item = new BUSOBJITEM();
    item.setITMNUMBER("012345");
    item.setSTORAGELOC("NE02");
    item.setTARGETQTY(BigDecimal.valueOf(50));
    item.setTARGETUOM("CNT");
    item.setPRICEPERUOM(BigDecimal.valueOf(7.95));
    item.setSHORTTEXT("7 mm Teflon Gasket");
    itemList.add(item);
    item = new BUSOBJITEM();
    item.setITMNUMBER("543210");
    item.setTARGETQTY(BigDecimal.valueOf(5));
    item.setTARGETUOM("KG");
    item.setPRICEPERUOM(BigDecimal.valueOf(12.58));
    item.setSHORTTEXT("Lithium grease with PTFE/Teflon");
    itemList.add(item);
    return itemList;
    
  }
  
  private static BUSOBJCCARD createCreditCard() {
    
    BUSOBJCCARD ccard = new BUSOBJCCARD();
    ccard.setCCTYPE("VISA");
    ccard.setCCNUMBER("01234567890123456789");
    ccard.setCCEXPIREDATE("2009-10-31");
    ccard.setCCNAME("John Doe");
    return ccard;
    
  }
  
  
  private static void printReturnedOrder(OrderType order) {
    
    Marshaller m = null;
    try {
      JAXBContext jc = JAXBContext.newInstance(OrderType.class);
      m = jc.createMarshaller();
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    } catch (Exception e) {}
    if ( m != null ) {
      JAXBElement<OrderType> orderElement = new JAXBElement<OrderType>(
          new QName("http://www.example.com/oms","order"), OrderType.class, order);
      try {
        m.marshal(orderElement, System.out);
      } catch (JAXBException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("order key = " + order.getOrderKey());
      System.out.println("order date = " + 
          order.getOrderHeader().getPURCHDATE());
      System.out.println("order text = " + order.getOrderText());
    }
    
  }
  
//! <example xn="PRINT_WSDL">
//! <c>chap07</c><s>endpoint</s>  
  private static void printWSDL(URL wsdlURL) throws Exception {

    HttpURLConnection con = (HttpURLConnection) wsdlURL.openConnection();
    con.setRequestMethod("GET");
    con.connect();
    InputStream wsdlInput = con.getInputStream();
    int b = wsdlInput.read();
    while ( b > -1 ) {
      System.out.print((char) b);
      b = wsdlInput.read();
    }
    wsdlInput.close();
    con.disconnect();

  }
//! </example>  
  
}

