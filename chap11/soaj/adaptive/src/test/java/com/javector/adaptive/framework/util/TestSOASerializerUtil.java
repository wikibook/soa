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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.javector.adaptive.framework.AdaptiveMapper;
import com.javector.adaptive.framework.interfaces.BaseAdaptiveDeserializer;
import com.javector.adaptive.framework.interfaces.BaseAdaptiveSerializer;
import com.javector.adaptive.framework.model.AdaptiveMapDTO;
import com.javector.adaptive.framework.model.RuleDTO;
import com.javector.adaptive.framework.model.StrategyDTO;

public class TestSOASerializerUtil extends TestCase {
	public static Test suite() {
		return new TestSuite(TestSOASerializerUtil.class);
	}

	public void setUp() {

	}

	/**
	 * Test Case for the condition when the String serializerClassName is
	 * present.
	 * 
	 * @throws Exception
	 */
	public void testGetPropertyValueDefault() throws Exception {
		AdaptiveMapDTO adaptiveMapDTO = AdaptiveMapper
				.parseAdaptiveXML("TestSOASerializer.xml");
		StrategyDTO[] strategyDTOs = adaptiveMapDTO.getStrategyDTOs();
		RuleDTO[] ruleDTOs = strategyDTOs[0].getRuleDTOs();
		BaseAdaptiveSerializer baseAdaptiveSerializer = SOASerializerUtil
				.getSerializerInstance(strategyDTOs[0],
						"com.javector.adaptive.mock.MockJaxbSerializer",
						ruleDTOs[0]);
		assertTrue((baseAdaptiveSerializer instanceof BaseAdaptiveSerializer));
	}

	/**
	 * Test Case for the condition when the RuleDTO Property Mappings are used.
	 * 
	 * @throws Exception
	 */
	public void testGetPropertyValueDefaultRuleDTOMapping() throws Exception {
		AdaptiveMapDTO adaptiveMapDTO = AdaptiveMapper
				.parseAdaptiveXML("TestSOASerializer.xml");
		StrategyDTO[] strategyDTOs = adaptiveMapDTO.getStrategyDTOs();
		RuleDTO[] ruleDTOs = strategyDTOs[1].getRuleDTOs();
		BaseAdaptiveSerializer baseAdaptiveSerializer = SOASerializerUtil
				.getSerializerInstance(strategyDTOs[1],
						"com.javector.adaptive.mock.MockJaxbSerializer",
						ruleDTOs[0]);
		assertTrue((baseAdaptiveSerializer instanceof BaseAdaptiveSerializer));
	}

	/**
	 * Test Case for the condition when the StrategyDTO Property Mappings are
	 * used.
	 * 
	 * @throws Exception
	 */
	public void testGetPropertyValueDefaultStrategyDTOMapping()
			throws Exception {
		AdaptiveMapDTO adaptiveMapDTO = AdaptiveMapper
				.parseAdaptiveXML("TestSOASerializer.xml");
		StrategyDTO[] strategyDTOs = adaptiveMapDTO.getStrategyDTOs();
		RuleDTO[] ruleDTOs = strategyDTOs[1].getRuleDTOs();
		BaseAdaptiveSerializer baseAdaptiveSerializer = SOASerializerUtil
				.getSerializerInstance(strategyDTOs[2],
						"com.javector.adaptive.mock.MockJaxbSerializer",
						ruleDTOs[0]);
		assertTrue((baseAdaptiveSerializer instanceof BaseAdaptiveSerializer));
	}

	/**
	 * Test Case for the condition when the String deserializerClassName is
	 * present
	 * 
	 * @throws Exception
	 */
	public void testGetDeserilaizerInstance() throws Exception {
		AdaptiveMapDTO adaptiveMapDTO = AdaptiveMapper
				.parseAdaptiveXML("TestSOADeserializer.xml");
		StrategyDTO[] strategyDTOs = adaptiveMapDTO.getStrategyDTOs();
		RuleDTO[] ruleDTOs = strategyDTOs[0].getRuleDTOs();
		BaseAdaptiveDeserializer baseAdaptiveDeserializer = SOASerializerUtil
				.getDeserilaizerInstance(strategyDTOs[0],
						"com.javector.adaptive.mock.MockJaxbDeserializer",
						ruleDTOs[0]);
		assertTrue((baseAdaptiveDeserializer instanceof BaseAdaptiveDeserializer));
	}

	/**
	 * Test Case for the condition when the RuleDTO Mappings are used
	 * 
	 * @throws Exception
	 */
	public void testGetDeserilaizerInstanceRuleDTOMappings() throws Exception {
		AdaptiveMapDTO adaptiveMapDTO = AdaptiveMapper
				.parseAdaptiveXML("TestSOADeserializer.xml");
		StrategyDTO[] strategyDTOs = adaptiveMapDTO.getStrategyDTOs();
		RuleDTO[] ruleDTOs = strategyDTOs[0].getRuleDTOs();
		BaseAdaptiveDeserializer baseAdaptiveDeserializer = SOASerializerUtil
				.getDeserilaizerInstance(strategyDTOs[0],
						"com.javector.adaptive.mock.MockJaxbDeserializer",
						ruleDTOs[0]);
		assertTrue((baseAdaptiveDeserializer instanceof BaseAdaptiveDeserializer));
	}
	
	/**
	 * Test Case for the condition when the StrategyDTO Mappings are used
	 * 
	 * @throws Exception
	 */
	public void testGetDeserilaizerInstanceStrategyDTOMappings() throws Exception {
		AdaptiveMapDTO adaptiveMapDTO = AdaptiveMapper
				.parseAdaptiveXML("TestSOADeserializer.xml");
		StrategyDTO[] strategyDTOs = adaptiveMapDTO.getStrategyDTOs();
		RuleDTO[] ruleDTOs = strategyDTOs[0].getRuleDTOs();
		BaseAdaptiveDeserializer baseAdaptiveDeserializer = SOASerializerUtil
				.getDeserilaizerInstance(strategyDTOs[0],
						"com.javector.adaptive.mock.MockJaxbDeserializer",
						ruleDTOs[0]);
		assertTrue((baseAdaptiveDeserializer instanceof BaseAdaptiveDeserializer));
	}
	public void tearDown() {

	}

}
