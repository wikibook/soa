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
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;

public class UpdateCustomerHistoryServlet extends HttpServlet {
  
  //! <example xn="UpdateCustomerHistoryServlet">
  //! <c>chap03</c><s>rest-post</s>
  public void doPost(HttpServletRequest req, 
      HttpServletResponse res)
  throws IOException, ServletException {
    res.setContentType("text/xml");
    // nb: need to put some XML in the response or many serlvet containers
    // re-write the Content-Type header to text/html
    PrintWriter pw = res.getWriter();
    pw.println("<?xml version=\"1.0\"?>");  
    pw.println("<voidResponse xmlns=\"http://www.example.com/css\"/>");
    // get stream to read from the XML being posted
    ServletInputStream in = req.getInputStream();
    // get the Source System
    String sourceSystem = req.getParameter("SourceSystem");
    // invoke the method
    CustomerManager cm = new CustomerManager();
    try {
      cm.addToCustomerHistory(sourceSystem, new StreamSource(in));
    } catch (Exception e) {
      // NB: this causes test/html errors for JAX-WS client
      in.close();
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          e.getMessage());
      return;
    }
    in.close();
    // respond with HTTP 200 "OK"
    res.setStatus(HttpServletResponse.SC_OK);
  }
//! </example>
  
}

