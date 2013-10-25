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

import javax.xml.bind.Binder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import samples.MySimpleOrder;

public class MySimpleOrderSerializer {

  public static void main(String[] args) throws Exception {
    
    MySimpleOrder myOrder = new MySimpleOrder(
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
      DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = db.newDocument();
      jaxbMarshaller.marshal(myOrder, new DOMResult(doc));
      Transformer xform = TransformerFactory.newInstance().newTransformer();
      xform.transform(new DOMSource(doc), new StreamResult(System.out));
      System.out.println();
      System.out.println();
      Binder<Node> myBinder = jaxbContext.createBinder();
      MySimpleOrder binderOrder = (MySimpleOrder) myBinder.unmarshal(doc);
      binderOrder.setPersisted(true);
      myBinder.updateXML(binderOrder);
      xform.transform(new DOMSource(doc), new StreamResult(System.out));
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }

}
