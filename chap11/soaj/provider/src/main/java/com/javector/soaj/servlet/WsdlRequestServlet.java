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
package com.javector.soaj.servlet;

import com.javector.soaj.SoajException;
import com.javector.soaj.provider.processing.RequestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: javector
 * Date: May 11, 2006
 * Time: 8:42:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class WsdlRequestServlet extends HttpServlet {

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {
        try {
            RequestController requestController = new RequestController();
            requestController.handleWsdlRequest(request);
        } catch (SoajException e) {
            throw new ServletException("Exception while servicing the wsdl request", e);
        }

    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        try {
            RequestController requestController = new RequestController();
            String responseWSDL = requestController.handleWsdlRequest(request);
            if ( responseWSDL == null ) {
              responseWSDL = "Generated WSDL is null.  This is probably a SOAJ bug.";
            }
            PrintWriter out = response.getWriter();
            out.write(responseWSDL);
            out.flush();

        } catch (SoajException e) {
            throw new ServletException("Exception while servicing the wsdl request", e);
        }

    }


}
