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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BUSOBJ_HEADER",
    namespace = "http://www.example.com/oms")
public class Header {

    @XmlElement(name = "SALES_ORG")
    protected String salesorg;
    @XmlElement(name = "PURCH_DATE")
    protected String purchdate;
    @XmlElement(name = "CUST_NO")
    protected String custno;
    @XmlElement(name = "PYMT_METH")
    protected String pymtmeth;
    @XmlElement(name = "PURCH_ORD_NO")
    protected String purchordno;
    @XmlElement(name = "WAR_DEL_DATE")
    protected String wardeldate;

    public String getSALESORG() {
        return salesorg;
    }

    public void setSALESORG(String value) {
        this.salesorg = value;
    }

    public String getPURCHDATE() {
        return purchdate;
    }

    public void setPURCHDATE(String value) {
        this.purchdate = value;
    }

    public String getCUSTNO() {
        return custno;
    }

    public void setCUSTNO(String value) {
        this.custno = value;
    }

    public String getPYMTMETH() {
        return pymtmeth;
    }

    public void setPYMTMETH(String value) {
        this.pymtmeth = value;
    }

    public String getPURCHORDNO() {
        return purchordno;
    }

    public void setPURCHORDNO(String value) {
        this.purchordno = value;
    }

    public String getWARDELDATE() {
        return wardeldate;
    }

    public void setWARDELDATE(String value) {
        this.wardeldate = value;
    }

}
