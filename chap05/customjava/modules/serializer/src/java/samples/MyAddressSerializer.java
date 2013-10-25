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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import com.example.oms.SimpleOrder;

public class MyAddressSerializer implements Serializer {
  
  private JAXBContext jc;
  private Marshaller m;

//! <example xn="MyAddressCustomJavaCode_yesJAXB">
//! <c>chap05</c><s>customjava</s>
  public MyAddressSerializer() throws JAXBException {
    
    jc = JAXBContext.newInstance("com.example.oms");
    m = jc.createMarshaller();
    
  }

  public Source getXML(Object o) {
    
    SimpleOrder.BillTo jaxbBillTo;
    try {
      jaxbBillTo = transformMyAddressToBillTo((MyAddress) o);
      JAXBElement<SimpleOrder.BillTo> jaxbBillToElt = 
        new JAXBElement<SimpleOrder.BillTo>(
            new QName("http://www.example.com/oms", "billTo"),
            SimpleOrder.BillTo.class,
            SimpleOrder.class,
            jaxbBillTo);
      ByteArrayOutputStream ba = new ByteArrayOutputStream();
      m.marshal(jaxbBillToElt, ba);
      return new StreamSource(new StringReader(ba.toString()));
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }

  }
  
  private SimpleOrder.BillTo transformMyAddressToBillTo(MyAddress myAddr)
  throws JAXBException {
    
    SimpleOrder.BillTo jaxbBillTo = new SimpleOrder.BillTo();
    jaxbBillTo.setName(myAddr.name);
    jaxbBillTo.setCity(myAddr.city);
    jaxbBillTo.setPhone(myAddr.phone);
    jaxbBillTo.setState(myAddr.state);
    jaxbBillTo.setStreet(myAddr.street);
    jaxbBillTo.setZip(myAddr.zip);
    return jaxbBillTo;
    
  }
//! </example>  

}
