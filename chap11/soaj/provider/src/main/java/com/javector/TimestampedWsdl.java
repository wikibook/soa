/*
* Copyright 2006-2007 Javector Software LLC
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
package com.javector;

/**
 * A WSDL that is timestamped to indicated when it was generated.  WSDLs need to be
 * timestamped, because they are regenerated with the SOAJ configuration changes. 
 * This enables SOAJ to be dynamically reconfigured.
 */
public class TimestampedWsdl {
  
  private String wsdl;
  private long timestamp;
  
  public TimestampedWsdl(String wsdl, long timestamp) {
    super();
    this.wsdl = wsdl;
    this.timestamp = timestamp;
  }
  
  public long getTimestamp() {
    return timestamp;
  }
  public String getWsdl() {
    return wsdl;
  }

}
