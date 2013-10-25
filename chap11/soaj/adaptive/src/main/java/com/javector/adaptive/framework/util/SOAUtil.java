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

import com.javector.adaptive.framework.ConfiguratorFactory;
import com.javector.soaj.SoajRuntimeException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

public class SOAUtil {

  //TODO - mdh to refactor these methods into core util libraries

    public static String convertToTitleCase(String str){
        String s = str.trim();
        return Character.toUpperCase(s.charAt(0)) + s.substring(1,str.length());

    }

    public static Object getDefaultInstance(String className) {
        try {
            Class<?> aClass = Class.forName(className);
            return  aClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new SoajRuntimeException("not able to find class definition check class Path " + e);
        } catch (InstantiationException e) {
            throw new SoajRuntimeException("not able to create instance of the class " + className +"check the existance of default constructor" + e);
        } catch (IllegalAccessException e) {
            throw new SoajRuntimeException("not able to create instance of the class = " + className,e);
        }
    }


    public static boolean doesConvertorExits(String convertorClassName) {
        return !(convertorClassName ==null || convertorClassName.trim().equals(""));
    }

    public static boolean validateHandler(String name, Class<?>  baseClass) {
        try {
            Class<?> javaClass = Class.forName(name);
            if(!baseClass.isAssignableFrom(javaClass)) {
                return false;
            }
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    public static InputStream loadResource(String relativeFilePath) {
          InputStream systemResourceAsStream;
        systemResourceAsStream= ConfiguratorFactory.class.getClassLoader().getResourceAsStream(relativeFilePath);

        if(systemResourceAsStream==null){
            systemResourceAsStream = ClassLoader.getSystemResourceAsStream(relativeFilePath);
        }
        if(systemResourceAsStream==null){
            systemResourceAsStream = ClassLoader.getSystemResourceAsStream(relativeFilePath);
        }
        if(systemResourceAsStream == null) {
            throw new SoajRuntimeException("The file path is not correct or the file does lie in classLoader's classpath for path =" +relativeFilePath);
        }
        return systemResourceAsStream;
    }

    public  static Document createDummyDocumentForString(String data) {
        return convertStringToDocument("<doc>"+data+"</doc>");
    }

   public  static Document convertStringToDocument(String xmlString) {
        try {
            DocumentBuilderFactory docfactory = DocumentBuilderFactory.newInstance();
            docfactory.setNamespaceAware(true);
            DocumentBuilder builder = docfactory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
            throw new SoajRuntimeException("exception while converting xmlstring to document ", e);
        }
    }


    public static Node getXMLNodeFromDOMSource(DOMSource source) {
            return source.getNode();
    }

    public static Node getXMLFromStreamSource(StreamSource source) {
        if(source == null) {
            throw new SoajRuntimeException("check the source as it is null");
        }
        InputStream inputStream = source.getInputStream();
        String dataFromStream = getDataFromStream(inputStream);
        return convertStringToDocument(dataFromStream).getFirstChild();
    }

    private static String getDataFromStream(InputStream stream ) {
        int i;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            while((i = stream.read())!=-1) {
                outputStream.write(i);
            }
        } catch (IOException e) {
            throw new SoajRuntimeException("not able to get data from stream ",e);
        }
        return outputStream.toString();
    }


    public static  Node getXMLNodeFromSource(Source source) {
        if(source == null ) {
            throw new SoajRuntimeException("Source object is null ");
        }
        if(source instanceof StreamSource) {
            return getXMLFromStreamSource((StreamSource)source);
        }else if(source instanceof DOMSource) {
            return getXMLNodeFromDOMSource((DOMSource)source);
        }else if(source instanceof SAXSource) {
            return getXMLNodeFromSAXSource((SAXSource)source);
        }

        throw new SoajRuntimeException("unable to get node from source of type = " + source.getClass());
    }

    public  static Node getXMLNodeFromSAXSource(SAXSource source) {
        InputStream byteStream = source.getInputSource().getByteStream();
        String dataFromStream = getDataFromStream(byteStream);
        return convertStringToDocument(dataFromStream).getFirstChild();
    }

   
}
