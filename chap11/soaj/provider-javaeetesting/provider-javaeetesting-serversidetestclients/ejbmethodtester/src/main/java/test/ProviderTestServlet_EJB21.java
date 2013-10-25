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

/**
 * Created by IntelliJ IDEA.
 * User: javector
 * Date: Jun 20, 2006
 * Time: 3:28:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProviderTestServlet_EJB21 extends HttpServlet {
    private SoajLogger log = LoggingFactory.getLogger(ProviderTestServlet_EJB21.class);

     public ProviderTestServlet_EJB21() {
       super();
     }

     protected void doGet(HttpServletRequest req, HttpServletResponse res) throws
     ServletException, IOException {

   log.info("Entered ProviderTestServlet_EJB21.doGet");
      test.IntegrationTest_SoajEJB21Method tester =
           new test.IntegrationTest_SoajEJB21Method();
         String testResponse;
         try {
           testResponse = tester.runTest();
         } catch (Exception e) {
        //throw new ServletException("IntegrationTest_SoajEJB30Method Exception.", e);
             log.severe(IOUtil.stackTrace(e));
           res.addHeader("IntegrationTest_SoajEJB21Method_Exception", IOUtil.stackTrace(e));
           return;
         }
         res.addHeader("IntegrationTest_SoajEJB21Method", testResponse);

     }


}

