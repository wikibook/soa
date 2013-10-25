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
package com.javector.ser.adaptive.util;

import com.javector.ser.adaptive.AdaptiveMap;

import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;
import java.io.File;

import org.jvnet.jaxb.reflection.JAXBModelFactory;
import org.jvnet.jaxb.reflection.model.runtime.RuntimeTypeInfoSet;
import org.jvnet.jaxb.reflection.model.runtime.RuntimeElementInfo;

/**
 * Created by IntelliJ IDEA.
 * Auther: Rohit Agarwal
 * Date: Apr 30, 2006
 * Time: 3:44:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestNameSpaces {
    public static void main(String[] args) throws Exception {
        JAXBContext context = JAXBContext.newInstance(AdaptiveMap.class);
        File file = new File("F:\\rohit_home\\soaj_root\\workspace\\jaxbImplNew\\jaxbimpl\\src\\test\\resources\\com\\javector\\ser\\framework\\SOAAdaptiveSample.xml");
        AdaptiveMap map = (AdaptiveMap)context.createUnmarshaller().unmarshal(file);
        RuntimeTypeInfoSet infoSet = JAXBModelFactory.create(map.getClass());
        QName name = ((RuntimeElementInfo)infoSet.getAllElements().iterator().next()).getElementName();

        System.out.println("name =" + name);
    }
}
