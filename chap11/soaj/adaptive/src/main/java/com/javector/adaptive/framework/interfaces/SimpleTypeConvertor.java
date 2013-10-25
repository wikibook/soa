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
package com.javector.adaptive.framework.interfaces;

/**
 * Created by IntelliJ IDEA.
 * Auther: Rohit Agarwal
 * Date: Jun 3, 2006
 * Time: 1:28:04 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Implement this interface to create a custom simple type serializer / 
 * deserialier.  InputType is a Java Class that the Adaptive Serializer 
 * Framework (ASF) does not know how to serialize to a simple type.  
 * OutputType is a Class that ASF knows how to serialize to a simple type.
 * So, for example, to convert Date to xs:Stringk you would implement an
 * interface like:
 *   SimpleTypeConverter<Date, String>.  
 * Since ASF knows how to convert a String to an xs:String, an instance of
 * such a SimpleTypeConverter can be use to serializer a Date to an xs:string
 */



/**
 * convrertors may not work properly in case of scripts defined for the rule
 * If convertor and scripts both are defined for a rule script is given preference over convertor
 * and the corresponding serialize method will never be invoked. can be changed in future if needed.
 * to invoke serialize before script method
 */

public interface  SimpleTypeConvertor<InputType, OutputType> {

  public OutputType serialize(InputType value);
  
  public InputType deserialize(OutputType value);
  
}
