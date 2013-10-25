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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Creates a wrapper around an HttpServletRequest that enables modification
 * of the request body.  Used with servler filters so that the request can
 * be modified and passed along to other filters or servlets.
 * 
 * WARNING: The constructor reads the body (via getReader()) of the wrapped
 * HttpServlerRequest and thereby invalidates it for further use within a 
 * container.  It is strongly suggested that you do not use the original
 * HttpServletRequeste after wrapping it in this class.
 * 
 * @author mark@javector.com
 *
 */
public class HttpServletRequestWrapper implements HttpServletRequest {
  
  private HttpServletRequest delegate;
  private StringBuffer body;
  private boolean bufHasBeenRead = false;
  
  public HttpServletRequestWrapper(HttpServletRequest req) throws IOException {

    if (req == null ) {
      throw new NullPointerException();
    }
    delegate = req;
    // copy body to string buffer
    // copy body
    body = new StringBuffer(4000);
    Reader r = req.getReader();
    char[] cbuf = new char[4000];
    int charsRead = r.read(cbuf);
    while ( charsRead > -1 ) {
      body.append(cbuf, 0, charsRead);
      charsRead = r.read(cbuf);
    }
    r.close();

  }

  public String getAuthType() {
    return delegate.getAuthType();
  }

  public Cookie[] getCookies() {
    return delegate.getCookies();
  }

  public long getDateHeader(String arg0) {
    return delegate.getDateHeader(arg0);
  }

  public String getHeader(String arg0) {
    return delegate.getHeader(arg0);
  }

  public Enumeration getHeaders(String arg0) {
    return delegate.getHeaders(arg0);
  }

  public Enumeration getHeaderNames() {
    return delegate.getHeaderNames();
  }

  public int getIntHeader(String arg0) {
    return delegate.getIntHeader(arg0);
  }

  public String getMethod() {
    return delegate.getMethod();
  }

  public String getPathInfo() {
    return delegate.getPathInfo();
  }

  public String getPathTranslated() {
    return delegate.getPathTranslated();
  }

  public String getContextPath() {
return delegate.getContextPath();
  }

  public String getQueryString() {
return delegate.getQueryString();
  }

  public String getRemoteUser() {
    return delegate.getRemoteUser();
  }

  public boolean isUserInRole(String arg0) {
    return delegate.isUserInRole(arg0);
  }

  public Principal getUserPrincipal() {
    return delegate.getUserPrincipal();
  }

  public String getRequestedSessionId() {
    return delegate.getRequestedSessionId();
  }

  public String getRequestURI() {
    return delegate.getRequestURI();
  }

  public StringBuffer getRequestURL() {
    return delegate.getRequestURL();
  }

  public String getServletPath() {
    return delegate.getServletPath();
  }

  public HttpSession getSession(boolean arg0) {
    return delegate.getSession(arg0);
  }

  public HttpSession getSession() {
    return delegate.getSession();
  }

  public boolean isRequestedSessionIdValid() {
    return delegate.isRequestedSessionIdValid();
  }

  public boolean isRequestedSessionIdFromCookie() {
    return delegate.isRequestedSessionIdFromCookie();
  }

  public boolean isRequestedSessionIdFromURL() {
    return delegate.isRequestedSessionIdFromURL();
  }

  public boolean isRequestedSessionIdFromUrl() {
    return delegate.isRequestedSessionIdFromUrl();
  }

  public Object getAttribute(String arg0) {
    return delegate.getAttribute(arg0);
  }

  public Enumeration getAttributeNames() {
    return delegate.getAttributeNames();
  }

  public String getCharacterEncoding() {
    return delegate.getCharacterEncoding();
  }

  public void setCharacterEncoding(String arg0)
      throws UnsupportedEncodingException {
    delegate.setCharacterEncoding(arg0);
  }

  public int getContentLength() {
    return delegate.getContentLength();
  }

  public String getContentType() {
    return delegate.getContentType();
  }

  public ServletInputStream getInputStream() throws IOException {

    if ( bufHasBeenRead ) {
      throw new IllegalStateException(
          "getReader() has already been called for thie request");
    }
    bufHasBeenRead = true;
    return new ServletInputStreamImpl(
        new ByteArrayInputStream(body.toString().getBytes()));
    
  }

  public String getParameter(String arg0) {
    return delegate.getParameter(arg0);
  }

  public Enumeration getParameterNames() {
    return delegate.getParameterNames();
  }

  public String[] getParameterValues(String arg0) {
    return delegate.getParameterValues(arg0);
  }

  public Map getParameterMap() {
    return delegate.getParameterMap();
  }

  public String getProtocol() {
    return delegate.getProtocol();
  }

  public String getScheme() {
    return delegate.getScheme();
  }

  public String getServerName() {
    return delegate.getServerName();
  }

  public int getServerPort() {
    return delegate.getServerPort();
  }

  public BufferedReader getReader() throws IOException {

    if ( bufHasBeenRead ) {
      throw new IllegalStateException(
          "getReader() has already been called for thie request");
    }
    bufHasBeenRead = true;
    return new BufferedReader(new StringReader(body.toString()));
    
  }

  public String getRemoteAddr() {
    return delegate.getRemoteAddr();
  }

  public String getRemoteHost() {
    return delegate.getRemoteHost();
  }

  public void setAttribute(String arg0, Object arg1) {
   delegate.setAttribute(arg0, arg1); 
  }

  public void removeAttribute(String arg0) {
delegate.removeAttribute(arg0);
  }

  public Locale getLocale() {
    return delegate.getLocale();
  }

  public Enumeration getLocales() {
    return delegate.getLocales();
  }

  public boolean isSecure() {
    return delegate.isSecure();
  }

  public RequestDispatcher getRequestDispatcher(String arg0) {
    return delegate.getRequestDispatcher(arg0);
  }

  public String getRealPath(String arg0) {
    return delegate.getRealPath(arg0);
  }

  public int getRemotePort() {
    return delegate.getRemotePort();
  }

  public String getLocalName() {
    return delegate.getLocalName();
  }

  public String getLocalAddr() {
    return delegate.getLocalAddr();
  }

  public int getLocalPort() {
    return delegate.getLocalPort();
  }

  /**
   * Enables you to get the request body in a form that can be read without
   * invoking either getReader() or getInputeStream().
   * 
   * @return the HTTP Request body
   */
  public StringBuffer getBody() {
    return body;
  }

}
