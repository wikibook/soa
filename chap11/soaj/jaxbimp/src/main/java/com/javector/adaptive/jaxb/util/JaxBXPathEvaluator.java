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

import com.javector.adaptive.framework.util.BaseReflectionUtil;
import com.javector.adaptive.framework.util.BaseXPathEvaluator;
import com.javector.adaptive.framework.util.SOAUtil;
import com.javector.adaptive.framework.dto.DeserializeBindingRuleDTO;
import com.javector.adaptive.framework.model.ScriptDTO;
import com.javector.adaptive.framework.model.RuleDTO;
import com.javector.soaj.SoajRuntimeException;


import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;
import org.apache.xpath.objects.XNodeSet;
import org.w3c.dom.*;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: kishore.g
 * Date: Nov 27, 2005
 * Time: 6:53:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class JaxBXPathEvaluator extends BaseXPathEvaluator {
    private static Logger log =
            Logger.getLogger(JaxBXPathEvaluator.class.getName());


   /**
    * Applies an XPath expression to an XML object and returns the result.
    * 
   * @param cl The class loaders used to instantiate JAXB instances
   * @param reflectionUtil The JAXB reflection utility used to create JAXB instances based on XML types (i.e., QNames)
   * @param xmlType The XML schema type or global element of the nodes specified by the XPath expression.  
   * @param sourceJaxbObject The XML that the XPath expression gets applied to
   * @param sourceXmlName
   * @param xPath The XPath expression that gets applied.
   * @return An array of XML nodes  - or a single String instance array - defined by the XPath expression
   */
  private Object[] applyXpathForComplexTypes(ClassLoader cl, BaseReflectionUtil reflectionUtil, QName xmlType, Object sourceJaxbObject, QName sourceXmlName, String xPath) {
        Document document = null;
        Node nsNode = null;
        XObject dataObject;
        Collection jaxbObjCollection = new ArrayList();
        if(reflectionUtil.belongsToXMLSchemaNameSpace(xmlType)) {
            document = SOAUtil.createDummyDocumentForString(sourceJaxbObject.toString());
        } else {
            String s = reflectionUtil.convertXMLObjectToString(sourceJaxbObject, cl, sourceXmlName);
            document = SOAUtil.convertStringToDocument(s);
        }
        xPath = xPath.replaceAll("namespace|declare", "").replaceAll("[^ ]*=[^ ]*", "").replaceAll("\n", "").trim();
        Map nsNodeMapFromSource = createNSNodeMapFromSource(document);
        Map nsNodeMapFromXpath = createNSNodeMapFromXpath(xPath);
        // String newXPath = adjustXPath(xPath, nsNodeMapFromSource, nsNodeMapFromXpath);
        nsNode = createNSNode(nsNodeMapFromSource, nsNodeMapFromXpath);
        try {
            dataObject = XPathAPI.eval(document,xPath,nsNode);
            if(dataObject instanceof XString) {
                return new Object[]{dataObject.toString()};
            }

            if(dataObject instanceof XNodeSet) {
                XNodeSet set = (XNodeSet)dataObject;
                NodeList  list = set.nodelist();
                Node node;
                for(int i=0; i<list.getLength();i++) {
                    node  = list.item(i);
                    Object jaxbObj = reflectionUtil.createXMLObject(xmlType,cl);
                    jaxbObjCollection.add(reflectionUtil.createXMLObjectFromXmlNode(cl,node,sourceJaxbObject,jaxbObj.getClass()));
                }
            }

        } catch (TransformerException e) {
            throw new SoajRuntimeException("unable to evaluate xpath = "+ xPath,e);
        }
        return jaxbObjCollection.toArray();
        //then serialize to document
        //adjust xpath
        //apply xpath
        //from the result create the jaxbobjects
    }

    public Object applyXPath(DeserializeBindingRuleDTO bindingRuleDTO, Object sourceObject) {
        if(sourceObject instanceof String) {
        return applyXpathForString(bindingRuleDTO, sourceObject);
        }else {
            RuleDTO ruleDTO = bindingRuleDTO.getRuleDTO();
            String scriptData = ruleDTO.getScriptDTO().getScriptData();
            return applyXpathForComplexTypes(bindingRuleDTO.getCl(),bindingRuleDTO.getReflectionUtil(),ruleDTO.getXmlType(),sourceObject,ruleDTO.getXmlName(),scriptData);
        }
    }

    private Object applyXpathForString(DeserializeBindingRuleDTO bindingRuleDTO, Object sourceObject) {
        ScriptDTO scriptDTO = bindingRuleDTO.getRuleDTO().getScriptDTO();
        String xPath = scriptDTO.getScriptData();
        xPath = xPath.replaceAll("namespace|declare", "").replaceAll("[^ ]*=[^ ]*", "").replaceAll("\n", "").trim();
        Document  document = SOAUtil.createDummyDocumentForString(sourceObject.toString());
        try {
            Object xObject =  XPathAPI.eval(document,xPath);
            if(xObject instanceof XString) {
                return xObject.toString();
            } else {
                throw new SoajRuntimeException("expecting a string value after applying xpath on property  = " + sourceObject);
            }

        } catch (TransformerException e) {
            throw new SoajRuntimeException("unable to transform given xpath ",e);
        }
    }



    private static String adjustXPath(String xPath, Map sourceXmlNsMap, Map xpathNSMap) {
        Set set = xpathNSMap.keySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            String uri = (String) iterator.next();
            String prefixInXpath = (String) xpathNSMap.get(uri);
            String prefixInNsMap = (String) sourceXmlNsMap.get(uri);
            if (sourceXmlNsMap.containsKey(uri) && !prefixInXpath.equalsIgnoreCase(prefixInNsMap)) {
                xPath = xPath.replaceAll(prefixInXpath + "\\:", prefixInNsMap + ":");
            }
        }
        return xPath;
    }

    private static Node createNSNode(Map sourceXmlNsMap, Map xpathNSMap) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        Element nsNode;
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            nsNode = documentBuilder.newDocument().createElement("nsNode");

        } catch (Exception e) {
            throw new SoajRuntimeException("Exception while creating nsNode", e);
        }

        Set set = sourceXmlNsMap.keySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            String uri = (String) iterator.next();
            String prefixInSource = (String) sourceXmlNsMap.get(uri);
            if (prefixInSource.length() > 0)
                nsNode.setAttribute("xmlns:" + prefixInSource, uri);
            else
                nsNode.setAttribute("xmlns" + prefixInSource, uri);
        }
        set = xpathNSMap.keySet();
        iterator = set.iterator();
        while (iterator.hasNext()) {
            String uri = (String) iterator.next();
            String prefixInXpath = (String) xpathNSMap.get(uri);
            if (!sourceXmlNsMap.containsKey(uri)) {
                if (prefixInXpath.length() > 0)
                    nsNode.setAttribute("xmlns:" + prefixInXpath, uri);
                else
                    nsNode.setAttribute("xmlns" + prefixInXpath, uri);
            }
        }
        return nsNode;
    }

    static Pattern pattern = Pattern.compile("([^ ]*?)='(.*?)'");

    private static Map createNSNodeMapFromXpath(String xPath) {
        //declare namespace test='http://javector.com/ser/adaptive/po' ./test:item
        HashMap hashMap = new HashMap();
        Matcher matcher = pattern.matcher(xPath);
        if (matcher.find()) {
            hashMap.put(matcher.group(2), matcher.group(1));
        }
        return hashMap;
    }

    private static Map createNSNodeMapFromSource(Node sourceDOM) {
        HashMap hashMap = new HashMap();
        if (sourceDOM.getNamespaceURI() != null && sourceDOM.getNamespaceURI().length() > 0) {
            hashMap.put(sourceDOM.getNamespaceURI(), sourceDOM.getPrefix());
        }

        NamedNodeMap attributes = sourceDOM.getAttributes();
        if (attributes != null) {
            for (int c = 0; c < attributes.getLength(); c++) {
                Node attr = attributes.item(c);
                if (attr.getNodeName().equals("xmlns")) {
                    hashMap.put(attr.getNodeValue(), attr.getNodeName().replaceAll("xmlns", ""));
                }
                if (attr.getNodeName().startsWith("xmlns:") || attr.getNodeName().equals("xmlns")) {
                    hashMap.put(attr.getNodeValue(), attr.getNodeName().replaceAll("xmlns:", ""));
                }
            }
        }
        return hashMap;
    }



}
