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
package com.javector.soaj.deploy.invocation;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestInvocation extends TestCase {

  private SoajLogger log = LoggingFactory.getLogger(TestInvocation.class);
  
  public static Test suite() {
      return new TestSuite(TestInvocation.class);
  }

  protected void setUp() throws Exception {}
  
  public void testInvocation() throws Exception {
    HttpClient client = new HttpClient();
    
    log.info("Entered TestInvocation.testInvocation.");
    //TODO factor out the hardcoded http address
    HttpMethod method = new GetMethod("http://localhost:8080/tester/test");
    System.out.println("HTTP GET response code: " + client.executeMethod(method));
    Header hdr = method.getResponseHeader("IntegrationTest_SoajEJB30Method");
    String response = "";
    if ( hdr == null ) {
      // check for exception
      hdr = method.getResponseHeader("IntegrationTest_SoajEJB30Method_Exception");
      throw new Exception(hdr.getValue());
    } else {
      response = hdr.getValue();
    }
    assertEquals("Hello World", response);
    
  }

   public void testInvocationEJB21() throws Exception {
    HttpClient client = new HttpClient();

    log.info("Entered InvocationTest_EJB21.");
    //TODO factor out the hardcoded http address
    HttpMethod method = new GetMethod("http://localhost:8080/tester/test/ejb21");
    System.out.println("HTTP GET response code: " + client.executeMethod(method));
    Header hdr = method.getResponseHeader("IntegrationTest_SoajEJB21Method");
    String response = "";
    if ( hdr == null ) {
      // check for exception
      hdr = method.getResponseHeader("IntegrationTest_SoajEJB21Method_Exception");
      throw new Exception(hdr.getValue());
    } else {
      response = hdr.getValue();
    }
    assertEquals("Hello World", response);

  }

}
