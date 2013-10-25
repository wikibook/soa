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
package com.javector.soaj.util.http;

import java.util.ArrayList;
import java.util.List;

import com.javector.soaj.util.IOUtil;

public class HttpRequestDTO {
  
  public List<HeaderDTO> headers = new ArrayList<HeaderDTO>();
  public String method;
  public String requestURL;
  public String body;
  public String queryString;
  
  public String toString() {
    
    String outputString = "";
    outputString += method + IOUtil.NL;
    outputString += requestURL + IOUtil.NL;
    outputString += queryString + IOUtil.NL;
    for (HeaderDTO h : headers) {
      outputString += h.toString() + IOUtil.NL;
    }
    outputString += IOUtil.NL + body;
    return outputString;
    
  }
  
}
