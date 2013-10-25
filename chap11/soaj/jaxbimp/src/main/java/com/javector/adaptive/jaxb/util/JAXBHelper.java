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
package com.javector.adaptive.jaxb.util;


import com.javector.soaj.SoajException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by IntelliJ IDEA.
 * User: Kishore G
 * Date: Jan 7, 2006
 * Time: 8:45:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class JAXBHelper {
    void test() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance("");
//        XmlElement

    }

    public static String convertPackageFromXmlType(QName xmlType) throws SoajException {
        StringBuffer stringBuffer = new StringBuffer();
        try {

            String host = new URI(xmlType.getNamespaceURI()).getHost();
            String[] strs = host.split("\\.");
            for (int i = strs.length - 1; i >= 0; i--) {
                String str = strs[i];

                stringBuffer.append(str + ((i == 0) ? "" : "."));
            }
            String path = new URI(xmlType.getNamespaceURI()).getPath();
            stringBuffer.append(path.replace('/', '.'));

        } catch (URISyntaxException e) {
            throw new SoajException("Exception while getting package name from xmlType", e);
        }
        return stringBuffer.toString();
    }

    public static String getFullyQualifiedClassName(QName xmlType) throws SoajException {

        return JAXBHelper.convertPackageFromXmlType(xmlType) + "." +getInitCap(xmlType.getLocalPart());
    }

    public static String getInitCap(String str) {

        return String.valueOf(str.charAt(0)).toUpperCase() + str.substring(1);
    }


}
