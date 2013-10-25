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
package com.javector.soaj.logging;

import com.javector.soaj.logging.javalogging.LoggingFactoryJavaLoggingImpl;


public abstract class LoggingFactory {
  
  private static LoggingFactory delegate;
  
  static {
    // to add other implementations, use a lookup process to find a delegate
    delegate = new LoggingFactoryJavaLoggingImpl();
  }

  
  public static synchronized  SoajLogger getLogger(String className) {
    return delegate._getLogger(className);
  }
  
  public static synchronized  SoajLogger getLogger(Class clazz) {
    return delegate._getLogger(clazz);
  }
  
  public abstract SoajLogger _getLogger(String className);
  public abstract SoajLogger _getLogger(Class clazz);
  
}

