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
import javax.xml.bind.annotation.AccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(AccessType.FIELD)
@XmlType(namespace = "http://www.example.com/oms")
@XmlRootElement(name = "Order", namespace = "http://www.example.com/oms")
public class OrderType {
  
  protected String OrderKey;
  protected Header OrderHeader;
  @XmlElementWrapper(name="OrderItems")
  protected List<MyItem> item;
  @XmlJavaTypeAdapter(WrapCcardAdapter.class)
  protected MyCreditCard OrderCcard;
  protected String OrderText;
  
}
