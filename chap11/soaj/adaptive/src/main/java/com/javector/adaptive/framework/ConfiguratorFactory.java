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

import com.javector.soaj.SoajRuntimeException;

import java.util.Map;
import java.util.HashMap;

public class ConfiguratorFactory {

    private static Map<String, Configurator> configurationMapping = new HashMap<String, Configurator>();
    public static Configurator DEFAULT_JAXB_CONFIGURATOR;
    static{
        DEFAULT_JAXB_CONFIGURATOR = createConfigurator("config/SOAJaxbHandlerMapping.xml");
    }
    public static Configurator createConfigurator(String filePath) {
        Configurator configurator;
        configurator = configurationMapping.get(filePath);
        if(configurator!=null) {
            return configurator;                 
        }
        configurator  = ConfigMapper.parseConfigurationDetail(filePath);
        configurationMapping.put(filePath,configurator);
        return configurator;
    }
}
