/*
* Copyright 2006 Javector Software LLC
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
package samples;

import javax.xml.bind.annotation.AccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.ws.WebFault;

@WebFault(name = "inputMessageValidationFault", 
    targetNamespace = "http://www.example.com/faults")
public class InputFault extends Exception {

    private FaultDetail faultInfo;

    public InputFault(String message, 
        FaultDetail faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    public InputFault(String message, 
        FaultDetail faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    public FaultDetail getFaultInfo() {
        return faultInfo;
    }

    @XmlAccessorType(AccessType.FIELD)
    @XmlType(name = "InputMessageValidationFaultType")
    public static class FaultDetail {

        @XmlAttribute
        protected String msg;

    }
    
}
