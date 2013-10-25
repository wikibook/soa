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

import java.util.List;

import javax.xml.namespace.QName;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.javector.adaptive.framework.XmlNameMapping;
import com.javector.adaptive.framework.interfaces.XMLMapping;
import com.javector.adaptive.framework.model.AdaptiveMapDTO;
import com.javector.adaptive.framework.model.JavaWrapDTO;
import com.javector.adaptive.framework.model.RuleDTO;
import com.javector.adaptive.framework.model.ScriptDTO;
import com.javector.adaptive.framework.model.StrategyDTO;
import com.javector.adaptive.framework.model.WrapDTO;
import com.javector.adaptive.framework.model.XmlWrapDTO;

/**
 * TestSOAContextUtil
 * 
 * @author Rohit Choudhary; 18th Sept, 2006 Tests the static methods in the
 *         SOAContextUtil.
 */
public class TestSOAContextUtil extends TestCase {

	public static Test suite() {
		return new TestSuite(TestSOAContextUtil.class);
	}

	public void setUp() {

	}

	/**
	 * Tests the GetSerializersForMapping()
	 */
	public void testGetSerializersForMapping() {
		AdaptiveMapDTO adaptiveMapDTO = this.populateAdaptiveMapDTO();
		XMLMapping xmlMapping = new XMLMapping(this.getQName("Type"), this
				.getQName("Name"), "defaultClass");

		// Case 1: XMLType of the StrategyDTO and xmlMapping are the same.
		List<StrategyDTO> list = SOAContextUtil.getSerializersForMapping(
				adaptiveMapDTO, xmlMapping);
		assertEquals(
				"Vaules of XMLType in the Strategy DTO, and XMLMapping differ",
				this.getQName("Type"), list.iterator().next().getXmlType());

		// Case 2: XMLType of the StrategyDTO and xmlMapping are the different.
		list = SOAContextUtil.getSerializersForMapping(adaptiveMapDTO,
				xmlMapping);
		assertEquals("", this.getQName("Name"), list.iterator().next()
				.getXmlName());
	}

	/**
	 * Tests the getSerializersForXmlName()
	 */
	public void testGetSerializersForXmlName() {
		AdaptiveMapDTO adaptiveMapDTO = this.populateAdaptiveMapDTO();
		QName qname = this.getQName("Name");

		List<StrategyDTO> list = SOAContextUtil.getSerializersForXmlName(
				adaptiveMapDTO, qname);
		assertEquals("Vaules of XMLName in the Strategy DTO, and Qname differ",
				qname, list.iterator().next().getXmlName());

	}

	/**
	 * Tests the getDeserializersForXmlName()
	 */
	public void testGetDeserializersForXmlName() {
		AdaptiveMapDTO adaptiveMapDTO = this.populateAdaptiveMapDTO();
		QName qname = this.getQName("Name");

		List<StrategyDTO> list = SOAContextUtil.getDeserializersForXmlName(
				adaptiveMapDTO, qname);
		assertEquals("Vaules of XMLName in the Strategy DTO, and Qname differ",
				qname, list.iterator().next().getXmlName());

	}

	/**
	 * Tests the getDeserializersForXmlType()
	 */
	public void testGetSerializersForXmlType() {
		AdaptiveMapDTO adaptiveMapDTO = this.populateAdaptiveMapDTO();
		QName qname = this.getQName("Type");

		List<StrategyDTO> list = SOAContextUtil.getSerializersForXmlType(
				adaptiveMapDTO, qname);
		assertEquals("Vaules of XMLtype in the Strategy DTO, and Qname differ",
				qname, list.iterator().next().getXmlType());

	}

	/**
	 * Tests the getSerializersForJavaClass()
	 */
	public void testGetSerializersForJavaClass() {
		AdaptiveMapDTO adaptiveMapDTO = this.populateAdaptiveMapDTO();
		QName qname = this.getQName("Name");

		List<StrategyDTO> list = SOAContextUtil.getSerializersForJavaClass(
				adaptiveMapDTO, qname);
		assertEquals("Vaules of XMLName in the Strategy DTO, and Qname differ",
				qname, list.iterator().next().getXmlName());

	}

	/**
	 * Tests the getDeserializersForJavaClass()
	 */
	public void testgetDeserializersForTypeMapping() {
		AdaptiveMapDTO adaptiveMapDTO = this.populateAdaptiveMapDTO();
		XMLMapping xmlMapping = new XMLMapping(this.getQName("Type"), this
				.getQName("Name"), "defaultClass");

		// Case 1: XMLType of the StrategyDTO and xmlMapping are the same.
		List<StrategyDTO> list = SOAContextUtil.getDeserializersForTypeMapping(
				adaptiveMapDTO, xmlMapping);
		assertEquals(
				"Vaules of XMLType in the Strategy DTO, and XMLMapping differ",
				this.getQName("Type"), list.iterator().next().getXmlType());

		// Case 2: XMLType of the StrategyDTO and xmlMapping are the different.
		list = SOAContextUtil.getDeserializersForTypeMapping(adaptiveMapDTO,
				xmlMapping);
		assertEquals("", this.getQName("Name"), list.iterator().next()
				.getXmlName());
	}

	/**
	 * Tests the getDeserializersForNameMapping()
	 */
	public void testGgetDeserializersForNameMapping() {
		AdaptiveMapDTO adaptiveMapDTO = this.populateAdaptiveMapDTO();
		XmlNameMapping xmlNameMapping = new XmlNameMapping(this
				.getQName("Name"), "defaultClass");

		List<StrategyDTO> list = SOAContextUtil.getDeserializersForNameMapping(
				adaptiveMapDTO, xmlNameMapping);
		assertEquals(
				"Values of XMlName in StrategyDTO and XMLNameMapping Differ",
				xmlNameMapping.getXmlName(), list.iterator().next()
						.getXmlName());
		assertEquals("Values of the JavaClassNames Differ", xmlNameMapping.getJavaClassName(), list.iterator()
				.next().getJavaClass());

	}

	/**
	 * Returns the populated adaptiveMapDTO.
	 * 
	 * @return
	 */
	private AdaptiveMapDTO populateAdaptiveMapDTO() {
		AdaptiveMapDTO adaptiveMapDTO = new AdaptiveMapDTO();
		StrategyDTO[] strategyDTOs = new StrategyDTO[1];
		StrategyDTO strategyDTO = new StrategyDTO();
		RuleDTO ruleDTO = new RuleDTO();
		WrapDTO wrapDTO = new WrapDTO();
		ScriptDTO scriptDTO = new ScriptDTO();
		JavaWrapDTO javaWrapDTO = new JavaWrapDTO();
		XmlWrapDTO xmlWrapDTO = new XmlWrapDTO();

		javaWrapDTO.setJavaClass("defaultClass");
		javaWrapDTO.setJavaName("defaultName");

		xmlWrapDTO.setXmlName("defaultClass");
		xmlWrapDTO.setXmlType("defaultName");

		scriptDTO.setGroovy(true);
		scriptDTO.setScriptData("scriptData");
		scriptDTO.setXSLT(true);

		wrapDTO.setJavaWrapDTO(javaWrapDTO);
		wrapDTO.setXmlWrapDTO(xmlWrapDTO);

		ruleDTO.setAttribute(true);
		ruleDTO.setDeserialization(true);
		ruleDTO.setElement(true);
		ruleDTO.setField(true);
		ruleDTO.setHandler("handler");
		ruleDTO.setJavaClass("defaultClass");
		ruleDTO.setJavaName("defaultName");
		ruleDTO.setMethod(true);
		ruleDTO.setScriptDTO(scriptDTO);
		ruleDTO.setSerialization(true);
		ruleDTO.setWrapDTO(wrapDTO);
		ruleDTO.setXmlName(this.getQName("Name"));
		ruleDTO.setXmlType(this.getQName("Type"));

		strategyDTO.setJavaClass("defaultClass");
		strategyDTO.setXmlName(this.getQName("Name"));
		strategyDTO.setXmlType(this.getQName("Type"));

		strategyDTOs[0] = strategyDTO;
		adaptiveMapDTO.setStrategyDTOs(strategyDTOs);
		return adaptiveMapDTO;
	}

	/**
	 * Returns a Qname object, for the QName for the XMLType for the XMLName
	 * 
	 * @return
	 */
	private QName getQName(String arg) {
		return new QName("http://localhost:4848/default", arg);
	}

	public void tearDown() {

	}

}
