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

import com.javector.soaj.config.UserDefinedSchema;

import java.util.ArrayList;
import java.util.List;

public class SOAConfigDTO {
  
    private ArrayList<WsdlDTO> wsdlDTOs;
    private List<UserDefinedSchema> userDefinedSchemas;
    private String mappingXml;
    private long lastModified = 0;
    

    public List<UserDefinedSchema> getUserDefinedSchemas() {
        return userDefinedSchemas;
    }

    public void setUserDefinedSchemas(List<UserDefinedSchema> userDefinedSchemas) {
        this.userDefinedSchemas = userDefinedSchemas;
    }


    public String getMappingXml() {
        return mappingXml;
    }

    public void setMappingXml(String mappingXml) {
        this.mappingXml = mappingXml;
    }


    public ArrayList<WsdlDTO> getWsdlDTOs() {
        return wsdlDTOs;
    }

    public void setWsdlDTOs(ArrayList<WsdlDTO> wsdlDTOs) {
        this.wsdlDTOs = wsdlDTOs;
    }

    public SOAJOperationDTO getOperationDTOForOperationName(String operationName) {
        SOAJOperationDTO dto = null;
        for (WsdlDTO wsdlDTO : wsdlDTOs) {
            List<SoajServiceDTO> soajServiceDTOs = wsdlDTO.getSoajServiceDTOs();
            for (SoajServiceDTO soajServiceDTO : soajServiceDTOs) {
                List<SoajPortDTO> soajPortDTOs = soajServiceDTO.getSoajPortDTOs();
                for (SoajPortDTO soajPortDTO : soajPortDTOs) {
                    List<SOAJOperationDTO> soajOperationDTOs = soajPortDTO.getSoajOperationDTOs();
                    for (SOAJOperationDTO soajOperationDTO : soajOperationDTOs) {
                        if (operationName.equals(soajOperationDTO.getOperationName())) {
                            dto = soajOperationDTO;
                        }
                    }
                }
            }
        }
        return dto;
    }

    public long getLastModified() {
      return lastModified;
    }

    public void setLastModified(long lastModified) {
      this.lastModified = lastModified;
    }

}
