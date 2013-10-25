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
package com.javector.soaj.wsdlgentest.po;

import java.util.ArrayList;
import java.util.Arrays;

public class PODatabase {

    private static int initalSize = 10;
    private static ArrayList<PurchaseOrder>  pos = new ArrayList<PurchaseOrder>(initalSize);
    private static PODatabase database = new PODatabase();

    public static PODatabase getInstance() {
        return database;
    }

    public ArrayList<PurchaseOrder> getPOs() {
        return pos;
    }

    public void setPos(PurchaseOrder[] pos) {
        this.pos.addAll(Arrays.asList(pos));
    }

    public void addPurchaseOrder(PurchaseOrder  purchaseOrder) {
        this.pos.add(purchaseOrder);
    }

    public PurchaseOrder getPO(String string) {
        for (PurchaseOrder po : getPOs()) {
            if ( po.getPonum().equals(string) ) {
                return po;
            }
        }
        return null;
    }

}
