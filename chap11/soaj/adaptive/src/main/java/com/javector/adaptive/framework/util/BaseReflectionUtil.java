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
package com.javector.adaptive.framework.util;

import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: rohit.agarwal
 * Date: Apr 4, 2006
 * Time: 10:20:00 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseReflectionUtil {

    public static final String xsNS = "http://www.w3.org/2001/XMLSchema";
    protected static HashMap<String,QName> classVSqNameMap = new HashMap<String,QName>();
    protected static HashMap<String,String> qNameVSClass = new HashMap<String,String>();

    static {
        qNameVSClass.put("string",String.class.getName());
        qNameVSClass.put("positiveInteger",BigInteger.class.getName());
        qNameVSClass.put("int",int.class.getName());
        qNameVSClass.put("double", double.class.getName());
        qNameVSClass.put("char",char.class.getName());
        qNameVSClass.put("float",float.class.getName());
        qNameVSClass.put("base64Binary",byte[].class.getName());
        qNameVSClass.put("byte",byte.class.getName());
        qNameVSClass.put("integer",Integer.class.getName());
        qNameVSClass.put("short",short.class.getName());
        qNameVSClass.put("decimal",BigDecimal.class.getName());
        qNameVSClass.put("dateTime",Calendar.class.getName());
        qNameVSClass.put("boolean",boolean.class.getName());
        qNameVSClass.put("ENTITIES",List.class.getName());
        qNameVSClass.put("anyURI",URI.class.getName());


    }


    static {
        classVSqNameMap.put(String.class.getName(), new QName(xsNS, "string"));
        classVSqNameMap.put(int.class.getName(), new QName(xsNS, "int"));
        classVSqNameMap.put(double.class.getName(), new QName(xsNS, "double"));
        classVSqNameMap.put(char.class.getName(), new QName(xsNS, "char"));
        classVSqNameMap.put(float.class.getName(), new QName(xsNS, "float"));
        classVSqNameMap.put(Float.class.getName(), new QName(xsNS, "float"));
        classVSqNameMap.put(byte[].class.getName(), new QName(xsNS, "base64Binary"));
        classVSqNameMap.put(byte.class.getName(), new QName(xsNS, "byte"));
        classVSqNameMap.put(Byte.class.getName(), new QName(xsNS, "byte"));
        classVSqNameMap.put(short.class.getName(), new QName(xsNS, "short"));
        classVSqNameMap.put(Integer.class.getName(), new QName(xsNS, "integer"));
        classVSqNameMap.put(BigInteger.class.getName(), new QName(xsNS, "integer"));
        classVSqNameMap.put(BigDecimal.class.getName(), new QName(xsNS, "decimal"));
        classVSqNameMap.put(Long.class.getName(), new QName(xsNS, "long"));
        classVSqNameMap.put(short.class.getName(), new QName(xsNS, "short"));
        classVSqNameMap.put(Short.class.getName(), new QName(xsNS, "short"));
        classVSqNameMap.put(long.class.getName(), new QName(xsNS, "long"));
        classVSqNameMap.put(boolean.class.getName(), new QName(xsNS, "boolean"));
        classVSqNameMap.put(Boolean.class.getName(), new QName(xsNS, "boolean"));
        classVSqNameMap.put(Calendar.class.getName(), new QName(xsNS, "dateTime"));
        classVSqNameMap.put(java.util.Date.class.getName(), new QName(xsNS, "dateTime"));
        classVSqNameMap.put(java.sql.Date.class.getName(), new QName(xsNS, "dateTime"));
        classVSqNameMap.put(List.class.getName(), new QName(xsNS, "ENTITIES"));
        classVSqNameMap.put(QName.class.getName(), new QName(xsNS, "QName"));
        classVSqNameMap.put(URI.class.getName(), new QName(xsNS, "anyURI"));
        classVSqNameMap.put(BigInteger.class.getName(), new QName(xsNS, "positiveInteger"));
    }


    public static String getJavaClassNameForXmlType(String type) {
        return qNameVSClass.get(type);
    }





    public  abstract Object createXMLObjectFromXMLNode(Node xmlDoc, QName xmlType, Object cl);


    public abstract String getNameSpaceUri(Object sourceJAXBObject);


    public static boolean isSimpleJavaType(String name) {
        return classVSqNameMap.containsKey(name);
    }





    public abstract  Object createXMLObject(QName xmlType, Object cl);

    public abstract Object createXMLObjectFromXMLSource(String xmlSource, QName xmlType, Object cl);

    public abstract boolean isSimpleType(Object parentJaxbObject, QName xmlName);

    public abstract boolean isSimpleType(Object parentJaxbObject);

    public  static boolean isSimpleType(QName qname) {
        return classVSqNameMap.containsValue(qname);
    }

    public boolean belongsToXMLSchemaNameSpace(QName qName)  {
        return classVSqNameMap.containsValue(qName);
    }

    public abstract  boolean isArrayType(Object parentJaxbObject, QName xmlName);

    public abstract  void setProperty(Object sourceJaxbObject, Object propertyValue, QName xmlName);

    public abstract  Object getPropertyValue(Object sourceJaxbObject, QName xmlName);


    public abstract  QName getPropertySchemaType(Object sourceJaxbObject, QName xmlName);

    public abstract List<Object> getList(Object objectz, QName xmlName);


    public abstract String convertXMLObjectToString(Object targetJaxbObject, ClassLoader cl, QName xmlName);




    public abstract Object createXMLObjectFromXmlNode(ClassLoader cl, Node xmlNode, Object sourceObject, Class jaxbMappingClass);


    public abstract Class getClass(Object sourceJaxbObject, QName xmlName);

    public abstract String convertXMLObjectToString(Object targetJaxbObject, ClassLoader cl);


      public  static String getXmlString(Node n) {
        Transformer serializer =
                null;
        try {
            serializer = TransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter sw = new StringWriter();
            StreamResult out = new StreamResult(sw);
            serializer.transform(new DOMSource(n), out);
            String result = ((StringWriter) out.getWriter()).getBuffer().toString();
            return result;
        } catch (TransformerException e) {
            return null;
        }
     }
}
