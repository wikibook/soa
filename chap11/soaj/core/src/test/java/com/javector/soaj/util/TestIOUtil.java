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
package com.javector.soaj.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestIOUtil extends TestCase {

  public static Test suite() {
    return new TestSuite(TestIOUtil.class);
}

protected void setUp() throws Exception {}


   public void testFileToString() throws Exception {

     String tmpDir = System.getProperty("java.io.tmpdir");
     String testFileName = tmpDir + IOUtil.FS + "ioutil_test" + (new Date()).getTime();
     File testFile = new File(testFileName);
     testFile.createNewFile();
     FileWriter writer = new FileWriter(testFile);
     String testMessage = "test message " + (new Date()).getTime();
     writer.write(testMessage);
     writer.close();
     String result = IOUtil.toString(testFile);
     assertEquals(testMessage, result);     
    
   }
   
   public void testCopy() throws Exception {

     String msg = "1@# _ / ## hello world" + (new Date()).getTime();
     ByteArrayInputStream bais = 
       new ByteArrayInputStream(msg.getBytes());
     ByteArrayOutputStream baos = new ByteArrayOutputStream();
     IOUtil.copy(bais, baos);
     assertEquals(msg, baos.toString());
     
   }
   
   public void testStackTrace() throws Exception {
     
     String throwableMsg = "test message to throw";
     Throwable t = new Throwable(throwableMsg);
     String stackTrace = IOUtil.stackTrace(t);
     assertTrue(stackTrace.indexOf(throwableMsg) > -1);
     
   }
   
   public void testInputStreamToString() throws Exception {

     String msg = "sld 55 &&% $$ !! {}{}";
     ByteArrayInputStream in = new ByteArrayInputStream(msg.getBytes());
     String result = IOUtil.toString(in);
     assertEquals(msg, result);     
    
   }
   

}
