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

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

public class TestXform {

  public static void main(String[] args) throws Exception {

	    String ordersFile = args[0];
	    String outputFile = args[1];
	    String xsltFile = args[2];
	    	    // Instantiate a Transformer using our XSLT file
	    Transformer transformer = 
	      TransformerFactory.newInstance().newTransformer
	      (new StreamSource(new File(xsltFile)));
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    Source src = new StreamSource(new File(ordersFile));
	    Result res = new StreamResult(new File(outputFile));
	    transformer.transform(src, res);

  }

}
