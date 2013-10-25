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
package com.javector.adaptive.util;

import org.w3c.dom.Document;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: javector
 * Date: Mar 30, 2006
 * Time: 9:14:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class SOAFormatterUtil {

    //todo : parseXmlData() not in use 
    public  static String parseXmlData(String data) {
           data = data.replaceAll(">","");
           data = data.replaceAll(">","");
           data = data.replaceAll("&lt;","<");
           data = data.replaceAll("&gt;",">");
           return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+data;
       }

    public static String unParseXMLData(String data) {
        data = data.replaceAll("<","&lt;");
        data = data.replaceAll(">","&gt;");
        return data;
    }

   

}
