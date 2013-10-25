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
import com.javector.adaptive.framework.util.BaseReflectionUtil;

/**
 * Created by IntelliJ IDEA.
 * Auther: Rohit Agarwal
 * Date: May 19, 2006
 * Time: 11:18:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class DeserializeBindingRuleDTO {

    private RuleDTO ruleDTO;
    private ClassLoader cl;
    private Object sourceJaxbObject;
    private Object targetJavaObject;
    private Object childJavaObject;
    private Object propertyValue;
    private BaseReflectionUtil reflectionUtil;

    public BaseReflectionUtil getReflectionUtil() {
        return reflectionUtil;
    }

    public void setReflectionUtil(BaseReflectionUtil reflectionUtil) {
        this.reflectionUtil = reflectionUtil;
    }

    public Object getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Object propertyValue) {
        this.propertyValue = propertyValue;
    }


    public RuleDTO getRuleDTO() {
        return ruleDTO;
    }

    public void setRuleDTO(RuleDTO ruleDTO) {
        this.ruleDTO = ruleDTO;
    }

    public ClassLoader getCl() {
        return cl;
    }

    public void setCl(ClassLoader cl) {
        this.cl = cl;
    }

    public Object getSourceJaxbObject() {
        return sourceJaxbObject;
    }

    public void setSourceJaxbObject(Object sourceJaxbObject) {
        this.sourceJaxbObject = sourceJaxbObject;
    }

    public Object getTargetJavaObject() {
        return targetJavaObject;
    }

    public void setTargetJavaObject(Object targetJavaObject) {
        this.targetJavaObject = targetJavaObject;
    }

    public Object getChildJavaObject() {
        return childJavaObject;
    }

    public void setChildJavaObject(Object childJavaObject) {
        this.childJavaObject = childJavaObject;
    }
}
