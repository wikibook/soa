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
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import com.javector.soaj.SoajException;
import com.javector.soaj.util.IOUtil;
import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;

import javax.naming.NamingException;
import javax.naming.InitialContext;

/**
 * Provides invocation of EJB21 classes from within the SOAJ module.
 *
 * @author Mark Hansen (mark@javector.com)
 *
 */
public class SoajEJB21Method extends SoajMethod {

  private static SoajLogger log =
    LoggingFactory.getLogger(SoajEJB21Method.class.getName());
  private String jndiName;

  //TODO home and local parameters are not used.  aren't they required?
  
  /**
   * @param javaClass
   * @param javaMethod
   * @param paramClass
   * @param jndiName The JNDI name used to look up a reference to the EJB
   * @param homeInterface
   * @param local
   * @throws SoajException
   */
  public SoajEJB21Method(String javaClass, String javaMethod,
                         List<String> paramClass, String jndiName, String homeInterface,
                         boolean local) {

    super(javaClass, javaMethod, paramClass);
    if ( jndiName == null || jndiName.length() == 0 ) {
      throw new SoajInvocationRuntimeException(
          "JNDI name not specified in configuration for EJB21 class.");
    }
    this.jndiName = jndiName;
    setClassloader(Thread.currentThread().getContextClassLoader());

  }

  /*
  * @see com.javector.soaj.deploy.invocation.SoajMethod#getTargetClassInstance()
  */
  @Override
  protected Object getTargetClassInstance() throws SoajInvocationRuntimeException {

  InitialContext ic;
  try {
  ic = new InitialContext();
      Object o = ic.lookup(jndiName);
      Method method = o.getClass().getMethod("create", new Class[]{});
     return method.invoke(o, new Object[]{});
  } catch (NamingException e) {
  log.severe(IOUtil.stackTrace(e));
  throw new SoajInvocationRuntimeException(e);
  } catch (IllegalAccessException e) {
       log.severe(IOUtil.stackTrace(e));
  throw new SoajInvocationRuntimeException(e);
  } catch (NoSuchMethodException e) {
      log.severe(IOUtil.stackTrace(e));
  throw new SoajInvocationRuntimeException(e);
  } catch (InvocationTargetException e) {
      log.severe(IOUtil.stackTrace(e));
  throw new SoajInvocationRuntimeException(e);
  }

  }

}
