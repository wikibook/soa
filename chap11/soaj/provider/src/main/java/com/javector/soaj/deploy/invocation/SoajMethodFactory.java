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

import com.javector.adaptive.util.dto.SOAJOperationDTO;
import com.javector.soaj.SoajRuntimeException;

public class SoajMethodFactory {
  
  public SoajMethod createSoajMethod(SOAJOperationDTO dto) 
  throws SoajRuntimeException {
    
    if ( dto == null ) {
      throw new SoajRuntimeException("DTO is null.");
    }
    switch ( dto.getMethodType() ) {
    case POJO:
      return new SoajPOJOMethod(
          dto.getTargetServiceClassName(),
          dto.getTargetServiceMethodName(),
          dto.getParamClasses(),
          getClasspathFromReference(dto.getClasspathReference()));
    case EJB30:
      return new SoajEJB30Method(
          dto.getTargetServiceClassName(),
          dto.getTargetServiceMethodName(),
          dto.getParamClasses(),
          dto.getJndiName());
    case EJB21:
      return new SoajEJB21Method(
          dto.getTargetServiceClassName(),
          dto.getTargetServiceMethodName(),
          dto.getParamClasses(),
          dto.getJndiName(),
          dto.getHomeInterface(),
          dto.getLocal().booleanValue());
    case UNKNOWN:
      throw new SoajInvocationRuntimeException(
          "Tried to deploy an unsupported type of Java class (i.e., not "+
          "POJO, EJB30, or EJB21).");
    default:
      throw new SoajInvocationRuntimeException("SOAJ Method type not specified.");
    }
    
  }
  
  private ClassLoader getClasspathFromReference(String classpathReference) {
    
    if ( classpathReference == null || classpathReference.length() == 0 ) {
      return null;
    }
    
    // TODO Create a classpath from the reference.
    return null;
  }

}
