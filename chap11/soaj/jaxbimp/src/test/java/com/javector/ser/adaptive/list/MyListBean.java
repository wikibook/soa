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
package com.javector.ser.adaptive.list;

import com.javector.ser.adaptive.po.MyItem;

import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: Kishore G
 * Date: Nov 23, 2005
 * Time: 1:17:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class MyListBean {
  private double[] num;
  private String[] city;
  private Calendar date;
  private MyItem item;
  private int[] someIntegers;

    public double[] getNum() {
        return num;
    }

    public void setNum(double[] num) {
        this.num = num;
    }

    public String[] getCity() {
        return city;
    }

    public void setCity(String[] city) {
        this.city = city;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public MyItem getItem() {
        return item;
    }

    public void setItem(MyItem item) {
        this.item = item;
    }

    public int[] getSomeIntegers() {
        return someIntegers;
    }

    public void setSomeIntegers(int[] someIntegers) {
        this.someIntegers = someIntegers;
    }
}
