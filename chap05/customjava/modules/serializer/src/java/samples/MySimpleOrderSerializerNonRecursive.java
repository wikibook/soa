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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import com.example.oms.ItemType;
import com.example.oms.SimpleOrder;

public class MySimpleOrderSerializerNonRecursive {

  public static void main(String[] args) throws Exception {

    //! <example xn="ExercisingTheSerializer_NonRecursive">
    //! <c>chap05</c><s>customjava</s>
    MySimpleOrderSerializerNonRecursive serializer = 
      new MySimpleOrderSerializerNonRecursive();
    MySimpleOrder myOrder = new MySimpleOrder(
        "John Doe",
        "125 Main Street",
        "Any Town", "NM", "95811",
        "(831) 874-1123");
    myOrder.getItemList().add(new MyItem(6, (float) 2.99, "Diet Coke"));
    myOrder.getItemList().add(new MyItem(4, (float) 3.99, "Potato Chips"));
    myOrder.getItemList().add(new MyItem(2, (float) 5.34, "Frozen Pizza"));
    Transformer xform = TransformerFactory.newInstance().newTransformer();
    xform.setOutputProperty(OutputKeys.INDENT, "yes");
    xform.transform(
        serializer.getXML(myOrder),
        new StreamResult(System.out));
    //! </example>

  }
  
  //! <example xn="SerializerImplementationNonRecursive">
  //! <c>chap05</c><s>customjava</s>
  public Source getXML(MySimpleOrder order) {

    // create the JAXB SimpleOrder
    SimpleOrder jaxbSimpleOrder = new SimpleOrder();
    // map the addresses
    MyAddress myAddress = order.getBillTo();
    SimpleOrder.BillTo billTo = new SimpleOrder.BillTo();
    billTo.setName(myAddress.name);
    billTo.setCity(myAddress.city);
    billTo.setPhone(myAddress.phone);
    billTo.setState(myAddress.state);
    billTo.setStreet(myAddress.street);
    billTo.setZip(myAddress.zip);
    jaxbSimpleOrder.setBillTo(billTo);
    // map the items
    jaxbSimpleOrder.setItems(new SimpleOrder.Items()); // needed to avoid NPE
    List<ItemType> jaxbItemList = jaxbSimpleOrder.getItems().getItem();
    for (MyItem myItem : order.getItemList()) {
      ItemType jaxbItem = new ItemType();
//    jaxbItem.setPrice((double) myItem.getPrice());
      jaxbItem.setPrice(Double.parseDouble(Float.toString(myItem.getPrice())));
      jaxbItem.setProductName(myItem.getProductName());
      jaxbItem.setQuantity(BigInteger.valueOf((long) myItem.getQuantity()));
      jaxbItemList.add(jaxbItem);
    }
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance("com.example.oms");
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      SchemaFactory sf = 
        SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      Schema schema = sf.newSchema(
          new URL("http://soabook.com/example/oms/simpleorder.xsd"));
      jaxbMarshaller.setSchema(schema);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      jaxbMarshaller.marshal(jaxbSimpleOrder, baos);
      return new StreamSource(new StringReader(baos.toString()));
    } catch (Exception e) {
      throw new RuntimeException(e);
    } 
    
  }
  //! </example>

}
