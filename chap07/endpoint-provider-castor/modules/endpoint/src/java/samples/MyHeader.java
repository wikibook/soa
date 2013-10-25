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

public class MyHeader {

    protected String salesorg;
    protected String purchdate;
    protected String custno;
    protected String pymtmeth;
    protected String purchordno;
    protected String wardeldate;
    
    public String getCustno() {
      return custno;
    }
    public void setCustno(String custno) {
      this.custno = custno;
    }
    public String getPurchdate() {
      return purchdate;
    }
    public void setPurchdate(String purchdate) {
      this.purchdate = purchdate;
    }
    public String getPurchordno() {
      return purchordno;
    }
    public void setPurchordno(String purchordno) {
      this.purchordno = purchordno;
    }
    public String getPymtmeth() {
      return pymtmeth;
    }
    public void setPymtmeth(String pymtmeth) {
      this.pymtmeth = pymtmeth;
    }
    public String getSalesorg() {
      return salesorg;
    }
    public void setSalesorg(String salesorg) {
      this.salesorg = salesorg;
    }
    public String getWardeldate() {
      return wardeldate;
    }
    public void setWardeldate(String wardeldate) {
      this.wardeldate = wardeldate;
    }



}
