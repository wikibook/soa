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
 * Time: 1:43:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class SoajPortDTO {
    private  String endpoint;
    private  String portName;
    private List<SOAJOperationDTO> soajOperationDTOs;

    public List<SOAJOperationDTO> getSoajOperationDTOs() {
        return soajOperationDTOs;
    }

    public void setSoajOperationDTOs(List<SOAJOperationDTO> soajOperationDTOs) {
        this.soajOperationDTOs = soajOperationDTOs;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }
}
