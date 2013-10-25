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
package com.javector.soaj.util;


import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;

import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;

import java.util.ArrayList;

public class SoajSAXErrorHandler extends DefaultHandler  {
  
  private static SoajLogger logger = LoggingFactory.getLogger(SoajSAXErrorHandler.class.getName());
  
  private ArrayList<SAXParseException> warnings = new ArrayList<SAXParseException>(10);
  private ArrayList<SAXParseException> errors = new ArrayList<SAXParseException>(10);
  private ArrayList<SAXParseException> fatalErrors = new ArrayList<SAXParseException>(10);
  private ArrayList<SAXParseException> exceptions = new ArrayList<SAXParseException>(10);
  
  public void warning(SAXParseException exception) throws SAXException {
    exceptions.add(exception);
    warnings.add(exception);
    logger.info("got warning " + exception);
  }
  
  public void error(SAXParseException exception) throws SAXException {
    exceptions.add(exception);
    errors.add(exception);
    logger.info("got error " + exception);
  }
  
  public void fatalError(SAXParseException exception) throws SAXException {
    exceptions.add(exception);
    fatalErrors.add(exception);
    logger.info("got fatal error " +exception.getMessage());
  }
  
  public String toString() {
    
    String errorTrace = "Fatal Errors:" + IOUtil.NL;
    int i = 1;
    for (SAXParseException e : fatalErrors) {
      errorTrace += "["+i+"] line "+e.getLineNumber()+" col "+e.getColumnNumber()+" "+
      e.getMessage()+IOUtil.NL;
      i++;
    }
    errorTrace += "Errors:" + IOUtil.NL;
    i = 1;
    for (SAXParseException e : errors) {
      errorTrace += "["+i+"] line "+e.getLineNumber()+" col "+e.getColumnNumber()+" "+
      e.getMessage()+IOUtil.NL;
      i++;
    }
    errorTrace += "Warnings:" + IOUtil.NL;
    i = 1;
    for (SAXParseException e : warnings) {
      errorTrace += "["+i+"] line "+e.getLineNumber()+" col "+e.getColumnNumber()+" "+
      e.getMessage()+IOUtil.NL;
      i++;
    }
    return errorTrace;
    
  }
  
  public boolean hasWarnings() {
    return warnings.size() > 0;
  }

  public boolean hasErrors() {
    return errors.size() > 0;
  }

  public boolean hasFatalErrors() {
    return fatalErrors.size() > 0;
  }

  /**
   * @return all warnings, errors, and fatal errors
   */
  public ArrayList<SAXParseException> getAllExceptions() {
    return exceptions;
  }
  
  public ArrayList<SAXParseException> getErrors() {
    return errors;
  }

  public ArrayList<SAXParseException> getFatalErrors() {
    return fatalErrors;
  }

  public ArrayList<SAXParseException> getWarnings() {
    return warnings;
  }
  
}
