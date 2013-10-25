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

import com.javector.adaptive.framework.model.RuleDTO;

import javax.xml.namespace.QName;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Kishore G
 * Date: Jan 7, 2006
 * Time: 1:36:31 PM
 * To change this template use File | Settings | File Templates.
 */

public class StrategyDTO {
    RuleDTO[] ruleDTOs;
    String javaClass;
    QName xmlName;
    QName xmlType;
    Map<String,String> propertyMappings = new HashMap<String,String>();

    public Map<String,String> getPropertyMappings() {
        return propertyMappings;
    }


    public QName getXmlType() {
        return xmlType;
    }

    public void setXmlType(QName xmlType) {
        this.xmlType = xmlType;
    }


    public RuleDTO[] getRuleDTOs() {
        return ruleDTOs;
    }

    public void setRuleDTOs(RuleDTO[] ruleDTOs) {
        this.ruleDTOs = ruleDTOs;
    }

    public String getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(String javaClass) {
        this.javaClass = javaClass;
    }

    public QName getXmlName() {
        return xmlName;
    }

    public void setXmlName(QName xmlName) {
        this.xmlName = xmlName;
    }
}
