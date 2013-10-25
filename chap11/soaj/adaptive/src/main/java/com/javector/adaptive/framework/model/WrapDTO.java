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
package com.javector.adaptive.framework.model;

/**
 * Created by IntelliJ IDEA.
 * User: rohit agarwal
 * Date: Apr 29, 2006
 * Time: 7:16:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class WrapDTO {

    private JavaWrapDTO javaWrapDTO;
    private XmlWrapDTO xmlWrapDTO;

    public JavaWrapDTO getJavaWrapDTO() {
        return javaWrapDTO;
    }

    public void setJavaWrapDTO(JavaWrapDTO javaWrapDTO) {
        this.javaWrapDTO = javaWrapDTO;
    }

    public XmlWrapDTO getXmlWrapDTO() {
        return xmlWrapDTO;
    }

    public void setXmlWrapDTO(XmlWrapDTO xmlWrapDTO) {
        this.xmlWrapDTO = xmlWrapDTO;
    }

}
