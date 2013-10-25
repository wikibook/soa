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
package com.javector.adaptive.util;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import com.javector.adaptive.util.dto.SOAConfigDTO;
import com.javector.adaptive.util.dto.SOAJOperationDTO;
import com.javector.soaj.SoajException;
import com.javector.soaj.SoajException;
import com.javector.soaj.SoajRuntimeException;
import com.javector.soaj.config.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

/**
 * Created by IntelliJ IDEA.
 * User: javector
 * Date: May 13, 2006
 * Time: 1:00:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestSOAConfigReader extends TestCase {

    public TestSOAConfigReader(String s){
        super(s);
    }

    public void testParserSOAConfig(){
        SOAConfigReader soaConfigReader = new SOAConfigReader();

        try {
            InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/SoajConfig.xml");
            SOAConfigDTO soaConfigDTO = soaConfigReader.parserSOAConfig(resourceAsStream);
            SOAJOperationDTO soajOperationDTO = soaConfigDTO.getOperationDTOForOperationName("OperationName");
            assertEquals("invokeService",soajOperationDTO.getTargetServiceMethodName());
        }  catch (SoajRuntimeException e) {
            fail("failed to process config file");
            e.printStackTrace();
        }

    }




}
