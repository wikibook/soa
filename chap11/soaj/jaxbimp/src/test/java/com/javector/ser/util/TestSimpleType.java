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
package com.javector.ser.util;

import com.javector.soaj.SoajException;

import org.jvnet.jaxb.reflection.model.runtime.RuntimeTypeInfoSet;
import org.jvnet.jaxb.reflection.model.runtime.RuntimeClassInfo;
import org.jvnet.jaxb.reflection.JAXBModelFactory;

/**
 * Created by IntelliJ IDEA.
 * Auther: Rohit Agarwal
 * Date: May 6, 2006
 * Time: 1:18:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestSimpleType {

    public static void main(String[] args) throws Exception {
    String data = "rohit";
    System.out.println("is simple type = " + isSimpleType(data));
    }


    public  static boolean isSimpleType(Object parentJaxbObject) throws SoajException {
        try {
            RuntimeTypeInfoSet runtimeTypeInfoSet;
            runtimeTypeInfoSet = JAXBModelFactory.create(parentJaxbObject.getClass());
            RuntimeClassInfo typeInfo;
            typeInfo = (RuntimeClassInfo) runtimeTypeInfoSet.getTypeInfo(parentJaxbObject.getClass());
            return typeInfo.isSimpleType();

        } catch (Exception e) {
            throw new SoajException("Exception while checking if the jaxb object is of simple type", e);
        }

    }
}
