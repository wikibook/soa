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
package com.javector.adaptive.framework.interfaces;

import javax.xml.namespace.QName;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * TestXMLMapping Rohit Choudhary 16 Sept 2006
 * 
 * Test Case to check the XMLMapping for two scenarios a. when XMLType is not
 * null b. when XMLType is null
 */
public class TestXMLmapping extends TestCase {
	public static Test suite() {
		return new TestSuite(TestXMLmapping.class);
	}

	protected void setup() throws Exception {

	}

	/**
	 * Test case for getTypeOrElementName when XMLType is not null.
	 * 
	 * @throws Exception
	 */
	public void testGetTypeOrElementQNameXMLTypeNotNull() throws Exception {
		// to test when the XMLType is not null
		QName xmlType = new QName("http://localhost:4848/sample",
				"XmlTypeNotNull");
		QName xmlName = new QName("http://locahost:4849/default", "default");
		XMLMapping xml = new XMLMapping(xmlType, xmlName, null);
		QName testResult = xml.getTypeOrElementQName();
		assertEquals("xmlType not null, yet xmlName was returned",xmlType, testResult);

	}

	/**
	 * Test Case for getTypeOrElementName when XMLType is null
	 * 
	 * @throws Exception
	 */
	public void testGetTypeOrElementQNameXMLTypeNull() throws Exception {
		QName xmlType = null;
		QName xmlName = new QName("http://locahost:4849/default", "default");
		XMLMapping xml = new XMLMapping(xmlType, xmlName, null);
		QName testResult = xml.getTypeOrElementQName();
		assertEquals("xmlType null, yet xmlType was returned", xmlName, testResult);
	}

	protected void teardown() {

	}
}
