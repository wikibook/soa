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

import java.io.File;
import java.io.IOException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

//! <example xn="CustomerManagerPOJO_Servlet">
//! <c>chap03</c><s>rest-post</s>
public class CustomerManager {
  
  public void addToCustomerHistory(
      String srcSystem, 
      Source src) throws IOException {
    if ( srcSystem.equals("OMS") ) { // process
      // get the resource to which the customer history is written
      File tmpDir = new File(System.getProperty("user.home")+"/tmp");
      File outputFile = File.createTempFile("soabook",".xml",tmpDir);
      StreamResult res = new StreamResult(outputFile);
      try {
        TransformerFactory.newInstance().newTransformer()
        .transform(src, res);
        res.getOutputStream().close();
      } catch (Exception e) {
        throw new IOException(e.getMessage());
      } 
    } else {
      throw new IOException("SourceSystem: "+srcSystem+" not supported yet.");
    }
  }
}
//! </example>

