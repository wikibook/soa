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

import com.javector.adaptive.framework.model.ConfigurationDetailDTO;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Apr 7, 2006
 * Time: 9:14:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class Configurator {

    private static String jaxbFilePath = "config/SOAJaxbHandlerMapping.xml";
    private static String xmlBeanFilePath  = "";

    public ConfigurationDetailDTO getDetailDTO() {
        return detailDTO;
    }

    public Configurator(ConfigurationDetailDTO detailDTO) {
        this.detailDTO = detailDTO;
    }

    private ConfigurationDetailDTO detailDTO;

    }


