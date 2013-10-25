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
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.soap.SOAPFault;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.soap.SOAPFaultException;


public class Tester {
  
  public void runTests(MyPort port) throws TransformerFactoryConfigurationError, 
  TransformerException {
    
    MyCreditCard ccard = createCreditCard();
    MyCreditCard expiredCCard = createExpiredCreditCard();
    ArrayList<MyItem> itemList = createItemList();
    
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
  
  private ArrayList<MyItem> createItemList() {
    
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
  
  private MyCreditCard createCreditCard() {
    
    MyCreditCard ccard = new MyCreditCard();
    ccard.CC_TYPE = "VISA";
    ccard.CC_NUMBER = "01234567890123456789";
    ccard.CC_EXPIRE_DATE = "2009-10-31";
    ccard.CC_NAME = "John Doe";
    return ccard;
    
  }
  
  private MyCreditCard createExpiredCreditCard() {
    
    MyCreditCard ccard = new MyCreditCard();
    ccard.CC_TYPE = "VISA";
    ccard.CC_NUMBER = "01234567890123456789";
    ccard.CC_EXPIRE_DATE = "2006-01-29";
    ccard.CC_NAME = "John Doe";
    return ccard;
    
  }
  
  private void printSOAPFault(SOAPFault sf) {
    
    try {
      Transformer xfmr = TransformerFactory.newInstance().newTransformer();
      xfmr.setOutputProperty(OutputKeys.INDENT, "yes");
      xfmr.transform(new DOMSource(sf), new StreamResult(System.out));
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println();
    
  }
  
  private void processSOAPFault(SOAPFaultException sfe) {
    
    System.out.println("Unmapped Exception (SOAPFaultException)");
    SOAPFault sf = sfe.getFault();
    printSOAPFault(sf);
    
  }
  
  private void processInputFault(InputFault e) {
    
    System.out.println("Mapped Exception (InputFault)");
    System.out.println("InputFault.getMessage() =");
    System.out.println(e.getMessage());
    System.out.println("InputFault.getFaultInfo.getMsg() =");
    System.out.println(e.getFaultInfo().msg);
    System.out.println();
    
  }
  
  private void printReturnedOrder(OrderType order) {
    
    Marshaller m = null;
    try {
      JAXBContext jc = JAXBContext.newInstance(OrderType.class);
      m = jc.createMarshaller();
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      m.marshal(order, System.out);
    } catch (JAXBException e) {
      e.printStackTrace();
    }
    
  }
  
}