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

package com.example.req;

import javax.xml.ws.WebFault;
import com.example.faults.InputMessageValidationFaultType;

//! <example xn="INPUT_FAULT">
//! <c>chap07</c><s>faults</s>
@WebFault(name = "inputMessageValidationFault", 
    targetNamespace = "http://www.example.com/faults")
    public class InputFault
    extends Exception {
  
  private InputMessageValidationFaultType faultInfo;
  
  public InputFault(String message, InputMessageValidationFaultType faultInfo) {
    super(message);
    this.faultInfo = faultInfo;
  }
//! </example> 
  
  public InputFault(String message, InputMessageValidationFaultType faultInfo,
      Throwable cause) {
    super(message, cause);
    this.faultInfo = faultInfo;
  }
  
  /**
   * 
   * @return
   *     returns fault bean: com.example.faults.InputMessageValidationFaultType
   */
  public InputMessageValidationFaultType getFaultInfo() {
    return faultInfo;
  }
  
}
