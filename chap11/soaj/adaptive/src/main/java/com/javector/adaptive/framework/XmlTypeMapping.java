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
package com.javector.adaptive.framework;

import com.javector.adaptive.framework.interfaces.Mapping;
import com.javector.soaj.SoajException;

import javax.xml.namespace.QName;

/**
 * Created by IntelliJ IDEA.
 * User: Kishore G
 * Date: Jan 13, 2006
 * Time: 9:21:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class XmlTypeMapping extends Mapping {

    public XmlTypeMapping(QName xmlType,String javaClassName ) {
        super(xmlType,javaClassName);
    }

    public QName getXmlType() {
        return typeOrName;
    }

    public QName getXmlName() throws SoajException {
        throw new SoajException("This method should not be called on this TypeMappinginstance");
    }

    public String getJavaClassName() {
        return javaClassName;
    }

}
