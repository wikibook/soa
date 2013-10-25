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
package com.javector.soaj.provider.processing;

import com.javector.adaptive.util.dto.SOAConfigDTO;

import javax.xml.transform.Source;
import java.util.List;

/**
 * Encapsulates a SOAJ request and its context.  In addition to containing the
 * message (SOAP), this contains the HTTP request context needed for processing.
 * The requestURL has the form:
 * <p>http://server/pathString?queryString
 *
 */
public class WSRequest {
    String queryString;
    String pathString;

    // TODO we should NOT store soapRequest as a String.  It should remain a
    // Source as long as possible.  Maybe use pull-parser.  Mark to think about
    // this.
    boolean isServiceRequest;
    boolean isWSDLRequest;
    private String endpoint;
    private SOAConfigDTO soaConfigDTO;
    private String operationName;
    // TODO should be Source - not String
    private List<Source> xmlParameters;
    private String requestURL;
    private String generatedWsdl;

    public void setGeneratedWsdl(String generatedWsdl) {
        this.generatedWsdl = generatedWsdl;
    }

    public List<Source> getXmlParameters() {
        return xmlParameters;
    }

    public void setXmlParameters(List<Source> paramObjects) {
        this.xmlParameters = paramObjects;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public void setSoaConfigDTO(SOAConfigDTO soaConfigDTO) {
        this.soaConfigDTO = soaConfigDTO;
    }

    public boolean isServiceRequest() {
        return isServiceRequest;
    }

    public void setServiceRequest(boolean serviceRequest) {
        isServiceRequest = serviceRequest;
    }

    public boolean isWSDLRequest() {
        return isWSDLRequest;
    }

    public void setWSDLRequest(boolean WSDLRequest) {
        isWSDLRequest = WSDLRequest;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String messageContextQueryString) {
        this.queryString = messageContextQueryString;
    }

    public String getPathString() {
        return pathString;
    }

    public void setPathString(String pathString) {
        this.pathString = pathString;
    }

    public SOAConfigDTO getSoaConfigDTO() {
        return soaConfigDTO;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public String getGeneratedWsdl() {
        return generatedWsdl;
    }
}
