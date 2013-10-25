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

import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

//! <example xn="MyItemCustomJavaCode_noJAXB">
//! <c>chap05</c><s>customjava</s>
public class MyItemSerializer implements Serializer {

  public Source getXML(Object o) {

    MyItem myItem = (MyItem) o;
    String xml = 
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
      "<item xmlns=\"http://www.example.com/oms\" productName=\"" +
      myItem.getProductName() + "\">" +
      "<quantity>" + myItem.getQuantity() + "</quantity>" +
      "<price>" + myItem.getPrice() + "</price>" +
      "</item>";
    return new StreamSource(new StringReader(xml));

  }
  
}
//! </example>
