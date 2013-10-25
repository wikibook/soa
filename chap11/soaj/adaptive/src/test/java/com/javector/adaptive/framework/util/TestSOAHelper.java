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

import javax.xml.namespace.QName;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.javector.adaptive.framework.AdaptiveMapper;
import com.javector.adaptive.framework.model.AdaptiveMapDTO;
import com.javector.adaptive.framework.model.RuleDTO;
import com.javector.adaptive.framework.model.StrategyDTO;
import com.javector.adaptive.mock.MockGetterClass;

/**
 * TestSOAHelper 
 * Rohit Choudhary 
 * 29 Sept 2006
 * 
 * Test case to check SOAHelper Methods
 * a. getConverteorValueForXML 
 * b. getConvertorValueForJava
 * 
 * 
 */
public class TestSOAHelper extends TestCase {
	public static Test suite() {
		return new TestSuite(TestSOAHelper.class);
	}

	public void setUp() {

	}

	/**
	 * Tests the getConvertorValueForXML method.
	 * a. condition when the JavaGetterMethod is not null, it specifies
	 * the name of the getter method
	 */
	public void testGetConvertorValueForXMLJavaGetterNotNull() {
		AdaptiveMapDTO adaptiveMapDTO = AdaptiveMapper
				.parseAdaptiveXML("TestSOAHelper.xml");
		StrategyDTO[] strategyDTOs = adaptiveMapDTO.getStrategyDTOs();
		RuleDTO[] ruleDTOs = strategyDTOs[0].getRuleDTOs();
		MockGetterClass mockGetterClass = new MockGetterClass();
		Object object = SOAHelper.getConvertorValueForXML(
				"com.javector.adaptive.mock.MockSimpleTypeConvertor",
				ruleDTOs[0], mockGetterClass, new QName(
						"http://localhost:8080/default", "integer"));
		assertTrue(object instanceof Integer);
	}

	/**
	 * Tests the getConvertorValueForXML method
	 * a. condition when the JavaGetterMethod is set to null, javaName
	 * value is used.
	 */
	public void testGetConvertorValueForXMLJavaGetterNull() {
		AdaptiveMapDTO adaptiveMapDTO = AdaptiveMapper
				.parseAdaptiveXML("TestSOAHelper.xml");
		StrategyDTO[] strategyDTOs = adaptiveMapDTO.getStrategyDTOs();
		RuleDTO[] ruleDTOs = strategyDTOs[0].getRuleDTOs();
		MockGetterClass mockGetterClass = new MockGetterClass();
		Object object = SOAHelper.getConvertorValueForXML(
				"com.javector.adaptive.mock.MockSimpleTypeConvertor",
				ruleDTOs[1], mockGetterClass, new QName(
						"http://localhost:8080/default", "integer"));
		assertTrue(object instanceof Integer);

	}
	/**
	 * Tests the getConvertorValueForXML method
	 * a. condition when the Javaclassname and the convertedValue
	 * are different.
	 */
	public void testGetConvertorValueForXML() {
		AdaptiveMapDTO adaptiveMapDTO = AdaptiveMapper
				.parseAdaptiveXML("TestSOAHelper.xml");
		StrategyDTO[] strategyDTOs = adaptiveMapDTO.getStrategyDTOs();
		RuleDTO[] ruleDTOs = strategyDTOs[0].getRuleDTOs();
		MockGetterClass mockGetterClass = new MockGetterClass();
		 Object object= SOAHelper.getConvertorValueForXML(
				"com.javector.adaptive.mock.MockSimpleTypeConvertor",
				ruleDTOs[0], mockGetterClass, new QName(
						"http://localhost:8080/default", "string"));
		 assertTrue(object instanceof String ) ;

	}
	/**
	 * Tests the getConvertorValueForJAVA method
	 * a. 
	 */
	public void testGetConvertorValueForJAVA(){
		SOAHelper.getConvertorValueForJava("com.javector.adaptive.mock.MockSimpleTypeConvertor",
				"name",new String(),"com.javector.adaptive.mock.MockGetterClass");
		
	}
	public void tearDown() {

	}
}
