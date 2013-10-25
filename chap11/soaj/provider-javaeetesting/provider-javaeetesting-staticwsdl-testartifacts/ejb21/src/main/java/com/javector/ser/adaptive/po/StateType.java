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

package com.javector.ser.adaptive.po;

public class StateType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    public StateType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_, this);
    };

    public static final java.lang.String _TX = "TX";
    public static final java.lang.String _IN = "IN";
    public static final java.lang.String _OH = "OH";
    public static final StateType TX = new StateType(_TX);
    public static final StateType IN = new StateType(_IN);
    public static final StateType OH = new StateType(_OH);

    public java.lang.String getValue() {
        return _value_;
    }

    public void setValue(String value) {
        _value_ = value;
    }

    public static StateType fromValue(java.lang.String value)
            throws java.lang.IllegalStateException {
        StateType enum1 = (StateType)
                _table_.get(value);
        if (enum1 == null) throw new java.lang.IllegalStateException();
        return enum1;
    }

    public static StateType fromString(String value)
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
