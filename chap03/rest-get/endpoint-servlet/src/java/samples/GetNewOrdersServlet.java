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

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

public class GetNewOrdersServlet extends HttpServlet {
  
  //! <example xn="GetNewOrdersServlet">
  //! <c>chap03</c><s>rest-get</s>
  public void doGet(HttpServletRequest req, 
      HttpServletResponse res)
  throws IOException, ServletException {
    // invoke the Java method
    OrderManager om = new OrderManager();
    Source src = om.getNewOrders();
    // write the file to the HTTP response stream
    ServletOutputStream out = res.getOutputStream();
    res.setContentType("text/xml");
    StreamResult strRes = new StreamResult(out);
    try {
      TransformerFactory.newInstance().newTransformer()
      .transform(src, strRes);
    } catch (Exception e) {
      throw new IOException(e.getMessage());
    } 
    out.close();    
  }
  //! </example>
  
  public void doPost(HttpServletRequest req, 
      HttpServletResponse res)
  throws IOException, ServletException {
    doGet(req, res);
  }
}

