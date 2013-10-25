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
 * User: javector
 * Date: Apr 4, 2006
 * Time: 9:30:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConfigurationDetailDTO {

    private String configurationContextClassName = "";
    private String xpathImplClassName = "";
    private String reflectionUtilClassName = "";
    private String simpleTypeSerializer = "";
    private String complexTypeSerializer =  "";
    private String BaseAdaptiveSerializer = "";
    private String simpleTypeDeserializer = "";
    private String complexTypeDeserializer = "";
    private String BaseAdaptiveDeserializer = "";

    public String getSimpleTypeSerializer() {
        return simpleTypeSerializer;
    }

    public void setSimpleTypeSerializer(String simpleTypeSerializer) {
        this.simpleTypeSerializer = simpleTypeSerializer;
    }

    public String getComplexTypeSerializer() {
        return complexTypeSerializer;
    }

    public void setComplexTypeSerializer(String complexTypeSerializer) {
        this.complexTypeSerializer = complexTypeSerializer;
    }

    public String getBaseAdaptiveSerializer() {
        return BaseAdaptiveSerializer;
    }

    public void setBaseAdaptiveSerializer(String baseAdaptiveSerializer) {
        BaseAdaptiveSerializer = baseAdaptiveSerializer;
    }

    public String getSimpleTypeDeserializer() {
        return simpleTypeDeserializer;
    }

    public void setSimpleTypeDeserializer(String simpleTypeDeserializer) {
        this.simpleTypeDeserializer = simpleTypeDeserializer;
    }

    public String getComplexTypeDeserializer() {
        return complexTypeDeserializer;
    }

    public void setComplexTypeDeserializer(String complexTypeDeserializer) {
        this.complexTypeDeserializer = complexTypeDeserializer;
    }

    public String getBaseAdaptiveDeserializer() {
        return BaseAdaptiveDeserializer;
    }

    public void setBaseAdaptiveDeserializer(String baseAdaptiveDeserializer) {
        BaseAdaptiveDeserializer = baseAdaptiveDeserializer;
    }

    public String getConfigurationContextClassName() {
        return configurationContextClassName;
    }

    public void setConfigurationContextClassName(String configurationContextClassName) {
        this.configurationContextClassName = configurationContextClassName;
    }

    public String getXpathImplClassName() {
        return xpathImplClassName;
    }

    public void setXpathImplClassName(String xpathImplClassName) {
        this.xpathImplClassName = xpathImplClassName;
    }

    public String getReflectionUtilClassName() {
        return reflectionUtilClassName;
    }



    public void setReflectionUtilClassName(String reflectionUtilClassName) {
        this.reflectionUtilClassName = reflectionUtilClassName;
    }
}
