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
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import samples.MySimpleOrder;

public class MySimpleOrderSerializer {

  //! <example xn="ExercisingTheSerializer">
  //! <c>chap05</c><s>annotations</s>
  public static void main(String[] args) throws Exception {
    
    MySimpleOrder myOrder = new MySimpleOrder(
        "John Doe",
        "125 Main Street",
        "Any Town", "NM", "95811",
        "(831) 874-1123");
    myOrder.getItemList().add(new MyItem(6, (float) 2.99, "Diet Coke"));
    myOrder.getItemList().add(new MyItem(4, (float) 3.99, "Potato Chips"));
    myOrder.getItemList().add(new MyItem(2, (float) 5.34, "Frozen Pizza"));
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(MySimpleOrder.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, 
          Boolean.TRUE);
      SchemaFactory sf = 
        SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      Schema schema = sf.newSchema(
          new URL("http://soabook.com/example/oms/simpleorder.xsd"));
      jaxbMarshaller.setSchema(schema);
      ByteArrayOutputStream ba = new ByteArrayOutputStream();
      jaxbMarshaller.marshal(myOrder, ba);
      System.out.println(ba.toString());
      Unmarshaller u = jaxbContext.createUnmarshaller();
      MySimpleOrder roundTripOrder = 
        (MySimpleOrder) u.unmarshal(new StringReader(ba.toString()));
      System.out.println("phone = " + roundTripOrder.getBillTo().phone);
      
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  //! </example>

}
