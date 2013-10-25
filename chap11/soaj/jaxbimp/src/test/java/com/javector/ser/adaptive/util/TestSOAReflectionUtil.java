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
package com.javector.ser.adaptive.util;

import com.javector.adaptive.framework.util.SOAReflectionUtil;
import com.javector.adaptive.framework.model.AdaptiveMapDTO;
import com.javector.adaptive.framework.model.StrategyDTO;
import com.javector.ser.adaptive.AdaptiveMap;



import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: javector
 * Date: Mar 20, 2006
 * Time: 8:26:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestSOAReflectionUtil extends TestCase{

    public void testSetPropertyValue() throws Exception {
        AdaptiveMapDTO  map = new AdaptiveMapDTO();
        StrategyDTO[] dtos = new StrategyDTO[10];
        for(int i=0; i<10; i++) {
            dtos[i]  = new StrategyDTO();
            dtos[i].setJavaClass("rohit"+i);
        }
        map.setStrategyDTOs(dtos);
        SOAReflectionUtil.setPropertyValueDefault(map,"strategyDTOs",dtos);
        System.out.println("adaptive map = " +map.getStrategyDTOs()[1]);
    }

}
