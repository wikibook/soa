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
package com.javector.soaj.filter;

import com.javector.soaj.logging.SoajLogger;
import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.provider.processing.RequestController;
import com.javector.soaj.SoajException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: javector
 * Date: Jun 9, 2006
 * Time: 6:23:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestFilter implements Filter {
    private static SoajLogger log = LoggingFactory.getLogger(RequestFilter.class);
    private FilterConfig filterConfig = null;
    public void init(FilterConfig filterConfig) throws ServletException {
       this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            log.info("Within Request Filter ... ");

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String queryString =httpServletRequest.getQueryString();

        String initParamValue = null;
        Enumeration initParameterNames = filterConfig.getInitParameterNames();
        while(initParameterNames.hasMoreElements()){
            String name= (String)initParameterNames.nextElement();
            initParamValue = filterConfig.getInitParameter(name);
        }

        if(initParamValue.equalsIgnoreCase(queryString)){
           log.info("wsdl generation request");
           filterChain.doFilter(servletRequest,servletResponse);
        }else{
            log.info("service request");
            RequestDispatcher requestDispatcher = httpServletRequest.getRequestDispatcher("/soaj-provider");
            requestDispatcher.forward(servletRequest,servletResponse);

        }

    }

    public void destroy() {
    this.filterConfig = null;
    }
}
