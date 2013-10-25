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
//
// "This program may be used, executed, copied, modified and distributed without 
// royalty for the purpose of developing, using, marketing, or distributing."
//
/**
 * Phone.java
 *
 */

package com.javector.ser.adaptive.po;

public class Phone implements java.io.Serializable {
    private int areaCode;
    private java.lang.String exchange;
    private java.lang.String number;

    public Phone() {
    }

    public Phone(int areaCode, java.lang.String exchange, java.lang.String number) {
        this.areaCode = areaCode;
        this.exchange = exchange;
        this.number = number;
    }

    public int getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(int areaCode) {
        this.areaCode = areaCode;
    }

    public java.lang.String getExchange() {
        return exchange;
    }

    public void setExchange(java.lang.String exchange) {
        this.exchange = exchange;
    }

    public java.lang.String getNumber() {
        return number;
    }

    public void setNumber(java.lang.String number) {
        this.number = number;
    }
    public boolean equals(Phone phoneNumber){
        return phoneNumber.getAreaCode() == this.getAreaCode() &&
                phoneNumber.getExchange().equals(this.getExchange()) &&
                phoneNumber.getNumber().equals(this.getNumber());                 
    }

}
