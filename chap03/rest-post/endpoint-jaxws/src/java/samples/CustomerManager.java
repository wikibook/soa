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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingType;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.http.HTTPException;

//! <example xn="CustomerManagerAnnotated_JAXWS">
//! <c>chap03</c><s>rest-post</s>
@ServiceMode(value=Service.Mode.PAYLOAD)
@WebServiceProvider
@BindingType(value=HTTPBinding.HTTP_BINDING)
public class CustomerManager implements Provider<Source> {
  
//  @Resource(type=Object.class)
//  protected WebServiceContext wsContext;
//  
//  public Source invoke(Source source) {
//    try {
//        MessageContext mc = wsContext.getMessageContext();
//        String query = (String)mc.get(MessageContext.QUERY_STRING);
//        String path = (String)mc.get(MessageContext.PATH_INFO);
//        System.out.println("Query String = "+query);
//        System.out.println("PathInfo = "+path);
//        if (query != null && query.contains("num1=") &&
//            query.contains("num2=")) {
//            return sendSource(query);
//        } else if (path != null && path.contains("/num1") &&
//                   path.contains("/num2")) {
//            return sendSource(path);
//        } else {
//            throw new HTTPException(404);
//        }
//    } catch(Exception e) {
//        e.printStackTrace();
//        throw new HTTPException(500);
//    }
//}
//
//private Source sendSource(String str) {
//    StringTokenizer st = new StringTokenizer(str, "=&/");
//    String token = st.nextToken();
//    int number1 = Integer.parseInt(st.nextToken());
//    st.nextToken();
//    int number2 = Integer.parseInt(st.nextToken());
//    int sum = number1+number2;
//    String body =
//        "<ns:addNumbersResponse xmlns:ns=\"http://duke.org\"><ns:return>"
//        +sum
//        +"</ns:return></ns:addNumbersResponse>";
//    Source source = new StreamSource(
//        new ByteArrayInputStream(body.getBytes()));
//    return source;
//}

  public Source invoke(Source src) {
    throw new HTTPException(500);
//    try {
//    addToCustomerHistory(src);
//    } catch (IOException ioe) {
//      ioe.printStackTrace();
//      throw new HTTPException(500);
//    }
//    return null;
  }
  
  public void addToCustomerHistory(Source src) throws IOException {
    // get the resource to which the customer history is written
    File tmpDir = new File(System.getProperty("user.home")+"/tmp");
    File outputFile = File.createTempFile("soabook",".xml",tmpDir);
    StreamResult res = new StreamResult(outputFile);
    try {
      TransformerFactory.newInstance().newTransformer()
      .transform(src, res);
    } catch (Exception e) {
      throw new IOException(e.getMessage());
    } 
    
  }
 
}
//! </example>

