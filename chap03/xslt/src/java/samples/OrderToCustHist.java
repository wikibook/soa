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
import java.net.URI;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.http.HTTPBinding;

public class OrderToCustHist {

  public static void main(String[] args) throws Exception {

    if (args.length != 3) {
      System.out.println
	("Usage: java " + OrderToCustHist.class + 
	 " <GetNewOrders URL> <PostCustomerHistory URL> <xslt-file>");
      return;
    }   
    QName svcQName = new QName("http://sample", "svc");
    QName orderQName = new QName("http://sample", "getNewOrders");
    QName histQName = new QName("http://sample", "addCustomerHistory");
    String newOrdersUrl = args[0];
    String addCustHistUrl = args[1];
    String xsltFile = args[2];
    //! <example xn="XSLTwithJAXP">
    //! <c>chap03</c><s>xslt</s>
    // Get the new orders
    Service svc = Service.create(svcQName);
    svc.addPort(orderQName, HTTPBinding.HTTP_BINDING, newOrdersUrl);
    Dispatch<Source> getOrdersDispatch =
      svc.createDispatch(orderQName, Source.class, Service.Mode.PAYLOAD);
    Source newOrdersSource = 
      getOrdersDispatch.invoke(new StreamSource(new StringReader("<empty/>")));
    // Instantiate a Transformer using our XSLT file
    Transformer transformer = 
      TransformerFactory.newInstance().newTransformer
      (new StreamSource(new File(xsltFile)));
    // Transform the new orders into history entry files
    ByteArrayOutputStream ba = new ByteArrayOutputStream();
    transformer.transform(newOrdersSource, new StreamResult(ba));
    // Update the customer histories
    svc.addPort(histQName, HTTPBinding.HTTP_BINDING, addCustHistUrl);
    Dispatch<Source> postCustomerHistoryDispatch =
      svc.createDispatch(histQName, Source.class, Service.Mode.PAYLOAD);
    postCustomerHistoryDispatch
    .invoke(new StreamSource(new StringReader(ba.toString())));
    //! </example>
  }

}
