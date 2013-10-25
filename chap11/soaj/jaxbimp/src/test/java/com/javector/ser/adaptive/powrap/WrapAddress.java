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
package com.javector.ser.adaptive.powrap;

import com.javector.ser.adaptive.po.StateType;
import com.javector.ser.adaptive.po.Phone;

/**
 * Created by IntelliJ IDEA.
 * Auther: Rohit Agarwal
 * Date: Jun 6, 2006
 * Time: 7:01:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class WrapAddress implements java.io.Serializable {
    private int streetNum;
    private java.lang.String streetName;
    private java.lang.String city;
    private WrapState state;
    private int zip;
    private WrapPhone phoneNumber;

    public WrapAddress() {
    }

    public WrapAddress(int streetNum, java.lang.String streetName, java.lang.String city, WrapState state, int zip, WrapPhone phoneNumber) {
        this.streetNum = streetNum;
        this.streetName = streetName;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
    }

    public int getStreetNum() {
        return streetNum;
    }

    public void setStreetNum(int streetNum) {
        this.streetNum = streetNum;
    }

    public java.lang.String getStreetName() {
        return streetName;
    }

    public void setStreetName(java.lang.String streetName) {
        this.streetName = streetName;
    }

    public java.lang.String getCity() {
        return city;
    }

    public void setCity(java.lang.String city) {
        this.city = city;
    }

    public WrapState getState() {
        return state;
    }

    public void setState(WrapState state) {
        this.state = state;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public WrapPhone getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(WrapPhone phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
