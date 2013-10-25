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
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.http.HTTPBinding;
import java.io.IOException;
import java.io.StringReader;

public class GetNewOrdersPost {
  
  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.err.println
      ("Usage: java GetNewOrdersPost <Web Service URL>");
      System.exit(1);
    } 
    QName svcQName = new QName("http://bogus.com", "GetNewOrdersProviderPortService");
    QName portQName = new QName("http://bogus.com", "GetNewOrdersProviderPortService");
    Service svc = Service.create(svcQName);
    svc.addPort(portQName, HTTPBinding.HTTP_BINDING, args[0]);
    Dispatch<Source> dis =
      svc.createDispatch(portQName, Source.class, Service.Mode.PAYLOAD);
    Source result = dis.invoke(new StreamSource(new StringReader("<bogus/>")));
    try {
      TransformerFactory.newInstance().newTransformer()
      .transform(result, new StreamResult(System.out));
    } catch (Exception e) {
      throw new IOException(e.getMessage());
    } 
  }

}
