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

import com.javector.adaptive.framework.interfaces.BaseSOAContext;
import com.javector.adaptive.framework.interfaces.AdaptiveContext;
import com.javector.adaptive.framework.model.ConfigurationDetailDTO;
import com.javector.soaj.SoajException;
import com.javector.soaj.SoajRuntimeException;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Kishore G
 * Date: Jan 12, 2006
 * Time: 9:54:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdaptiveContextFactory {
    //have the logic here to create the context factory
    BaseSOAContext createContext(String name) throws SoajException {
        try {
            return (BaseSOAContext) Class.forName(name).newInstance();
        } catch (Exception e) {
            throw new SoajException("Exception while initializing context");
        }
    }

    public static AdaptiveContext createNewContext(Configurator configurator, String adaptiveMappingFile, URL[] urls)  {

//    TODO class name is from property files.
        BaseSOAContext implSoaContext;
        ConfigurationDetailDTO detailDTO = configurator.getDetailDTO();
            try {
              implSoaContext = (BaseSOAContext) Class.forName(detailDTO.getConfigurationContextClassName()).newInstance();
            } catch (Exception e) {
              throw new SoajRuntimeException("Failed to create an instance of the Adaptive Context.", e);
            }
            implSoaContext.init(adaptiveMappingFile, urls, detailDTO);
        return implSoaContext;
        
    }
}
