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

import javax.xml.namespace.QName;
import java.util.Map;
import java.util.HashMap;

public class RuleDTO {
    String javaName;
    String javaClass;
    QName xmlName;
    QName xmlType;
//    String type;
    boolean isElement;
    boolean isAttribute;


    ScriptDTO scriptDTO;
//    String restrictTo;
    boolean isSerialization;
    boolean isDeserialization;
    private boolean field;
    private boolean method;
    private String handler;
    private WrapDTO wrapDTO;
    private Map<String,String> propertyMappings = new HashMap<String, String>();

    public Map<String, String> getPropertyMappings() {
        return propertyMappings;
    }

    public WrapDTO getWrapDTO() {
        return wrapDTO;
    }

    public void setWrapDTO(WrapDTO wrapDTO) {
        this.wrapDTO = wrapDTO;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(String javaClass) {
        this.javaClass = javaClass;
    }

    public String getJavaName() {
        return javaName;
    }

    public void setJavaName(String javaName) {
        this.javaName = javaName;
    }

    public QName getXmlName() {
        return xmlName;
    }

    public void setXmlName(QName xmlName) {
        this.xmlName = xmlName;
    }

    public boolean isElement() {
        return isElement;
    }

    public void setElement(boolean element) {
        isElement = element;
    }

    public boolean isAttribute() {
        return isAttribute;
    }

    public void setAttribute(boolean attribute) {
        isAttribute = attribute;
    }


    public ScriptDTO getScriptDTO() {
        return scriptDTO;
    }

    public void setScriptDTO(ScriptDTO scriptDTO) {
        this.scriptDTO = scriptDTO;
    }

    public boolean isSerialization() {
        return isSerialization;
    }

    public void setSerialization(boolean serialization) {
        isSerialization = serialization;
    }

    public boolean isDeserialization() {
        return isDeserialization;
    }

    public void setDeserialization(boolean deserialization) {
        isDeserialization = deserialization;
    }

    public QName getXmlType() {
        return xmlType;
    }

    public void setXmlType(QName xmlType) {
        this.xmlType = xmlType;
    }

    public void setField(boolean field) {
        this.field = field;
    }

    public void setMethod(boolean method) {
        this.method = method;
    }

    public boolean isField() {
        return field;
    }

    public boolean isMethod() {
        return method;
    }
}
