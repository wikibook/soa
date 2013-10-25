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
package com.javector.adaptive.framework;

import javax.xml.namespace.QName;

import com.javector.adaptive.framework.model.AdaptiveMapDTO;
import com.javector.adaptive.framework.model.RuleDTO;
import com.javector.adaptive.framework.model.StrategyDTO;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * TestAdaptiveMapper Rohit Choudhary 16 Sept 2006
 * 
 * Test Case to check the creation of AdaptiveMapDTO and validate its structure.
 * a. Tests the structure of every StrategyDTO
 * b. Tests the structure of one RuleDTO inside a StrategyDTO.
 */
public class TestAdaptiveMapper extends TestCase {
	public static Test suite() {
		return new TestSuite(TestAdaptiveMapper.class);
	}

	public void setUp() {
	}

	/**
	 * Tests the testparseAdaptiveXML() of AdaptiveMapper.java
	 * 
	 * @throws Exception
	 */
	public void testparseAdaptiveXML() throws Exception {
		String xmlPath = "TestAdaptiveMapper.xml";
		String test = "http://javector.com/ser/adaptive/po";
		String xs = "http://www.w3.org/2001/XMLSchema";

		AdaptiveMapDTO adaptiveMapDTO = AdaptiveMapper
				.parseAdaptiveXML(xmlPath);
		StrategyDTO[] strategyDTOs = adaptiveMapDTO.getStrategyDTOs();

		RuleDTO[] ruleDTOs = strategyDTOs[0].getRuleDTOs();

		// For the StrategyDTO[0]
		assertEquals(strategyDTOs[0].getJavaClass(),
				"com.javector.ser.adaptive.po.MyItem");
		assertEquals(strategyDTOs[0].getXmlType(), new QName(test, "item"));
		assertEquals(strategyDTOs[0].getXmlName(), new QName(test, "item"));

		assertEquals(ruleDTOs[0].getJavaName(), "name");
		assertEquals(ruleDTOs[0].getXmlName(), new QName(test, "productName"));
		assertEquals(ruleDTOs[0].getXmlType(), new QName(xs, "string"));

		// For the StrategyDTO[1]
		assertEquals(strategyDTOs[1].getJavaClass(),
				"com.javector.ser.adaptive.po.Address");
		assertEquals(strategyDTOs[1].getXmlType(),
				new QName(test, "billToType"));
		assertEquals(strategyDTOs[1].getXmlName(), new QName(test, "billTo"));

		RuleDTO[] ruleDTOs2 = strategyDTOs[1].getRuleDTOs();
		assertEquals(ruleDTOs2[0].getJavaName(), ".");
		assertEquals(ruleDTOs2[0].getXmlName(), new QName(test, "street"));
		assertEquals(ruleDTOs2[0].getXmlType(), new QName(xs, "string"));

		// For the StrategyDTO[2]
		assertEquals(strategyDTOs[2].getJavaClass(),
				"com.javector.ser.adaptive.po.Phone");
		assertEquals(strategyDTOs[2].getXmlType(), new QName(xs, "string"));
		RuleDTO[] ruleDTOs3 = strategyDTOs[2].getRuleDTOs();

		assertEquals(ruleDTOs3[0].getJavaName(), "areaCode");
		assertEquals(ruleDTOs3[0].getXmlType(), new QName(xs, "string"));

		// For the StrategyDTO[3]
		assertEquals(strategyDTOs[3].getJavaClass(),
				"com.javector.ser.adaptive.po.MyPurchaseOrder");
		assertEquals(strategyDTOs[3].getXmlType(), new QName(test,
				"PurchaseOrder"));

		RuleDTO[] ruleDTOs4 = strategyDTOs[3].getRuleDTOs();

		//For the StrategyDTO[4]
		assertEquals(ruleDTOs4[0].getJavaName(), "billTo");
		assertEquals(ruleDTOs4[0].getXmlName(), new QName(test, "billTo"));
		assertEquals(ruleDTOs4[0].getXmlType(), new QName(test, "billToType"));
		assertEquals(ruleDTOs4[0].getJavaClass(),
				"com.javector.ser.adaptive.po.Address");

	}

	public void tearDown() {

	}
}
