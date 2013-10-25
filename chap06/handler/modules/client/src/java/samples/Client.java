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

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Response;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import com.example.oms.BUSOBJCCARD;
import com.example.oms.BUSOBJITEM;
import com.example.req.RequestOrderPort;
import com.example.req.RequestOrderResponse;
import com.example.req.RequestOrderService;

public class Client {
  
  public static void main(String[] args) throws Exception {
    
    File persistenceDir = new File(args[0]);
    runPersistenceHandlerTest(persistenceDir);
    
  }
  
//! <example xn="MAIN">
//! <c>chap06</c><s>handler</s>
  private static void runPersistenceHandlerTest(File persistenceDir)
  throws Exception {
    
    RequestOrderService service = new RequestOrderService();
    // add the handler to the service
    service.setHandlerResolver(new RequestOrderHandlerResolver());
    RequestOrderPort port = service.getRequestOrderPort();
    // configure message request context
    Map<String, Object> reqCtxt = ((BindingProvider) port).getRequestContext();
    reqCtxt.put(PersistMessageHandler.PERSISTENCE_DIR_PROP, persistenceDir);
    reqCtxt.put(AddMessageIdHandler.MSGID_PROP, "msg0001");
    // add the callback handler
    RequestOrderCallbackHandler cbh = new RequestOrderCallbackHandler();
    cbh.setStartTime((new Date()).getTime());
    Future<?> response = port.requestOrderAsync(
        "ENT0072123", "", createCreditCard(), createItemList(), cbh);
    response.get(2000, TimeUnit.MILLISECONDS);
   
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
  
  private static class RequestOrderCallbackHandler 
  implements AsyncHandler<RequestOrderResponse> {

    private long startTime;
    
    public void handleResponse (Response<RequestOrderResponse> response) {
      
      long elapsed = (new Date()).getTime() - startTime;
      try {
        JAXBContext jc = JAXBContext.newInstance(RequestOrderResponse.class);
        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        System.out.println();
        System.out.println("================================================");
        System.out.println("Asynchronous Proxy Test");
        System.out.println("with Persistence Message Handler on Client");
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
        
      } catch (Exception e) {
        e.printStackTrace ();
      }
    }
    
    public void setStartTime(long t) { startTime = t; }
    
  }
  
  
//! <example xn="HandlerResolver">
//! <c>chap06</c><s>handler</s>
  private static class RequestOrderHandlerResolver implements HandlerResolver {

    public List<Handler> getHandlerChain(PortInfo arg0) {

      List<Handler> handlerChain = new ArrayList<Handler>();
      handlerChain.add(new AddMessageIdHandler());
      handlerChain.add(new PersistMessageHandler());
      return handlerChain;

    }
    
  }
//! </example>

}