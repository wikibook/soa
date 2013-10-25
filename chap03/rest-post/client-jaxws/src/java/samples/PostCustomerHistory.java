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
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;

public class PostCustomerHistory {

  //! <example xn="PostCustomerHistory_Client_JAXWS">
  //! <c>chap03</c><s>rest-post</s>
  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.err.println
	("Usage: java XMLUploadSender <XML file name> " 
	 + "<Web Service URL>");
      System.exit(1);
    } 
    QName svcQName = new QName("http://sample", "svc");
    QName portQName = new QName("http://sample", "port");
    Service svc = Service.create(svcQName);
    svc.addPort(portQName, HTTPBinding.HTTP_BINDING, args[1]);
    Dispatch<Source> dis =
    	svc.createDispatch(portQName, Source.class, Service.Mode.PAYLOAD);
    dis.invoke(new StreamSource(new File(args[0])));
    Map<String, Object> respContext = dis.getResponseContext();
    Integer respCode = 
      (Integer) respContext.get(MessageContext.HTTP_RESPONSE_CODE);
    System.out.println("HTTP Response Code: "+respCode);
  }
  //! </example>
}
