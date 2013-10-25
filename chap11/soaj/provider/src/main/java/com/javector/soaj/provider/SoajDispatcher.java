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
package com.javector.soaj.provider;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Dispatches SOAP and WSDL requests to the SoajProvider.  For SOAP requests,
 * this servlet captures the target endpoint URL and stores it in context so
 * that SOAJ can resolve the SOAP request to the correct wsdl:port.
 *
 */
public class SoajDispatcher extends HttpServlet {

//  public SoajDispatcher() {
//    super();
//    // TODO Auto-generated constructor stub
//  }

  public void doGet(HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException {
      String queryString = request.getQueryString();
      if("wsdl".equalsIgnoreCase(queryString)){
      RequestDispatcher requestDispatcher = request.getRequestDispatcher("/wsdlgenerator?"+request.getRequestURL().toString());
          requestDispatcher.forward(request,response);
      }else{
         RequestDispatcher requestDispatcher = request.getRequestDispatcher("/provider");
            requestDispatcher.forward(request,response);
      }
  }

     public void doPost(HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException {
      String queryString = request.getQueryString();
      if("wsdl".equalsIgnoreCase(queryString)){
          RequestDispatcher requestDispatcher = request.getRequestDispatcher("/wsdlgenerator?"+request.getRequestURI());
          requestDispatcher.forward(request,response);
      }else{
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/provider");
            requestDispatcher.forward(request,response);
      }
  }

}
