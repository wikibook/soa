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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;

import com.example.oms.BUSOBJCCARD;
import com.example.oms.BUSOBJITEM;
import com.example.oms.OrderType;
import com.example.req.RequestOrder;
import com.example.req.RequestOrderResponse;

public class Client {
  
  public static void main(String[] args) throws Exception {
    
    File xmlFile = new File(args[0]);
    String host = args[1];
    String port = args[2];

    // run Dispatch Tests
    runDispatchSourceTest(xmlFile, host, port);
    runDispatchJAXBTest(host, port);

    
  }
  
  private static void runDispatchSourceTest(File xmlFile, String host, String port) 
  throws Exception {
    
    System.out.println();
    System.out.println("===============================================");
    System.out.println("Dispatch<Source> Test");
    System.out.println("===============================================");
    System.out.println();

    // Load file in memory. (Dispatch doesn't like Files - a bug?)
    ByteArrayOutputStream xmlByteArray = new ByteArrayOutputStream();
    TransformerFactory fac = TransformerFactory.newInstance();
    Transformer x = fac.newTransformer();
    x.transform(new StreamSource(xmlFile), new StreamResult(xmlByteArray));
//! <example xn="DISPATCH_SOURCE">
//! <c>chap06</c><s>xmlmessaging</s>
    StreamSource xmlSource = 
      new StreamSource(new StringReader(xmlByteArray.toString()));
    // create Service
    URL wsdlURL = new URL("http://"+host+":"+port+
      "/chap06-endpoint-endpoint-1.0/requestOrder?wsdl");
    QName serviceQName =
      new QName("http://www.example.com/req", "RequestOrderService");
    Service service = Service.create(wsdlURL, serviceQName);
    // create Dispatch<Source>
    QName portQName = 
      new QName("http://www.example.com/req", "RequestOrderPort");
    Dispatch<Source> dispatch = service.createDispatch(portQName, 
        Source.class, Service.Mode.PAYLOAD);
    Source orderSource = dispatch.invoke(xmlSource);
    JAXBContext jc = JAXBContext.newInstance(RequestOrderResponse.class);
    Unmarshaller u = jc.createUnmarshaller();
    RequestOrderResponse response = 
      (RequestOrderResponse) u.unmarshal(orderSource);
//! </example>
    Marshaller m = jc.createMarshaller();
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    System.out.println("Request Message ===============================");
    System.out.println();
    System.out.println(xmlByteArray.toString());
    System.out.println();
    System.out.println("Response Message ===============================");
    m.marshal(response, System.out);
 
  }
  
  private static void runDispatchJAXBTest(String host, String port) throws Exception {
    
    System.out.println();
    System.out.println("===============================================");
    System.out.println("Dispatch<JAXB> Test");
    System.out.println("===============================================");
    System.out.println();
    // create Service
    URL wsdlURL = new URL("http://"+host+":"+port+"/chap06-endpoint-endpoint-1.0/requestOrder?wsdl");
    QName serviceQName =
      new QName("http://www.example.com/req", "RequestOrderService");
    Service service = Service.create(wsdlURL, serviceQName);
    // create Dispatch<Object> - JAXB form
//! <example xn="DISPATCH_JAXB">
//! <c>chap06</c><s>xmlmessaging</s>
    JAXBContext ctxt = JAXBContext.
    newInstance(MyRequestOrder.class, MyRequestOrderResponse.class);
    QName portQName = 
      new QName("http://www.example.com/req", "RequestOrderPort");
    Dispatch<Object> dispatchJAXB = service.createDispatch(portQName, 
        ctxt, Service.Mode.PAYLOAD);
    // create the custom request order object
    MyRequestOrder myReq = new MyRequestOrder();
    myReq.ccard = createMyCreditCard();
    myReq.item = createMyItemList();
    myReq.CUST_NO = "ENT0072123";
    myReq.PURCH_ORD_NO = "";
    MyRequestOrderResponse resp =
      (MyRequestOrderResponse) dispatchJAXB.invoke(myReq);
//! </example>
    Marshaller m = ctxt.createMarshaller();
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    System.out.println("Request Message ===============================");
    System.out.println();
    m.marshal(myReq, System.out);
    System.out.println();
    System.out.println("Response Message ===============================");
    System.out.println();
    m.marshal(resp, System.out);
    
  }
  
  private static ArrayList<MyItem> createMyItemList() {
    
    ArrayList<MyItem> itemList = new ArrayList<MyItem>();
    MyItem item = new MyItem();
    item.setITMNUMBER("012345");
    item.setSTORAGELOC("NE02");
    item.setTARGETQTY(BigDecimal.valueOf(50));
    item.setTARGETUOM("CNT");
    item.setPRICEPERUOM(BigDecimal.valueOf(7.95));
    item.setSHORTTEXT("7 mm Teflon Gasket");
    itemList.add(item);
    item = new MyItem();
    item.setITMNUMBER("543210");
    item.setTARGETQTY(BigDecimal.valueOf(5));
    item.setTARGETUOM("KG");
    item.setPRICEPERUOM(BigDecimal.valueOf(12.58));
    item.setSHORTTEXT("Lithium grease with PTFE/Teflon");
    itemList.add(item);
    return itemList;
    
  }
  
  private static MyCreditCard createMyCreditCard() {
    
    MyCreditCard ccard = new MyCreditCard();
    ccard.CC_TYPE = "VISA";
    ccard.CC_NUMBER = "01234567890123456789";
    ccard.CC_EXPIRE_DATE = "2009-10-31";
    ccard.CC_NAME = "John Doe";
    return ccard;
    
  }
  
  
  private static RequestOrder createRequestOrder() {
    
    RequestOrder req = new RequestOrder();
    req.setCUSTNO("ENT0072123");
    req.setCcard(createCreditCard());
    for (BUSOBJITEM item : createItemList()) {
      req.getItem().add(item);
    }
    return req;
    
  }
  
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

}
