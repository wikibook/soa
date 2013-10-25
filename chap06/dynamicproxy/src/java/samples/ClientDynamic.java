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

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class ClientDynamic {
  
  public static void main(String[] args) throws Exception {
    
//    URL wsdlURL = new URL("http://localhost:8080/oms/requestOrder?wsdl");
    QName serviceQName =
      new QName("http://www.example.com/req", "RequestOrderService");
    QName portQName = 
      new QName("http://www.example.com/req", "RequestOrderPort");
//    Service service = Service.create(wsdlURL, serviceQName);
    Service service = Service.create(serviceQName);
    MyPort port = (MyPort) service.getPort(portQName, MyPort.class);
    (new Tester()).runTests(port);
    
  }
 
}