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
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPFault;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Response;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPFaultException;

import com.example.oms.BUSOBJCCARD;
import com.example.oms.BUSOBJITEM;
import com.example.req.RequestOrderPort;
import com.example.req.RequestOrderResponse;
import com.example.req.RequestOrderService;

public class Client {
  
  public static void main(String[] args) throws Exception {
    
    runStaticServiceProxyAsyncTest();
    String host = args[1];
    String port = args[2];
    runDynamicServiceProxyCallbackTest(host, port);
    File xmlFile = new File(args[0]);
    runDispatchPollingTest(xmlFile, host, port);
    
  }
  
  private static void runStaticServiceProxyAsyncTest() throws Exception {
    
    RequestOrderService service = new RequestOrderService();
    RequestOrderPort port = service.getRequestOrderPort();
    Response<RequestOrderResponse> response = port.requestOrderAsync(
        "ENT0072123", null, createCreditCard(), createItemList());
    long startTime = (new Date()).getTime();
    while (!response.isDone()) {
       Thread.sleep(10);
    }
    long elapsed = (new Date()).getTime() - startTime;
    RequestOrderResponse orderResponse = response.get();
    JAXBContext jc = JAXBContext.newInstance(RequestOrderResponse.class);
    Marshaller m = jc.createMarshaller();
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    
    System.out.println();
    System.out.println("================================================");
    System.out.println("Asynchronous Proxy Test");
    System.out.println("with Polling and Static Service");
    System.out.println("Elapsed waiting time for web service response:");
    System.out.println(elapsed + " milliseconds.");
    System.out.println("================================================");
    System.out.println();
    System.out.println();
    System.out.println("Response Message ===============================");
    m.marshal(orderResponse, System.out);
   
  }
  
  private static void runDynamicServiceProxyCallbackTest(String host, String portVal) throws Exception {
    
//! <example xn="CALLBACK_PROXY">
//! <c>chap06</c><s>asynchronous</s>
    URL wsdlURL = new URL("http://"+host+":"+portVal+
      "/chap06-endpoint-endpoint-1.0/requestOrder?wsdl");
    QName serviceQName =
      new QName("http://www.example.com/req", "RequestOrderService");
    Service service = Service.create(wsdlURL, serviceQName);
    QName portQName = 
      new QName("http://www.example.com/req", "RequestOrderPort");
    RequestOrderPort port = service.getPort(portQName, RequestOrderPort.class);
    RequestOrderCallbackHandler cbh = new RequestOrderCallbackHandler();
    cbh.setStartTime((new Date()).getTime());
    Future<?> response1 = port.requestOrderAsync(
        "ENT0072123", "", createCreditCard(), createItemList(), cbh);
    Future<?> response2 = port.requestOrderAsync(
        "ENT0072123", "", createExpiredCreditCard(), createItemList(), cbh);
    try {
    response1.get(2000, TimeUnit.MILLISECONDS);
    } catch (TimeoutException te) {
      response1.cancel(true);
    }
//! </example>    
    response2.get(2000, TimeUnit.MILLISECONDS);
    
  }
  
  private static void runDispatchPollingTest(File xmlFile, String host, String port)
  throws Exception {
    
    // Load file in memory. (Dispatch doesn't like Files - a bug?)
    ByteArrayOutputStream xmlByteArray = new ByteArrayOutputStream();
    TransformerFactory fac = TransformerFactory.newInstance();
    Transformer x = fac.newTransformer();
    x.transform(new StreamSource(xmlFile), new StreamResult(xmlByteArray));
    StreamSource xmlSource = 
      new StreamSource(new StringReader(xmlByteArray.toString()));

    // create Dispatch<Source>
//! <example xn="POLLING_DISPATCH">
//! <c>chap06</c><s>asynchronous</s>
    URL wsdlURL = new URL("http://"+host+":"+port+
      "/chap06-endpoint-endpoint-1.0/requestOrder?wsdl");
    QName serviceQName =
      new QName("http://www.example.com/req", "RequestOrderService");
    Service service = Service.create(wsdlURL, serviceQName);
    QName portQName = 
      new QName("http://www.example.com/req", "RequestOrderPort");
    Dispatch<Source> dispatch = service.createDispatch(portQName, 
        Source.class, Service.Mode.PAYLOAD);
    Response<Source> responseSource = dispatch.invokeAsync(xmlSource);
    long startTime = (new Date()).getTime();
    while (!responseSource.isDone()) {
       Thread.sleep(10);
    }
    long elapsed = (new Date()).getTime() - startTime;
    Source orderSource = responseSource.get();
//! </example>    
    JAXBContext jc = JAXBContext.newInstance(RequestOrderResponse.class);
    Marshaller m = jc.createMarshaller();
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    Unmarshaller u = jc.createUnmarshaller();
    RequestOrderResponse response = 
      (RequestOrderResponse) u.unmarshal(orderSource);
    
    System.out.println();
    System.out.println("================================================");
    System.out.println("Asynchronous Dispatch Test");
    System.out.println("with Polling and Dynamic Service");
    System.out.println("Elapsed waiting time for web service response:");
    System.out.println(elapsed + " milliseconds.");
    System.out.println("================================================");
    System.out.println();
    System.out.println();
    System.out.println("Response Message ===============================");
    m.marshal(response, System.out);
 
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
    ccard.setCCEXPIREDATE("2005-10-31");
    ccard.setCCNAME("John Doe");
    return ccard;
    
  }
  
//! <example xn="AsyncHandler">
//! <c>chap06</c><s>asynchronous</s>
  private static class RequestOrderCallbackHandler 
  implements AsyncHandler<RequestOrderResponse> {
    
    private long startTime;
    
    public void handleResponse (Response<RequestOrderResponse> response) {
      
      long elapsed = (new Date()).getTime() - startTime;
      Marshaller m;
      try {
        JAXBContext jc = JAXBContext.newInstance(RequestOrderResponse.class);
        m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        System.out.println();
        System.out.println("================================================");
        System.out.println("Asynchronous Proxy Test");
        System.out.println("with Callback and Dynamic Service");
        System.out.println("Elapsed waiting time for web service response:");
        System.out.println(elapsed + " milliseconds.");
        System.out.println("================================================");
        System.out.println();
        System.out.println();
        System.out.println("Response Message ===============================");
        if ( response != null ) {
          RequestOrderResponse orderResponse = response.get();
          m.marshal(orderResponse, System.out);
        }
      } catch (ExecutionException e) {
        Throwable t = e.getCause();
        if ( t instanceof SOAPFaultException ) {
          processSOAPFault((SOAPFaultException) t);
          return;
        }
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
      
    }
    
    public void setStartTime(long t) { startTime = t; }
    
//!  </example>
    
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
    
  }

}
