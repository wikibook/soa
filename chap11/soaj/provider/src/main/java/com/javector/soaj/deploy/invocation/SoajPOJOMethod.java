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

import java.util.List;
import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;

/**
 * Provides invocation of POJO classes from within the SOA-J module.
 * 
 * @author Mark Hansen (mark@javector.com)
 *
 */
public class SoajPOJOMethod extends SoajMethod {
  
  private static SoajLogger log = LoggingFactory.getLogger(SoajPOJOMethod.class);
  
  /**
   * @param javaClass
   * @param javaMethod
   * @param paramClass
   * @param cl The ClassLoader used to resolve the javaClass and paramClass 
   *   references.  If null, then the 
   *   Thread.currentThread().getContextClassLoader() is used and the referenced
   *   classes must be available based on the SOA-J module packaging.
   * @throws SoajException
   */
  public SoajPOJOMethod(String javaClass, String javaMethod,
      List<String> paramClass, ClassLoader cl) {
    
    super(javaClass, javaMethod, paramClass);
    if ( cl != null ) {
      setClassloader(cl);
    }
    
  }
  
  /*
   * @see com.javector.soaj.deploy.invocation.SoajMethod#getTargetClassInstance()
   */
  @Override
  protected Object getTargetClassInstance() {
    
    try {
      if ( getTargetClass() == null ) {
        setTargetClass(getClassloader().loadClass(getJavaClass()));
      }
      return getTargetClass().newInstance();
    } catch (ClassNotFoundException e) {
      throw new SoajInvocationRuntimeException(
          "Classloader used in deployment could not find the target class.  " +
          "Check the classpth specified in the SOA-J configuration XML.", e);
    } catch (InstantiationException e) {
      throw new SoajInvocationRuntimeException(
          "Loaded, but cannot instantiate the target class. " +
          "Check that is has a no-arg constructor and is not an interface.", e);
    } catch (IllegalAccessException e) {
      throw new SoajInvocationRuntimeException(
          "IllegalAccessException during deployment. " +
          "Check to see if the deployed method is private or protected.", e);
    }
    
  }
  
}
