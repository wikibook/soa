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

/**
 * Created by IntelliJ IDEA.
 * Auther: Rohit Agarwal
 * Date: Jun 6, 2006
 * Time: 7:02:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class WrapState implements  java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    public WrapState(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_, this);
    };

    public static final java.lang.String _TX = "TX";
    public static final java.lang.String _IN = "IN";
    public static final java.lang.String _OH = "OH";
    public static final WrapState TX = new WrapState(_TX);
    public static final WrapState IN = new WrapState(_IN);
    public static final WrapState OH = new WrapState(_OH);

    public java.lang.String getValue() {
        return _value_;
    }

    public void setValue(String value) {
        _value_ = value;
    }

    public static WrapState fromValue(java.lang.String value)
            throws java.lang.IllegalStateException {
        WrapState enum1 = (WrapState)
                _table_.get(value);
        if (enum1 == null) throw new java.lang.IllegalStateException();
        return enum1;
    }

    public static WrapState fromString(String value)
            throws java.lang.IllegalStateException {
        return fromValue(value);
    }

    public boolean equals(Object obj) {
        return (obj == this);
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public String toString() {
        return _value_;
    }
}
