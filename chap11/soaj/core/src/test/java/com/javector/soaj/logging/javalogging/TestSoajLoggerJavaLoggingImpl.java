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
package com.javector.soaj.logging.javalogging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;
import com.javector.soaj.util.IOUtil;

public class TestSoajLoggerJavaLoggingImpl extends TestCase {
  
  private LoggingFactory loggingFac;
  private String fileHandlerPattern;
  private boolean skipTest = false;
  
  public static Test suite() {
    return new TestSuite(TestSoajLoggerJavaLoggingImpl.class);
  }
  
  protected void setUp() throws Exception { 
    
    String configClass = System.getProperty("java.util.logging.config.class");
    if ( configClass != null ) {
      skipTest = true;
      System.out.println("SOAJ WARING: Logging not tested because " +
          "configuration is handled by the class: " + configClass +
          " rather than a configuration file.");
    }
    String logPropFileName = 
      System.getProperty("java.util.logging.config.file");
    if ( logPropFileName == null ) {
      logPropFileName = System.getProperty("java.home") + 
      IOUtil.FS + "lib" + IOUtil.FS + "logging.properties";
    }
    if ( !(new File(logPropFileName)).exists() ) {
      skipTest = true;
      System.out.println("SOAJ WARING: Logging not tested because " +
          logPropFileName + " does not exist."); 
    }
    System.out.println("logging.properties location: " + logPropFileName);
    Properties logProperties = new Properties();
    logProperties.load(new FileInputStream(logPropFileName));
    fileHandlerPattern = 
      logProperties.getProperty("java.util.logging.FileHandler.pattern");
    fileHandlerPattern = fileHandlerPattern.replace("%h",IOUtil.USER_HOME);
    System.out.println("fileHandlerPattern = " + fileHandlerPattern);
    loggingFac = new LoggingFactoryJavaLoggingImpl();
    
  }

   public void testLog() throws Exception {
      
     if ( skipTest ) { return; }
     long one_second_ago = (new Date()).getTime() - 1000;
     SoajLogger logger = loggingFac._getLogger(this.getClass());
     String msg = "log_test_timestamp = "+(new Date()).getTime();
     logger.info(msg);
     File logDir = (new File(fileHandlerPattern)).getParentFile();
     String logFileName = (new File(fileHandlerPattern)).getName();
     File[] files = logDir.listFiles(new LogFileNameFilter(logFileName));
     long lastModified = one_second_ago;
     File logFile = null;
     for (File file: files) {
       if ( file.lastModified() > lastModified ) {
       logFile = file;
       lastModified = file.lastModified();
       }
     }
     if ( logFile == null ) {
       fail("Failed to find any recently modified log file. Make sure that your " +
           "logging.properties defines a file handler.");
     }
     assertTrue("Test message not found in log file: " + logFile.getCanonicalPath(), 
         IOUtil.toString(logFile).indexOf(msg) > -1);

   }
   
   private class LogFileNameFilter implements FilenameFilter {
     
     String pattern;
     
     public LogFileNameFilter(String pattern) {
       this.pattern = pattern;
     }
     
     public boolean accept(File dir, String name) {
       
       String regex = pattern.replaceAll("%u.", "[0-9]+\\\\.");
       boolean retVal = Pattern.matches(regex, name);
       return retVal;
       
     }
     
   }
   
}
