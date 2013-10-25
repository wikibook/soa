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

import java.util.Date;
import java.util.Hashtable;

import com.javector.adaptive.util.dto.SOAConfigDTO;
import com.javector.soaj.SoajException;

/**
 * Implements a cache for storing generated WSDLs.  This class is designed to enhance
 * the performance of WSDL lookups by only invoking the WSDL generator the first time
 * a WSDL is requested, or when the SOAJ configuration has changed since the last time
 * that the WSDL was generated.
 *
 */
public abstract class WsdlCache {

  private static Hashtable<String, TimestampedWsdl> wsdlCache = 
    new Hashtable<String, TimestampedWsdl>();
  private static final Object preOperation_monitor = new Object();
  
  /**
   * @param requestedWsdlPath the path where some WSDL id deployed by the SOAJ configuration
   * @param soajConfigDto the SOAJ configuration used to generated WSDL (if cache is empty or
   *   out of date)
   * @return the generated WSDL that has been cached or generated and cached.
   * @throws SoajException if there is no Web service deployed at the requestedWsdlPath
   */

  public static TimestampedWsdl getWsdl(String requestedWsdlPath, SOAConfigDTO soajConfigDto) 
  throws SoajException {
    
    synchronized (preOperation_monitor) {

      TimestampedWsdl tsWsdl = wsdlCache.get(requestedWsdlPath);
      if ( tsWsdl == null ||
          soajConfigDto.getLastModified() > tsWsdl.getTimestamp() ) {
        tsWsdl = new TimestampedWsdl(
            new WsdlGenerator().generateWsdl(requestedWsdlPath, soajConfigDto),
            new Date().getTime());
        wsdlCache.put(requestedWsdlPath, tsWsdl);
      }
      return tsWsdl;

    }
    
  }
    


  /**
   * @param requestedWsdlPath the path where the WSDL is deployed by the SOAJ configuration
   * @param wsdlString the generated WSDL to cache
   * @return the old timestamp or 0 if there was no existing WSDL cached
   */
  public static long setWsdl(String requestedWsdlPath, String wsdlString) {

    TimestampedWsdl tsWsdl = new TimestampedWsdl(wsdlString, new Date().getTime());
    TimestampedWsdl oldTsWsdl = wsdlCache.put(requestedWsdlPath, tsWsdl);
    return ( oldTsWsdl == null ? 0 : oldTsWsdl.getTimestamp() );
    
  }

}
