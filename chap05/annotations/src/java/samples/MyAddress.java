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

//! <example xn="MyAddress">
//! <c>chap05</c><s>annotations</s>
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", 
    propOrder = {"name", "street", "city", "state", "zip", "phone"})
public class MyAddress {
  
  @XmlElement(namespace = "http://www.example.com/oms")
  protected String name;
  @XmlElement(namespace = "http://www.example.com/oms")
  protected String street;
  @XmlElement(namespace = "http://www.example.com/oms")
  protected String city;
  @XmlElement(namespace = "http://www.example.com/oms")
  protected String state;
  @XmlElement(namespace = "http://www.example.com/oms")
  protected String zip;
  @XmlElement(namespace = "http://www.example.com/oms")
  protected String phone;

  // need a no-arg constructor
  public MyAddress() {};
//! </example>
  
  public MyAddress(String name, String street, String city, String state,
      String zip, String phone) {

    this.name = name;
    this.street = street;
    this.city = city;
    this.state = state;
    this.zip = zip;
    this.phone = phone;
    
  }
  
}
