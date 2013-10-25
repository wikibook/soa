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

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceRef;
import com.example.req.RequestOrderPort;
import com.example.req.RequestOrderService;

public class Client {

//! <example xn="WebServiceRef">
//! <c>chap06</c><s>proxy</s>
  @WebServiceRef(RequestOrderService.class)
  public static RequestOrderPort port;
//! </example>
  
  public static void main(String[] args) throws Exception {
    
    if (args.length != 2) {
      throw new Exception("Must specify host and port");
    }
    String hostName = args[0];
    String portVal = args[1];
    if ( port == null ) {
      System.out.println("Running in standalone mode.");
      System.out.println();
      doStaticService();
      doDynamicService(hostName, portVal);
      return;
    } else {
      System.out.println("Running inside the Application Client Container.");
      System.out.println();
    }
    (new Tester()).runTests(port);
  
  }
  
  public static void doStaticService() throws Exception {
    
    System.out.println();
    System.out.println("Getting proxy from the static service.");
    System.out.println();
//! <example xn="StaticService">
//! <c>chap06</c><s>proxy</s>
    RequestOrderService service = new RequestOrderService();
    RequestOrderPort port = service.getRequestOrderPort();
    (new Tester()).runTests(port);
//! </example>
    
  }
 
  public static void doDynamicService(String hostName, String portVal) throws Exception {
    
    System.out.println();
    System.out.println("Getting proxy from the dynamic service.");
    System.out.println();
//! <example xn="DynamicService">
//! <c>chap06</c><s>proxy</s>
    URL wsdlURL = new URL("http://"+hostName+":"+portVal+"/chap06-endpoint-endpoint-1.0/requestOrder?wsdl");
  QName serviceQName =
    new QName("http://www.example.com/req", "RequestOrderService");
  QName portQName = 
    new QName("http://www.example.com/req", "RequestOrderPort");
  Service service = Service.create(wsdlURL, serviceQName);
  RequestOrderPort port = 
    (RequestOrderPort) service.getPort(portQName, RequestOrderPort.class);
  (new Tester()).runTests(port);
//! </example>
    
  }
}
