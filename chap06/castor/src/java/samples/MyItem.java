/*
* Copyright 2006 Javector Software LLC
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
package samples;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BUSOBJ_ITEM", propOrder = {
    "itmnumber",
    "storageloc",
    "targetqty",
    "targetuom",
    "priceperuom",
    "shorttext"
})
public class MyItem {

    @XmlElement(name = "ITM_NUMBER", namespace = "http://www.example.com/oms")
    protected String itmnumber;
    @XmlElement(name = "STORAGE_LOC", namespace = "http://www.example.com/oms")
    protected String storageloc;
    @XmlElement(name = "TARGET_QTY", namespace = "http://www.example.com/oms")
    protected double targetqty;
    @XmlElement(name = "TARGET_UOM", namespace = "http://www.example.com/oms")
    protected String targetuom;
    @XmlElement(name = "PRICE_PER_UOM", namespace = "http://www.example.com/oms")
    protected BigDecimal priceperuom;
    @XmlElement(name = "SHORT_TEXT", namespace = "http://www.example.com/oms")
    protected String shorttext;

    /**
     * Gets the value of the itmnumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITMNUMBER() {
        return itmnumber;
    }

    /**
     * Sets the value of the itmnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITMNUMBER(String value) {
        this.itmnumber = value;
    }

    /**
     * Gets the value of the storageloc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTORAGELOC() {
        return storageloc;
    }

    /**
     * Sets the value of the storageloc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTORAGELOC(String value) {
        this.storageloc = value;
    }

    /**
     * Gets the value of the targetqty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public double getTARGETQTY() {
        return targetqty;
    }

    /**
     * Sets the value of the targetqty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTARGETQTY(double value) {
        this.targetqty = value;
    }

    /**
     * Gets the value of the targetuom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTARGETUOM() {
        return targetuom;
    }

    /**
     * Sets the value of the targetuom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTARGETUOM(String value) {
        this.targetuom = value;
    }

    /**
     * Gets the value of the priceperuom property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPRICEPERUOM() {
        return priceperuom;
    }

    /**
     * Sets the value of the priceperuom property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPRICEPERUOM(BigDecimal value) {
        this.priceperuom = value;
    }

    /**
     * Gets the value of the shorttext property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSHORTTEXT() {
        return shorttext;
    }

    /**
     * Sets the value of the shorttext property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSHORTTEXT(String value) {
        this.shorttext = value;
    }

}
