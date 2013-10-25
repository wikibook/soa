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

import com.javector.adaptive.framework.util.BaseXPathEvaluator;
import com.javector.adaptive.framework.util.BaseReflectionUtil;
import com.javector.adaptive.framework.util.SOAUtil;
import com.javector.adaptive.framework.dto.DeserializeBindingRuleDTO;
import com.javector.adaptive.framework.model.RuleDTO;
import com.javector.adaptive.framework.model.ScriptDTO;
import com.javector.soaj.SoajRuntimeException;

import javax.xml.transform.TransformerException;
import java.util.logging.Logger;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.w3c.dom.*;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.XPathAPI;
import org.apache.xml.utils.PrefixResolver;

/**
 * Created by IntelliJ IDEA.
 * Auther: Rohit Agarwal
 * Date: Jun 5, 2006
 * Time: 7:25:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class JaxbXpathEvaluatorPR extends BaseXPathEvaluator  {
    private static Logger log =
            Logger.getLogger(JaxbXpathEvaluatorPR.class.getName());

    static Pattern pattern = Pattern.compile("([^ ]*?)='(.*?)'");

    private static String DEFAULT_PREFIX = "DEFAULT_PREFIX";

    /**
     * Apply xpath for complex type xpath where the source xml  Object is not fo String type
     * @param bindingRuleDTO
     * @param sourceXmlObject
     * @return
     * @
     */
    private Object[] applyXpathForComplexTypes(DeserializeBindingRuleDTO bindingRuleDTO, Object sourceXmlObject)  {
        Document document = null;
        XObject dataObject;
        BaseReflectionUtil reflectionUtil = bindingRuleDTO.getReflectionUtil();
        RuleDTO ruleDTO = bindingRuleDTO.getRuleDTO();
        Collection jaxbObjCollection = new ArrayList();
        if(reflectionUtil.belongsToXMLSchemaNameSpace(ruleDTO.getXmlType())) {
            document = SOAUtil.createDummyDocumentForString(sourceXmlObject.toString());
        } else {
            String s = reflectionUtil.convertXMLObjectToString(sourceXmlObject, bindingRuleDTO.getCl());
            document = SOAUtil.convertStringToDocument(s);
        }
        String  xPath = ruleDTO.getScriptDTO().getScriptData();
        Map<String,String> uriMappings = createNSNodeMapFromXpath(xPath,document);
        xPath = xPath.replaceAll("namespace|declare", "").replaceAll("[^ ]*=[^ ]*", "").replaceAll("\n", "").trim();
        JavectorPrefixResolver prefixResolver = new JavectorPrefixResolver();
        prefixResolver.setUriMap(uriMappings);
        try {
            dataObject = XPathAPI.eval(document,xPath,prefixResolver);
            if(dataObject instanceof XString) {
                return new Object[]{dataObject.toString()};
            }

            if(dataObject instanceof XNodeSet) {
                XNodeSet set = (XNodeSet)dataObject;
                NodeList  list = set.nodelist();
                Node node;
                for(int i=0; i<list.getLength();i++) {
                    node  = list.item(i);
                    Object jaxbObj = reflectionUtil.createXMLObject(ruleDTO.getXmlType(),bindingRuleDTO.getCl());
                    jaxbObjCollection.add(reflectionUtil.createXMLObjectFromXmlNode(bindingRuleDTO.getCl(),node,sourceXmlObject,jaxbObj.getClass()));
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
            return applyXpathForComplexTypes(bindingRuleDTO,sourceObject);
        }
    }

    private Object applyXpathForString(DeserializeBindingRuleDTO bindingRuleDTO, Object sourceObject)  {
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





    private static Map<String,String> createNSNodeMapFromXpath(String xPath,Document document) {
        //declare namespace test='http://javector.com/ser/adaptive/po' ./test:item
        HashMap<String,String> nameSpaceMap = new HashMap<String,String>();
        Matcher matcher = pattern.matcher(xPath);
        if (matcher.find()) {
            nameSpaceMap.put(matcher.group(2), matcher.group(1));
        }
        if(nameSpaceMap.isEmpty()) {
            String namespaceURI = document.getFirstChild().getNamespaceURI();
            nameSpaceMap.put(DEFAULT_PREFIX,namespaceURI);
        }
        return nameSpaceMap;
    }

    /**
     * Inner class for this xpath evaluator for resolving all name space
     * conflicts for a given xpath.
     */

    class JavectorPrefixResolver implements PrefixResolver {

        Map<String, String> uriMap;

        public Map getUriMap() {
            return uriMap;
        }

        public void setUriMap(Map<String, String> uriMap) {
            this.uriMap = uriMap;
        }

        public String getNamespaceForPrefix(String string) {
            if(string == null ||string.equals("")) {
                return uriMap.get(DEFAULT_PREFIX);
            }
            return uriMap.get(string);
        }

        public String getNamespaceForPrefix(String string, Node node) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getBaseIdentifier() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }


        // in  case there is no name space  declared by the user
        // then name space corresponding to first child element of the xml Surce Object will be used to resolve all
        // name space conflicts.

        public boolean handlesNullPrefixes() {
            return true;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}





