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
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller;
import org.xml.sax.InputSource;


public class Client {
  
//! <example xn="MAIN">
//! <c>chap06</c><s>castor</s>
  public static void main(String[] args) throws Exception {
    
    String host = args[1];
    String port = args[2];
    // load Castor Mapping File
    FileInputStream castorMappingFile = new FileInputStream(args[0]);
    Mapping castorMapping = new Mapping();
    castorMapping.loadMapping(new InputSource(castorMappingFile));
    // Use Castor to marshal MyRequestOrder to XML
    MyRequestOrder requestOrder = createRequestOrder();
    ByteArrayOutputStream ba = new ByteArrayOutputStream();
    Marshaller m = new Marshaller(new OutputStreamWriter(ba));
    m.setMapping(castorMapping);
    m.marshal(requestOrder);
    Source xmlSource = new StreamSource(new StringReader(ba.toString()));
    // create Dispatch<Source>
    URL wsdlURL = new URL("http://"+host+":"+port+
      "/chap06-endpoint-endpoint-1.0/requestOrder?wsdl");
    QName serviceQName =
      new QName("http://www.example.com/req", "RequestOrderService");
    Service service = Service.create(wsdlURL, serviceQName);
    QName portQName = 
      new QName("http://www.example.com/req", "RequestOrderPort");
    Dispatch<Source> dispatch = service.createDispatch(portQName, 
        Source.class, Service.Mode.PAYLOAD);
    // invoke web service with Castor generated XML
    Source orderSource = dispatch.invoke(xmlSource);
//! </example>    
    // print response
    Transformer x = TransformerFactory.newInstance().newTransformer();
    x.setOutputProperty(OutputKeys.INDENT, "yes");
    System.out.println("Request Message ===============================");
    System.out.println();
    x.transform(new StreamSource(new StringReader(ba.toString())),
        new StreamResult(System.out));
    System.out.println();
    System.out.println("Response Message ===============================");
    x.transform(orderSource, new StreamResult(System.out));
    
  }
  
  private static MyRequestOrder createRequestOrder() {
    
    MyRequestOrder req = new MyRequestOrder();
    req.setCustno("ENT0072123");
    req.setCcard(createCreditCard());
    for (MyItem item : createItemList()) {
      req.getItemList().add(item);
    }
    return req;
    
  }
  
  private static ArrayList<MyItem> createItemList() {
    
    ArrayList<MyItem> itemList = new ArrayList<MyItem>();
    MyItem item = new MyItem();
    item.setITMNUMBER("012345");
    item.setSTORAGELOC("NE02");
    item.setTARGETQTY(50);
    item.setTARGETUOM("CNT");
    item.setPRICEPERUOM(BigDecimal.valueOf(7.95));
    item.setSHORTTEXT("7 mm Teflon Gasket");
    itemList.add(item);
    item = new MyItem();
    item.setITMNUMBER("543210");
    item.setTARGETQTY(5);
    item.setTARGETUOM("KG");
    item.setPRICEPERUOM(BigDecimal.valueOf(12.58));
    item.setSHORTTEXT("Lithium grease with PTFE/Teflon");
    itemList.add(item);
    return itemList;
    
  }
  
  private static CreditCard createCreditCard() {
    
    CreditCard ccard = new CreditCard();
    ccard.type = "VISA";
    ccard.num = "01234567890123456789";
    ccard.expireDate = "2009-10-31";
    ccard.name = "John Doe";
    return ccard;
    
  }
  
}
