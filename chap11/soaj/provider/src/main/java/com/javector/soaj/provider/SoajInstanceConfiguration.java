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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;

import com.javector.soaj.SoajException;
import com.javector.soaj.SoajRuntimeException;
import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;
import com.javector.soaj.util.IOUtil;
import com.javector.soaj.util.ResourceFinder;

/**
 * The configuration for an instance of SOAJ.  Contains the deployment information used to
 * dispatch and invoke Web services.  This class stores the configuration in memory and reads
 * it initially from the classpath at config/SoajConfig.xml.  It enables you to edit the 
 * configuration.  Of course, your changes do not get saved back to the deployment module (EAR, 
 * WAR, etc.), but you can save them yourself to backup the state of the SOAJ instance.
 *
 */
public abstract class SoajInstanceConfiguration {

  // TODO having this static restricts us to one SOAJ instance per JVM/Classloader - need to refactor
  private static SoajLogger log = LoggingFactory.getLogger(SoajInstanceConfiguration.class);
  private static String soajConfig;
  private static long lastModified;
  
  static {
    
    InputStream configStream = 
      ResourceFinder.getResourceAsStream("config/SoajConfig.xml", SoajInstanceConfiguration.class);
    byte[] bytes = new byte[1000];
    int readBytes;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      while ( (readBytes = configStream.read(bytes)) > -1 ) {
        out.write(bytes, 0, readBytes);
      }
    } catch (Throwable t) {
      log.severe("Initialization of SoajInstanceConfiguration Failed." + IOUtil.stackTrace(t));
      throw new SoajRuntimeException("Initialization of SoajInstanceConfiguration Failed.", t);
    }
    soajConfig = out.toString();
    lastModified = new Date().getTime();
    
  }
  
  /**
   * @return a copy of the SOAJ configuration
   */
  public static InputStream getSoajConfigAsStream() {
     return new ByteArrayInputStream(soajConfig.getBytes());
  }
  
  /**
   * @return a copy of the SOAJ configuration
   */
  public static String getSoajConfig() {
     return new String(soajConfig);
  }

  public static long getLastModified() {
    return lastModified;
  }
  
  /**
   * Enables you to dynamically modify the configuration.  Use with care!
   * 
   * @param s The new SOAJ configuration.
   * @throws SoajException 
   */
  protected static void setSoajConfig(String s) throws SoajException {
    
    String configErrors = getConfigurationErrors(s);
    if ( configErrors == null ) {
    soajConfig = s;
    } else {
      throw new SoajException("Tried to save an invalid configuration. " + 
          configErrors);
    }
  }

  private static String getConfigurationErrors(String s) {
    // TODO Auto-generated method stub
    return "not implemented yet.";
  }

}
