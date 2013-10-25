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
package com.javector.ser.convertor;

import com.javector.adaptive.framework.interfaces.SimpleTypeConvertor;

import java.util.Date;
import java.math.BigInteger;

/**
 * Created by IntelliJ IDEA.
 * Auther: Rohit Agarwal
 * Date: Jun 3, 2006
 * Time: 8:17:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomConvertor implements SimpleTypeConvertor<Date, Integer>  {


//    /**
//     * Type is the required class type which denotes the type of value that needs to be populated in
//     * xmlName of target XML Object
//     * type parameter can be used by the user to make sure that convertion from java Value to the value to be
//     * populated is of proper type
//     * @param value
//     * @param type
//     * @return
//     */

  public Integer serialize(Date value) {
    return new Integer(String.valueOf(value.getDate()));
  }

  public Date deserialize(Integer value) {
    return new Date();
  }




}
