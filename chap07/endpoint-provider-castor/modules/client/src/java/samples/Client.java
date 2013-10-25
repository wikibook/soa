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


import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.soap.SOAPFault;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.Service;

import com.example.oms.BUSOBJCCARD;
import com.example.oms.BUSOBJITEM;
import com.example.oms.OrderType;
import com.example.req.InputFault;
import com.example.req.RequestOrderPort;

public class Client {
  
  public static void main(String[] args) throws Exception {

    if (args.length != 2) {
      throw new Exception("Must specify host and port");
    }
    String hostName = args[0];
    String portVal = args[1];    
    System.out.println();
    System.out.println("Getting proxy from the dynamic service.");
    System.out.println();
    URL wsdlURL = new URL("http://"+hostName+":"+portVal+"/chap07-endpoint-provider-castor-endpoint-1.0/RequestOrder?wsdl");
    QName serviceQName =
      new QName("http://www.example.com/req", "RequestOrderService");
    QName portQName = 
      new QName("http://www.example.com/req", "RequestOrderPort");
    Service service = Service.create(wsdlURL, serviceQName);
    RequestOrderPort port = 
      (RequestOrderPort) service.getPort(portQName, RequestOrderPort.class);
    
    BUSOBJCCARD ccard = createCreditCard();
    BUSOBJCCARD expiredCCard = createExpiredCreditCard();
    ArrayList<BUSOBJITEM> itemList = createItemList();
    
    OrderType order;
    try {
      System.out.println("Running test with expired credit card.");
      order = port.requestOrder(
          "ENT0072123", null, expiredCCard, itemList);
    } catch (SOAPFaultException sfe) { // a runtime exception
      processSOAPFault(sfe);
    } catch (InputFault e) {  // a checked exception
      System.out.println("Error - should have thrown SOAPFault");
    }
    try {
      System.out.println("Running test with null customer number.");
      order = port.requestOrder(
          null, null, ccard, itemList);
    } catch (InputFault ife) {
      processInputFault(ife);
    }  catch (SOAPFaultException sfe) { // a runtime exception
      System.out.println("Error - should have thrown InputFault");
      processSOAPFault(sfe);
    }
    try {
      System.out.println("Running test with a valid request.");
      order = port.requestOrder(
          "ENT0072123", null, ccard, itemList);
      printReturnedOrder(order);
    } catch (InputFault ife) {
      processInputFault(ife);
    }
    
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
  
  
  private static BUSOBJCCARD createExpiredCreditCard() {
    
    BUSOBJCCARD ccard = new BUSOBJCCARD();
    ccard.setCCTYPE("VISA");
    ccard.setCCNUMBER("01234567890123456789");
    ccard.setCCEXPIREDATE("2006-01-29");
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
  
  private static void processInputFault(InputFault e) {
    
    System.out.println("Mapped Exception (InputFault)");
    System.out.println("InputFault.getMessage() =");
    System.out.println(e.getMessage());
    System.out.println("InputFault.getFaultInfo.getMsg() =");
    System.out.println(e.getFaultInfo().getMsg());
    System.out.println();

  }
  
  private static void printSOAPFault(SOAPFault sf) {
    
    try {
      Transformer xfmr = TransformerFactory.newInstance().newTransformer();
      xfmr.setOutputProperty(OutputKeys.INDENT, "yes");
      xfmr.transform(new DOMSource(sf), new StreamResult(System.out));
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println();
    
  }
  
  private static void processSOAPFault(SOAPFaultException sfe) {
    
    System.out.println("Unmapped Exception (SOAPFaultException)");
    SOAPFault sf = sfe.getFault();
    printSOAPFault(sf);
    
  }
  
}

