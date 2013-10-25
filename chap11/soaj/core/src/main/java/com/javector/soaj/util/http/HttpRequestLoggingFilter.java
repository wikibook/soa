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
package com.javector.soaj.util.http;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;
import com.javector.soaj.util.IOUtil;

public class HttpRequestLoggingFilter implements Filter {

SoajLogger log = LoggingFactory.getLogger(HttpRequestLoggingFilter.class);
  
  public void init(FilterConfig arg0) throws ServletException {
    // TODO Auto-generated method stub

  }

  public void doFilter(ServletRequest arg0, ServletResponse arg1,
      FilterChain arg2) throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) arg0;
    HttpServletRequestWrapper wrapper = 
      new HttpServletRequestWrapper(req);
    log.info("HttpServletRequest = " + IOUtil.NL +
        HttpUtil.httpRequestToString(wrapper));
    arg2.doFilter(wrapper, arg1);
    
  }

  public void destroy() {
    // TODO Auto-generated method stub

  }

}
