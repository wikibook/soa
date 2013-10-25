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
package com.javector.adaptive.framework.dto;

import com.javector.adaptive.framework.model.RuleDTO;

/**
 * Created by IntelliJ IDEA.
 * Auther: Rohit Agarwal
 * Date: May 6, 2006
 * Time: 10:29:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class SerializeBindingRuleDTO {

    private RuleDTO  ruleDTO;
    private Object propertyValue;
    private ClassLoader cl;
    private Object targetJaxbObject;
    private Object childJaxbObject;
    private Object sourceJavaObject;

    public Object getSourceJavaObject() {
        return sourceJavaObject;
    }

    public void setSourceJavaObject(Object sourceJavaObject) {
        this.sourceJavaObject = sourceJavaObject;
    }

    public Object getChildJaxbObject() {
        return childJaxbObject;
    }

    public void setChildJaxbObject(Object childJaxbObject) {
        this.childJaxbObject = childJaxbObject;
    }

    public RuleDTO getRuleDTO() {
        return ruleDTO;
    }

    public void setRuleDTO(RuleDTO ruleDTO) {
        this.ruleDTO = ruleDTO;
    }

    public Object getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Object propertyValue) {
        this.propertyValue = propertyValue;
    }

    public ClassLoader getCl() {
        return cl;
    }

    public void setCl(ClassLoader cl) {
        this.cl = cl;
    }

    public Object getTargetJaxbObject() {
        return targetJaxbObject;
    }

    public void setTargetJaxbObject(Object targetJaxbObject) {
        this.targetJaxbObject = targetJaxbObject;
    }

}
