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
package com.javector.ser.framework;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class TestAdaptiveMapper extends TestCase {

    public static Test suite() {
        return new TestSuite(TestAdaptiveMapper.class);
    }

    protected void setUp() throws Exception {


    }

    public void testDummy(){
        assertTrue(true);
    }

//    public void testparseAdaptiveMappingXML() throws Exception {
//
//        AdaptiveMapDTO adaptiveMapDTO = AdaptiveMapper.parseAdaptiveXML("com/javector/ser/framework/SOAAdaptiveSample.xml");
//        assertNotNull(adaptiveMapDTO);
//
//        assertEquals(adaptiveMapDTO.getStrategyDTOs().length, 4);
//
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[0].getRuleDTOs().length, 3);
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[0].getJavaClass(), "com.javector.ser.adaptive.po.MyItem");
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[0].getXmlType(), new QName("http://javector.com/ser/adaptive/po", "item"));
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[0].getXmlName(), new QName("http://javector.com/ser/adaptive/po", "item"));
//
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[0].getRuleDTOs()[0].getJavaName(), "name");
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[0].getRuleDTOs()[0].getXmlName(), new QName("http://javector.com/ser/adaptive/po", "productName"));
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[0].getRuleDTOs()[0].isAttribute(), true);
//
//
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[0].getRuleDTOs()[1].getJavaName(), "quantity");
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[0].getRuleDTOs()[1].getXmlName(), new QName("http://javector.com/ser/adaptive/po", "quantity"));
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[0].getRuleDTOs()[1].isElement(), true);
//
//
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[0].getRuleDTOs()[2].getJavaName(), "price");
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[0].getRuleDTOs()[2].getXmlName(), new QName("http://javector.com/ser/adaptive/po", "price"));
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[0].getRuleDTOs()[2].isElement(), true);
//
//
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getJavaClass(), "com.javector.ser.adaptive.po.Address");
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getXmlType(), new QName("http://javector.com/ser/adaptive/po", "billToType"));
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getXmlName(), new QName("http://javector.com/ser/adaptive/po", "billTo"));
//
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs().length, 8);
//
//
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[0].getJavaName(), ".");
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[0].getXmlName(), new QName("http://javector.com/ser/adaptive/po", "street"));
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[0].isElement(), true);
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[0].isSerialization(), true);
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[0].getScriptDTO().getScriptData(),
//                "return source.getStreetNum()+\" \"+source.getStreetName();");
//
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[1].getJavaName(), "streetNum");
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[1].getXmlName(), new QName("http://javector.com/ser/adaptive/po", "street"));
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[1].isElement(), true);
////        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[1].getScriptDTO().getScriptData(),
////               "declare namespace test='http://javector.com/ser/adaptive/po'\n" +
////                       "                            substring-before(substring-after(.,'('),')')");
//
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[2].getJavaName(), "streetName");
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[2].getXmlName(), new QName("http://javector.com/ser/adaptive/po", "street"));
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[2].isElement(), true);
////        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[2].getScriptDTO().getScriptData(),
////                "declare namespace test='http://javector.com/ser/adaptive/po'\n" +
////                        "                            normalize-space(substring-before(substring-after(.,')'),'-'))");
//
//
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[3].getJavaName(), "city");
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[3].getXmlName(), new QName("http://javector.com/ser/adaptive/po", "city"));
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[3].isElement(), true);
//
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[4].getJavaName(), "state");
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[4].getXmlName(), new QName("http://javector.com/ser/adaptive/po", "state"));
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[4].isElement(), true);
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[4].isSerialization(), true);
//
//
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[5].getJavaName(), "zip");
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[5].getXmlName(), new QName("http://javector.com/ser/adaptive/po", "zip"));
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[5].isElement(), true);
//
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[6].getJavaName(), "phoneNumber");
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[6].getXmlName(), new QName("http://javector.com/ser/adaptive/po", "phone"));
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[6].isElement(), true);
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[6].isSerialization(), false);
////        assertEquals(adaptiveMapDTO.getStrategyDTOs()[1].getRuleDTOs()[6].getScriptDTO().getScriptData(),
////                "String area = Integer.toString(source.getAreaCode());return \"(\"+area+\")\"+\" \"+source.getExchange()+\"-\"+source.getNumber();");
//
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[2].getJavaClass(), "com.javector.ser.adaptive.po.Phone");
//     //   assertEquals(adaptiveMapDTO.getStrategyDTOs()[2].getXmlType(), new QName("http://javector.com/ser/adaptive/po", "PurchaseOrder"));
//        assertEquals(adaptiveMapDTO.getStrategyDTOs()[2].getRuleDTOs().length, 3);
//
//
//
//
//    }
//
////     public static void main(String args[]) {
////        String[] testCaseName = {TestAdaptiveMapper.class.getName()};
////        junit.textui.TestRunner.main(testCaseName);
////    }
//

}
