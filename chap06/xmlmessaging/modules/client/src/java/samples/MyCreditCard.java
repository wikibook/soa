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
//! <example xn="MyCreditCard">
//! <c>chap06</c><s>xmlmessaging</s>
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BUSOBJ_CCARD", 
    namespace = "http://www.example.com/oms")
public class MyCreditCard {

    protected String CC_TYPE;
    protected String CC_NUMBER;
    protected String CC_EXPIRE_DATE;
    protected String CC_NAME;
    protected BigDecimal BILLAMOUNT;
    protected String CHARGE_DATE;
    
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class OrderCcard {
      
      @XmlElement(namespace = "http://www.example.com/oms")
      protected MyCreditCard ccard;
      
    }
}
//! </example>