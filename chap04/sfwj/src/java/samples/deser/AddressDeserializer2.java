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
package samples.deser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import samples.Address;

public class AddressDeserializer2 {
  
  public static void main(String args[]) {
    
    System.out.println(" ");
    System.out.println("running AddressDeserializer2");
    System.out.println("(XSLT MAPPING IMPLEMENTATION)");
    try {
      File corp_addrXml = new File(args[0]);
      File xsltFile = new File(args[1]);
      AddressDeserializer2 dser = new AddressDeserializer2();
      Source addrSource = dser.convertCorpAddrToAddr(
          new StreamSource(corp_addrXml), new StreamSource(xsltFile));
      Address addr = dser.deserializeAddress(addrSource);
      System.out.println("Address.streetNum = " + addr.getStreetNum());
      System.out.println("Address.streetName = " + addr.getStreetName());
      System.out.println("Address.city = " + addr.getCity());
      System.out.println("Address.state = " + addr.getState());
      System.out.println("Address.zip = " + addr.getZip());
      System.out.println("Address.phone.areaCode = " + 
          addr.getPhoneNumber().getAreaCode());
      System.out.println("Address.phone.exchange = " + 
          addr.getPhoneNumber().getExchange());
      System.out.println("Address.phone.number = " + 
          addr.getPhoneNumber().getNumber());
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
//! <example xn="DESERIALIZE_WITH_XSLT">
//! <c>chap04</c><s>sfwj</s> 
  public Address deserializeAddress(Source xml) throws Exception {

    JAXBContext jc = 
      JAXBContext.newInstance(Address.class);
    Unmarshaller u = jc.createUnmarshaller();
    JAXBElement<Address> addrJAXBElt = u.unmarshal(xml, Address.class);
    Address addr = addrJAXBElt.getValue();
    return addr;
    
  }
  
  private Source convertCorpAddrToAddr(Source xml, Source xslt) 
  throws Exception {
    
    Transformer transformer = 
      TransformerFactory.newInstance().newTransformer(xslt);
    ByteArrayOutputStream ba = new ByteArrayOutputStream();
    transformer.transform(xml, new StreamResult(ba));
    return new StreamSource(new StringReader(ba.toString()));
    
  }
  //! </example>
    
}
