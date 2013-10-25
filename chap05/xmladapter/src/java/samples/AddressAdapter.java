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

import javax.xml.bind.annotation.adapters.XmlAdapter;

//! <example xn="AddressAdapter">
//! <c>chap05</c><s>xmladapter</s>
public class AddressAdapter extends XmlAdapter<AddressXML, Address> {
  
  public AddressXML marshal(Address addr) throws Exception {
    
    System.out.println("entered AddressAdapter.marshal.");
    AddressXML jaxbAddress = new AddressXML();
    String[] streetParts = addr.getStreetName().split(" - ",2);
    jaxbAddress.addrLine1 = addr.getStreetNum() + " " + streetParts[0];
    if ( streetParts.length > 1 ) {
      jaxbAddress.addrLine2 = streetParts[1];
    } else {
      jaxbAddress.addrLine2 = "";
    }
    // the rest is simple mapping
    jaxbAddress.city = addr.getCity();
    jaxbAddress.phone = addr.getPhoneNumber();
    jaxbAddress.state = addr.getState();
    jaxbAddress.zip = addr.getZip();
    return jaxbAddress;
    
  }
  
  public Address unmarshal(AddressXML jaxbAddress)
  throws Exception {
    
    Address addr = new Address();
    String[] line1Parts = jaxbAddress.addrLine1.split(" ",2);
    int num = -1;
    String street;
    try {
      num = Integer.valueOf(line1Parts[0]).intValue();
    } catch (Exception e ) {}
    if ( num > 0 ) {
      addr.setStreetNum(num);
      street = line1Parts[1];
    } else {
      street = jaxbAddress.addrLine1;
    }
    String line2 = jaxbAddress.addrLine2;
    if (line2 != null && !line2.equals("") ) {
      street += " - " + line2;
    }
    // the rest is simple mapping
    addr.setStreetName(street);
    addr.setCity(jaxbAddress.city);
    addr.setState(jaxbAddress.state);
    addr.setZip(jaxbAddress.zip);
    addr.setPhoneNumber(jaxbAddress.phone);
    return addr;
  }
  
}
//! </example>
