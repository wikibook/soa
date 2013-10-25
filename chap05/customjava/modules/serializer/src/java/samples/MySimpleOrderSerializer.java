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
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.example.oms.ItemType;
import com.example.oms.SimpleOrder;

public class MySimpleOrderSerializer implements Serializer {

  JAXBContext jaxbContext;
  Marshaller jaxbMarshaller; 
  private Unmarshaller jaxbUnmarshaller;
  
  //! <example xn="MySimpleOrderConstructor">
  //! <c>chap05</c><s>customjava</s>
  public MySimpleOrderSerializer() throws JAXBException {

    jaxbContext = JAXBContext.newInstance("com.example.oms");
    jaxbMarshaller = jaxbContext.createMarshaller();
    jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    
  }
  //! </example>
  
  public static void main(String[] args) throws Exception {

    //! <example xn="ExercisingTheSerializer">
    //! <c>chap05</c><s>customjava</s>
    Serializer serializer = new MySimpleOrderSerializer();
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
  
  //! <example xn="SerializerImplementation">
  //! <c>chap05</c><s>customjava</s>
  public Source getXML(Object o) {

    try {
      SimpleOrder jaxbSimpleOrder = 
        transformMySimpleOrderToJAXB((MySimpleOrder) o);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      jaxbMarshaller.marshal(jaxbSimpleOrder,baos);
      return new StreamSource(new StringReader(baos.toString()));
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    } 
    
  }
  //! </example>

  //! <example xn="MySimpleOrderCustomJavaCode">
  //! <c>chap05</c><s>customjava</s>
  private SimpleOrder transformMySimpleOrderToJAXB(MySimpleOrder order) 
  throws JAXBException {
 
    SimpleOrder jaxbSimpleOrder = new SimpleOrder();
    // map the addresses
    MyAddress myAddress = order.getBillTo();
    Serializer myAddressSer = new MyAddressSerializer();
    JAXBElement<SimpleOrder.BillTo> jaxbBillToElt = 
      (JAXBElement<SimpleOrder.BillTo>) jaxbUnmarshaller.unmarshal(
       myAddressSer.getXML(myAddress),  SimpleOrder.BillTo.class);
    jaxbSimpleOrder.setBillTo(jaxbBillToElt.getValue());
    // map the items
    Serializer myItemSer = new MyItemSerializer();
    jaxbSimpleOrder.setItems(new SimpleOrder.Items()); // needed to avoid NPE
    List<ItemType> jaxbItemList = jaxbSimpleOrder.getItems().getItem();
    for (MyItem myItem : order.getItemList()) {
      JAXBElement<ItemType> jaxbItemTypeElt = 
        (JAXBElement<ItemType>) jaxbUnmarshaller.unmarshal(
         myItemSer.getXML(myItem),  ItemType.class);
      jaxbItemList.add(jaxbItemTypeElt.getValue());      
    }
    return jaxbSimpleOrder;
    
  }
  //! </example>

}
