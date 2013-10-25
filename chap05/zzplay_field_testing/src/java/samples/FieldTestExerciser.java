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

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


import samples.FieldTest;


public class FieldTestExerciser {

  public static void main(String[] args) throws Exception {
    
    FieldTest ft = new FieldTest("hello world");
//    ft.p1 = "hello world";
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(FieldTest.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
      ByteArrayOutputStream ba = new ByteArrayOutputStream();
      jaxbMarshaller.marshal(ft, ba);
      System.out.println(ba.toString());
      Unmarshaller u = jaxbContext.createUnmarshaller();
      ft = (FieldTest) u.unmarshal(new StringReader(ba.toString()));
      System.out.println(ft.getP1());
      
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }

}
