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

import java.util.logging.Logger;
import java.util.logging.Level;

import com.javector.soaj.logging.SoajLogger;

public class SoajLoggerJavaLoggingImpl implements SoajLogger {
  
  private  Logger loggerInstance;
  
  /**
   * Protected because should only be instantiated by the Factory
   * @param className
   */
  protected SoajLoggerJavaLoggingImpl(String className) {
    loggerInstance = Logger.getLogger(className);
  }
  
  protected SoajLoggerJavaLoggingImpl(Class clazz) {
    loggerInstance = Logger.getLogger(clazz.getName());
  }
  
  public void severe(String msg) {
    loggerInstance.log(Level.SEVERE,msg);
  }

  public void warning(String msg) {
    loggerInstance.log(Level.WARNING,msg);
  }
  
  public void info(String msg) {
    loggerInstance.log(Level.INFO,msg);
  }
  
  public void fine(String msg) {
    loggerInstance.log(Level.FINE,msg);
  }
  
}
