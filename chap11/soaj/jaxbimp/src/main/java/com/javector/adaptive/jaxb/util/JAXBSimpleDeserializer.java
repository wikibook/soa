/*
* Copyright 2006-2007 Javector Software LLC
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
package com.javector.adaptive.jaxb.util;

import javax.xml.namespace.QName;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: javector
 * Date: Mar 19, 2006
 * Time: 3:25:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class JAXBSimpleDeserializer {

    public static final String xsNS = "http://www.w3.org/2001/XMLSchema";
      private static HashMap qNameMap = new HashMap();

      static {
          qNameMap.put(String.class.getName(), new QName(xsNS, "string"));
          qNameMap.put(int.class.getName(), new QName(xsNS, "int"));
          qNameMap.put(double.class.getName(), new QName(xsNS, "double"));
          qNameMap.put(char.class.getName(), new QName(xsNS, "char"));
          qNameMap.put(float.class.getName(), new QName(xsNS, "float"));
          qNameMap.put(byte[].class.getName(), new QName(xsNS, "base64Binary"));
          qNameMap.put(short.class.getName(), new QName(xsNS, "short"));
          qNameMap.put(boolean.class.getName(), new QName(xsNS, "boolean"));
      }

    public static boolean belongsToXMLSchemaNameSpace(QName name) {
           return  qNameMap.containsValue(name);
       }

       public static void main(String[] args) {
           System.out.println(" = " +  JAXBSimpleDeserializer.belongsToXMLSchemaNameSpace(new QName(xsNS,"string")));
       }




}
