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

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

public class Tester {


  public static void main(String[] args) throws Exception {

    JAXBContext jc = JAXBContext.newInstance(Test.class);
    Marshaller m = jc.createMarshaller();
    Unmarshaller u = jc.createUnmarshaller();
    String xml = "<myElement>"+
    "<myCollection>stuff</myCollection>"+
    "<myCollection>stuff</myCollection>"+
    "</myElement>";
    JAXBElement<Test> testElt = 
      u.unmarshal(new StreamSource(new StringReader(xml)), Test.class);
    Test t = testElt.getValue();
    System.out.println("t.myCollection.size() = " + t.myCollection.size());
    System.out.println("t.myCollection.getClass() = " + t.myCollection.getClass());
    for (Object o : t.myCollection) {
      System.out.println("o.getClass() = " + o.getClass());      
    }
    
    m.marshal(testElt, System.out);
    
  }

}
