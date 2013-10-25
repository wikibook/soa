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
package com.javector.adaptive.framework.interfaces;

import javax.xml.namespace.QName;

public class XMLMapping {

    private String javaClass;
    private QName xmlName;
    private QName xmlType;
    
    /**
     * @return the Type QName the XML is a schema type, otherwise the XML is
     * assumed to be a global element, and the qualified name of that global
     * element is returned.
     */
    public QName getTypeOrElementQName() {
        if ( getXmlType() != null ) {
          return getXmlType();
        } 
        return getXmlName();
    }

    public XMLMapping(QName xmlType, QName xmlName, String javaClass) {
        this.javaClass = javaClass;
        this.xmlName = xmlName;
        this.xmlType = xmlType;
    }

    public QName getXmlType() {
        return xmlType;
    }

    public QName getXmlName() {
        return xmlName;
    }

    public String getJavaClassName() {
        return javaClass;
    }

    public String toString() {
        return "xmlType = " + xmlType + ", xmlName = " + xmlName + ", javaClass = " + javaClass;
    }
}
