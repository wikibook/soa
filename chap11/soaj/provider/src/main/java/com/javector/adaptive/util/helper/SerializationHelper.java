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
package com.javector.adaptive.util.helper;

import com.javector.adaptive.framework.interfaces.AdaptiveContext;
import com.javector.adaptive.framework.interfaces.XMLMapping;
import com.javector.soaj.SoajException;
import com.javector.soaj.util.XmlUtil;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;

public class SerializationHelper {
  /**
   *
   * @param retObject
   * @param soajOperationDTO
   * @param adaptiveContext
   * @return
   * @throws SOAException
   */
//public static Source doSerialization(Object retObject, SOAJOperationDTO soajOperationDTO, AdaptiveContext adaptiveContext) throws SOAException {
//String returnJavaClass = soajOperationDTO.getReturnJavaClass();
//QName returnType = soajOperationDTO.getReturnType();
//XMLMapping xmlMapping = new XMLMapping(returnType,returnType,returnJavaClass);
//return adaptiveContext.serializeAsSource(retObject, xmlMapping);
//}

  /**
   * 
   * @param object
   * @param javaClassName
   * @param qNameType
   * @param qnameName
   * @param adaptiveContext
   * @return
   * @throws SoajException
   */
  public static String doSerialization(Object object, String javaClassName, QName qNameType, QName qnameName, AdaptiveContext adaptiveContext) throws SoajException {
    XMLMapping xmlMapping = new XMLMapping(qNameType,qnameName,javaClassName);
    Source source = adaptiveContext.serializeAsSource(object, xmlMapping);
    return XmlUtil.toString(source);
  }
}
