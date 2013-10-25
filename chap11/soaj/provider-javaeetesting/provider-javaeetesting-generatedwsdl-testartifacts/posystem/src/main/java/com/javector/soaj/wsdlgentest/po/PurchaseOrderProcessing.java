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

public class PurchaseOrderProcessing {
  static PODatabase database = PODatabase.getInstance();

  static {
    populateDataBase();
  }

  private static void populateDataBase() {
    PurchaseOrder purchaseOrder = new PurchaseOrder();
    purchaseOrder.setPonum("posystem");
    Address address = new Address();
    address.setStreetName("road");
    purchaseOrder.setBillTo(address);
    database.addPurchaseOrder(purchaseOrder);
  }

  public static void updateAddress(Address addr, String poNum) {
    PurchaseOrder po = getPOfromDatabase(poNum);
    if (po == null) {
      throw new PurchaseOrderRuntimeException(
          "No such purchase order exits for poNum = " + po);
    }
    po.setBillTo(addr);
  }

  public static PurchaseOrder getPOfromDatabase(String poNum) {
    return database.getPO(poNum);
  }
}
