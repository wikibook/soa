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
import java.io.File;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

public class MyFooSerializer {

  public static void main(String[] args) throws Exception {
    
    MyFoo x = new MyFoo();
    x.myFooString = "Hello World";
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance("samples");
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
//      SchemaFactory sf = 
//        SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//      Schema schema = sf.newSchema(new File(args[0])); // pass in the schema
//      jaxbMarshaller.setSchema(schema);
//      ByteArrayOutputStream ba = new ByteArrayOutputStream();
//      jaxbMarshaller.marshal(x, ba);
//      System.out.println(ba.toString());
      Unmarshaller u = jaxbContext.createUnmarshaller();
      String fooXML = "<foo><fooString>Hello World</fooString></foo>";
      x = (MyFoo) u.unmarshal(new StringReader(fooXML));
      System.out.println(x.myFooString);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
}
