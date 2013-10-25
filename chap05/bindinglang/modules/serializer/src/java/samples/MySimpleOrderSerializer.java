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
import java.math.BigInteger;
import java.net.URL;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import samples.MySimpleOrder;

public class MySimpleOrderSerializer {

  //! <example xn="ExercisingTheSerializer">
  //! <c>chap05</c><s>bindinglang</s>
  public static void main(String[] args) throws Exception {
    
    MySimpleOrder myOrder = new MySimpleOrder();
    myOrder.setBillTo(new MyAddress());
    myOrder.getBillTo().setName("John Doe");
    myOrder.getBillTo().setStreet("125 Main Street");
    myOrder.getBillTo().setCity("Any Town");
    myOrder.getBillTo().setState("NM");
    myOrder.getBillTo().setZip("95811");
    myOrder.getBillTo().setPhone("(831) 874-1123");
    Items items = new Items();
    myOrder.setItems(items);
    List<MyItem> itemList = items.getItem();
    MyItem myItem = new MyItem();
    myItem.setPrice((float) 2.99);
    myItem.setQuantity(6);
    myItem.setProductName("Diet Coke");
    itemList.add(myItem);
    myItem = new MyItem();
    myItem.setPrice((float) 3.99);
    myItem.setQuantity(4);
    myItem.setProductName("Potato Chips");
    itemList.add(myItem);
    myItem = new MyItem();
    myItem.setPrice((float) 5.34);
    myItem.setQuantity(2);
    myItem.setProductName("Frozen Pizza");
    itemList.add(myItem);
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
