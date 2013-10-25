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
import javax.xml.ws.WebServiceRef;
import com.example.bug.Fault;
import com.example.bug.EchoPort;
import com.example.bug.EchoService;

public class Client {
  
  @WebServiceRef(wsdlLocation="http://localhost:8080/echoservice/echo?wsdl")
  public static EchoService service;
    
  public static void main(String[] args) throws Exception {
    
    Client client = new Client();
    if ( service == null ) {
      System.out.println("Running in standalone mode.");
      service = new EchoService(
          new URL("http://localhost:8080/echoservice/echo?wsdl"),
          new QName("http://www.example.com/bug", "EchoService"));
      } else {
      System.out.println("Running inside the Application Client Container.");
    }
    System.out.println();
    client.runTests();
    
  }
  
  public void runTests() {
    
    EchoPort port = service.getEchoPort();
    try {
      System.out.println("EchoPort.echo(\"Hello World\") = " + port.echo("Hello World"));
      System.out.println("EchoPort.echo(\"throwFault\") = " + port.echo("throwFault"));
    } catch (Fault f) {
      System.out.println("Fault.getMesage() = " + f.getMessage());
      System.out.println("Fault.getFaultInfo() = " + f.getFaultInfo());
    }
  }
  
}