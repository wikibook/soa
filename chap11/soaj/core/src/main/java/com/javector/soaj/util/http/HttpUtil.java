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
import java.io.Reader;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * Utilities for working with Http related object.
 * @author mark@javector.com
 *
 */
public abstract class HttpUtil {
  
  /**
   * Copies an HttpServletRequestWrapper.  Uses the wrapper's string buffer
   * so that the can be reused within a container.  I.e., it does not invoke
   * the getReader or getInputStream methods.
   *
   * @param req
   * @return
   * @throws IOException
   */
  public static final synchronized String httpRequestToString(
      HttpServletRequestWrapper req) throws IOException {

    return toHttpRequestDTO(req).toString();

  }
  
  /**
   * Copies an HttpServletRequestWrapper.  Uses the wrapper's string buffer
   * so that the can be reused within a container.  I.e., it does not invoke
   * the getReader or getInputStream methods.
   * 
   * @param req
   * @return
   * @throws IOException
   */
  public static final synchronized HttpRequestDTO toHttpRequestDTO(
      HttpServletRequestWrapper req) throws IOException {
    
    HttpRequestDTO dto = new HttpRequestDTO();
    
    dto.method = req.getMethod();
    StringBuffer url = req.getRequestURL();
    dto.requestURL = (url == null ? null : url.toString());
    dto.queryString = req.getQueryString();
    
    // copy headers
    dto.headers = new ArrayList<HeaderDTO>();
    String hdrName;
    Enumeration hdrNames = req.getHeaderNames();
    while (hdrNames != null && hdrNames.hasMoreElements()) {
      hdrName = (String) hdrNames.nextElement();
      Enumeration values = req.getHeaders(hdrName);
      String value;
      while (values != null && values.hasMoreElements()) {
        value = (String) values.nextElement();
        dto.headers.add(new HeaderDTO(hdrName,value));
      }
    }
    
    // copy body
    dto.body = req.getBody().toString();
    
    return dto;
    
  }
  


}
