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

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.javector.soaj.SoajException;
import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;
import com.javector.soaj.util.IOUtil;

/**
 * Provides invocation of EJB30 classes from within the SOA-J module.
 * 
 * @author Mark Hansen (mark@javector.com)
 *
 */
public class SoajEJB30Method extends SoajMethod {
  
  private static SoajLogger log = 
    LoggingFactory.getLogger(SoajEJB30Method.class.getName());
  private String jndiName;
  
  /**
   * @param javaClass
   * @param javaMethod
   * @param paramClass
   * @param jndiName The JNDI name used to look up a reference to the EJB
   * @throws SoajException
   */
  public SoajEJB30Method(String javaClass, String javaMethod,
      List<String> paramClass, String jndiName) {
    super(javaClass, javaMethod, paramClass);
    if ( jndiName == null || jndiName.length() == 0 ) {
      try {
        jndiName = Class.forName(javaClass).getName();
      } catch (ClassNotFoundException e) {
        log.severe(IOUtil.stackTrace(e));
        throw new SoajInvocationRuntimeException(
            "Classloader used in deployment could not find class specified " +
            "by the JNDI name. Check the classpth specified in the SOA-J " +
            "configuration XML.", e);
      }
    }
    this.jndiName = jndiName;
    setClassloader(Thread.currentThread().getContextClassLoader());
    
  }
  
  /*
   * @see com.javector.soaj.deploy.invocation.SoajMethod#getTargetClassInstance()
   */
  @Override
  protected Object getTargetClassInstance() {
    
    try {
      InitialContext ic = new InitialContext();
      return ic.lookup(jndiName);
    } catch (NamingException e) {
      throw new SoajInvocationRuntimeException(e);
    }
    
  }
  
}
