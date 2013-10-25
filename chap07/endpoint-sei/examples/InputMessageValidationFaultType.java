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

package com.example.faults;

import javax.xml.bind.annotation.AccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

//! <example xn="FAULT_BEAN">
//! <c>chap07</c><s>faults</s>
@XmlAccessorType(AccessType.FIELD)
@XmlType(name = "InputMessageValidationFaultType")
public class InputMessageValidationFaultType {
  
  @XmlAttribute
  protected String msg;
  
  public String getMsg() {
    return msg;
  }
  
  public void setMsg(String value) {
    this.msg = value;
  }
  
}
//! </example>
