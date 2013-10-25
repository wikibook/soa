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

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
//! <example xn="MyRequestOrder">
//! <c>chap06</c><s>xmlmessaging</s>
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "http://www.example.com/req")
@XmlRootElement(name = "requestOrder", 
    namespace = "http://www.example.com/req")
public class MyRequestOrder {

    protected String CUST_NO;
    protected String PURCH_ORD_NO;
    protected MyCreditCard ccard;
    protected List<MyItem> item;

}
//! </example>