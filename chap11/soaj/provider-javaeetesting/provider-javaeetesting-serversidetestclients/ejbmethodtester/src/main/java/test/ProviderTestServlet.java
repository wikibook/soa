/*
* Copyright 2006-2007 Javector Software LLC
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
package test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;
import com.javector.soaj.util.IOUtil;

public class ProviderTestServlet extends HttpServlet {

 private SoajLogger log = LoggingFactory.getLogger(ProviderTestServlet.class);
  
  public ProviderTestServlet() {
    super();
  }
  
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws
  ServletException, IOException {
    
    log.info("Entered ProviderTestServlet.doGet");
    IntegrationTest_SoajEJB30Method tester =
      new IntegrationTest_SoajEJB30Method();
    String testResponse;
    try {
      testResponse = tester.runTest();
    } catch (Exception e) {
//     throw new ServletException("IntegrationTest_SoajEJB30Method Exception.", e);
     res.addHeader("IntegrationTest_SoajEJB30Method_Exception", IOUtil.stackTrace(e));
     return;
    }
    res.addHeader("IntegrationTest_SoajEJB30Method", testResponse);

  }

}
