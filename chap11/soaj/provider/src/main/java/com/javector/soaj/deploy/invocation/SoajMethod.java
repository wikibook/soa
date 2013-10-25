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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.javector.soaj.SoajRuntimeException;
import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;
import com.javector.soaj.util.IOUtil;

public abstract class SoajMethod {
  
  private static SoajLogger log = 
    LoggingFactory.getLogger(SoajPOJOMethod.class.getName());
  
  private String javaClass;
  private String javaMethod;
  private List<String> paramClass;
  private Object targetClassInstance = null;
  private Method targetMethod = null;
  private Class targetClass = null;
  private ClassLoader classloader = null;
  
  /**
   * @param javaClass Full package and class name.  Must not be null.
   * @param javaMethod Name of the method to be invoked.  Must not be null.
   * @param paramClass Lists of input parameter classes defined by the method.
   *   Must not be null.  If there are no parameters, bhit should be a zero
   *   length list.
   * @throws SoajRuntimeException 
   */
  public SoajMethod(String javaClass, String javaMethod, 
      List<String> paramClass) {
    
    if ( javaClass == null ) {
      throw new SoajRuntimeException("javaClass cannot be null.");
    }
    if ( javaMethod == null ) {
      throw new SoajRuntimeException("javaMethod cannot be null.");
    }
    if ( paramClass == null ) {
      throw new SoajRuntimeException("paramClass cannot be null.");
    }
    this.javaClass = javaClass;
    this.javaMethod = javaMethod;
    this.paramClass = paramClass;
    // default ClassLoader - can be overidden by sub classes
    this.classloader = Thread.currentThread().getContextClassLoader();
    
  }
  
  protected abstract Object getTargetClassInstance() throws SoajInvocationRuntimeException;
  
  /**
   * Invoke the deployed method and return the response.
   * 
   * @param paramObject
   * @return
   * @throws SoajInvocationRuntimeException - indicates an internal problem with the 
   *   SOA-J configuration or runtime.
   * @throws InvocationTargetException - indicates that the deployed method
   *   threw an exception.  This should be mapped by SOA-J to a SOAP fault
   *   message that is returned to the sender.
   */
  public final Object invoke(List<Object> paramObject) 
  throws InvocationTargetException {
    
    checkNumParams(paramObject);  // fail fast if wrong num parameters
    if ( targetMethod == null ) {
      targetMethod = getTargetMethod();
    }
    if ( targetClassInstance == null ) {
      targetClassInstance = getTargetClassInstance(); 
    }
    try {
      return targetMethod.invoke(targetClassInstance, paramObject.toArray());
    } catch (IllegalAccessException e) {
      throw new SoajInvocationRuntimeException(
          "Check to see if the deployed method is private or protected.", e);
    } catch (IllegalArgumentException e) {
      String msg = "";
      for (int i=0; i<getParamClass().size() && msg.length()==0; i++) {
        if (!targetMethod.getParameterTypes()[i].isInstance(paramObject.get(i))) {
          msg="methodParams["+i+"] = "+targetMethod.getParameterTypes()[i]+
          " and object has runtime class = "+paramObject.get(i).getClass();
        }
      }
      throw new SoajInvocationRuntimeException(
          "Check that the parameter Objects are instances of the right types." + 
          IOUtil.NL + msg, e);
    }
    
  }
  
  protected final Method getTargetMethod() {
    
    Class[] methodParams = new Class[getParamClass().size()];
    try {
      if ( targetClass == null ) {
        targetClass = classloader.loadClass(getJavaClass());
      }
      for (int i=0; i<getParamClass().size(); i++) {
        methodParams[i] = classloader.loadClass(getParamClass().get(i));
      }
      return targetClass.getMethod(getJavaMethod(), methodParams);
    } catch (ClassNotFoundException e) {
      throw new SoajInvocationRuntimeException(
          "Classloader used in deployment could not find the specified class."+
          " Check the classpath specified in the SOA-J configuration XML", e);
    } catch (NoSuchMethodException e) {
      log.severe(IOUtil.stackTrace(e));
      throw new SoajInvocationRuntimeException(
          "The method name provided for deployment does not exist on the "+
          "specified class or interface.  Check the SOA-J configuration.", e);
    }
  }
  
  private void checkNumParams(List<Object> paramObject) {
    if ( paramObject.size() != paramClass.size()) {
      throw new SoajInvocationRuntimeException(
          "Tried to invoke a method with the wrong number of parameters.");
    }
  }
  
  public String getJavaClass() {
    return javaClass;
  }
  
  public String getJavaMethod() {
    return javaMethod;
  }
  
  public List<String> getParamClass() {
    return paramClass;
  }

  public ClassLoader getClassloader() {
    return classloader;
  }

  public void setClassloader(ClassLoader classloader) {
    this.classloader = classloader;
  }

  public Class getTargetClass() {
    return targetClass;
  }

  public void setTargetClass(Class targetClass) {
    this.targetClass = targetClass;
  }
  
}
