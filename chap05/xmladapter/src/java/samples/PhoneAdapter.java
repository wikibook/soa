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
//! <example xn="PhoneAdapter">
//! <c>chap05</c><s>xmladapter</s>
public class PhoneAdapter extends XmlAdapter<String, Phone> {

  public Phone unmarshal(String jaxbPhone) throws Exception {

    Phone phone = new Phone();
    int areaStart = jaxbPhone.indexOf("(");
    int areaEnd = jaxbPhone.indexOf(")");
    String area = jaxbPhone.substring(areaStart+1,areaEnd);
    jaxbPhone = jaxbPhone.substring(areaEnd+1,jaxbPhone.length());
    String phoneSplit[] = jaxbPhone.split("-", 2);
    phone.setAreaCode(Integer.valueOf(area).intValue());
    phone.setExchange(phoneSplit[0].trim());
    phone.setNumber(phoneSplit[1].trim());
    return phone;
    
  }

  public String marshal(Phone myPhone) throws Exception {

    return "(" + myPhone.getAreaCode() + ") " + myPhone.getExchange() + "-" +
    myPhone.getNumber();
    
  }

}
//! </example>
