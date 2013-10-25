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
package com.javector.ser.adaptive.po;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * Auther: Rohit Agarwal
 * Date: May 30, 2006
 * Time: 9:49:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyOwnPurchaseOrder {


    private Address billTo = new Address();
    private List items = new ArrayList();
    private VendorTest vendorTest;

    public VendorTest getVendor() {
        return vendorTest;
    }

    public void setVendor(VendorTest vendorTest) {
        this.vendorTest = vendorTest;
    }

    public void setBillTo(Address billTo) {
        this.billTo = billTo;
    }

    public void setItems(MyOwnItem[] items) {

        this.items.addAll(Arrays.asList(items));
    }

    public Address getBillTo() {
        return billTo;
    }

    public List getItems() {
        return items;
    }

    public void addItem(MyOwnItem item) {
        items.add(item);
    }


}

