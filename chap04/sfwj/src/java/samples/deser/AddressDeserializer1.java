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

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import samples.Address;
import samples.Phone;
import samples.State;

public class AddressDeserializer1 {
  
  public static void main(String args[]) {
    
    System.out.println(" ");
    System.out.println("running AddressDeserializer1");
    System.out.println("(JAVA MAPPING IMPLEMENTATION)");
    try {
      File addrXml = new File(args[0]);
      AddressDeserializer1 dser = new AddressDeserializer1();
      Address addr = dser.deserializeAddress(new StreamSource(addrXml));
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
 
  
//! <example xn="DESERIALIZE_WITH_JAVA">
//! <c>chap04</c><s>sfwj</s> 
  public Address deserializeAddress(Source xml) throws Exception {

    JAXBContext jc = 
      JAXBContext.newInstance("com.example.corp");
    Unmarshaller u = jc.createUnmarshaller();
    JAXBElement<com.example.corp.AddressType> addrJAXBElt =
      u.unmarshal(xml, com.example.corp.AddressType.class);
    com.example.corp.AddressType addrJAXB = addrJAXBElt.getValue();
    return convertAddressTypeToAddress(addrJAXB);
    
  }
  
  private Address convertAddressTypeToAddress
  (com.example.corp.AddressType addrJAXB) throws Exception {
    
    Address addr = new Address();
    String[] line1Parts = addrJAXB.getAddrLine1().split(" ",2);
    int num = -1;
    String street;
    try {
      num = Integer.valueOf(line1Parts[0]).intValue();
    } catch (Exception e ) {}
    if ( num > 0 ) {
      addr.setStreetNum(num);
      street = line1Parts[1];
    } else {
      street = addrJAXB.getAddrLine1();
    }
    String line2 = addrJAXB.getAddrLine2();
    if (line2 != null && !line2.equals("") ) {
      street += " - " + line2;
    }
    addr.setStreetName(street);
    addr.setCity(addrJAXB.getCity());
    addr.setState(State.valueOf(addrJAXB.getState()));
    addr.setZip(Integer.valueOf(addrJAXB.getZip()).intValue());
    String ph = addrJAXB.getPhone();
    int areaStart = ph.indexOf("(");
    int areaEnd = ph.indexOf(")");
    String area = ph.substring(areaStart+1,areaEnd);
    ph = ph.substring(areaEnd+1,ph.length());
    String phoneSplit[] = ph.split("-", 2);
    Phone phone = new Phone();
    phone.setAreaCode(Integer.valueOf(area).intValue());
    phone.setExchange(phoneSplit[0].trim());
    phone.setNumber(phoneSplit[1].trim());
    addr.setPhoneNumber(phone);    
    return addr;
    
  }
  //! </example >
  
}
