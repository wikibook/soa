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

public class MyItem {

    protected String itmnumber;
    protected String storageloc;
    protected double targetqty;
    protected String targetuom;
    protected BigDecimal priceperuom;
    protected String shorttext;

    public String getITMNUMBER() {
        return itmnumber;
    }

    public void setITMNUMBER(String value) {
        this.itmnumber = value;
    }

    public String getSTORAGELOC() {
        return storageloc;
    }

    public void setSTORAGELOC(String value) {
        this.storageloc = value;
    }

    public double getTARGETQTY() {
        return targetqty;
    }

    public void setTARGETQTY(double value) {
        this.targetqty = value;
    }

    public String getTARGETUOM() {
        return targetuom;
    }

    public void setTARGETUOM(String value) {
        this.targetuom = value;
    }

    public BigDecimal getPRICEPERUOM() {
        return priceperuom;
    }

    public void setPRICEPERUOM(BigDecimal value) {
        this.priceperuom = value;
    }

    public String getSHORTTEXT() {
        return shorttext;
    }

    public void setSHORTTEXT(String value) {
        this.shorttext = value;
    }

}
