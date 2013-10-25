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
import java.io.InputStream;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

public class TestClient extends TestCase {
  
  
  public static Test suite() {
    return new TestSuite(TestClient.class);
  }
  
  protected void setUp() throws Exception {}

  public void testEndpoint() throws Exception {
    
    String hostVal = System.getProperty("glassfish.host");	   
    String portVal = System.getProperty("glassfish.deploy.port");
    URL wsdlURL = 
      new URL("http://"+hostVal+":"+portVal+"/HelloService/Hello?wsdl");
    printWSDL(wsdlURL);
    QName serviceQName = new QName("http://samples/", "HelloService");
    QName portQName =    new QName("http://samples/", "HelloPort");
    Service service = Service.create(wsdlURL, serviceQName);
    Hello port = (Hello) service.getPort(portQName, Hello.class);
    String result = port.sayHello("Java Programmer");
    System.out.println(result);
    String expectedResult = "Hello: Java Programmer[injectedString: ZipZapZang!]" +
      System.getProperty("line.separator") + 
      "Greetings from MyWeb: Java Programmer" +
      System.getProperty("line.separator") + 
      "Goodbye: Java Programmer";
    assertEquals(expectedResult, result);

  }

  private static void printWSDL(URL wsdlURL) throws Exception {
    
    InputStream is = (InputStream) wsdlURL.getContent();
    Transformer xform = TransformerFactory.newInstance().newTransformer();
    xform.setOutputProperty(OutputKeys.INDENT,"yes");
    xform.setOutputProperty(OutputKeys.METHOD,"xml");
    xform.setOutputProperty(OutputKeys.MEDIA_TYPE,"text/xml");
    xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
    xform.transform(new StreamSource(is), new StreamResult(System.out));
    System.out.println();
    
  }
  
}
