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
package com.javector.adaptive.util.dto;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: javector
 * Date: May 14, 2006
 * Time: 12:31:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class WsdlDTO {
  
   private String wsdlNamespace;
   private  List<SoajServiceDTO> soajServiceDTOs;
   private String wsdlName;

    public String getWsdlName() {
        return wsdlName;
    }

    public void setWsdlName(String wsdlName) {
        this.wsdlName = wsdlName;
    }

    public List<SoajServiceDTO> getSoajServiceDTOs() {
        return soajServiceDTOs;
    }

    public void setSoajServiceDTOs(List<SoajServiceDTO> soajServiceDTOs) {
        this.soajServiceDTOs = soajServiceDTOs;
    }

    public String getWsdlNamespace() {
        return wsdlNamespace;
    }

    public void setWsdlNamespace(String wsdlNamespace) {
        this.wsdlNamespace = wsdlNamespace;
    }

}
