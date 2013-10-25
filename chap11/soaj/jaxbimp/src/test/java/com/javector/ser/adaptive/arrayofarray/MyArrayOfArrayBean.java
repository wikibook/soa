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
package com.javector.ser.adaptive.arrayofarray;

import com.javector.ser.adaptive.po.MyItem;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Kishore G
 * Date: Nov 24, 2005
 * Time: 9:48:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyArrayOfArrayBean {
    Integer[] integerArray;
    Float[] floatArray;
    Date[] dateArray;
    MyItem[] myItemArray;

    public Integer[] getIntegerArray() {
        return integerArray;
    }

    public void setIntegerArray(Integer[] integerArray) {
        this.integerArray = integerArray;
    }

    public Float[] getFloatArray() {
        return floatArray;
    }

    public void setFloatArray(Float[] floatArray) {
        this.floatArray = floatArray;
    }

    public Date[] getDateArray() {
        return dateArray;
    }

    public void setDateArray(Date[] dateArray) {
        this.dateArray = dateArray;
    }

    public MyItem[] getMyItemArray() {
        return myItemArray;
    }

    public void setMyItemArray(MyItem[] myItemArray) {
        this.myItemArray = myItemArray;
    }

}
