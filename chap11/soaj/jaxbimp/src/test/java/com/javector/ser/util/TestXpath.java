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
package com.javector.ser.util;

import junit.framework.TestCase;
import junit.framework.Assert;
import org.w3c.dom.Document;
import org.apache.xpath.XPathAPI;
import com.javector.adaptive.framework.util.SOAUtil;

/**
 * Created by IntelliJ IDEA.
 * Auther: Rohit Agarwal
 * Date: May 20, 2006
 * Time: 11:58:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestXpath extends TestCase  {


    public void testXPathEvaluator() throws Exception {
        String xmlString = "rohit-agarwal";
        Document  document = SOAUtil.createDummyDocumentForString(xmlString);
        String xPath = "substring-before(.,'-')";
        String value = XPathAPI.eval(document,xPath).toString();
        Assert.assertEquals("rohit",value);
    }
}
