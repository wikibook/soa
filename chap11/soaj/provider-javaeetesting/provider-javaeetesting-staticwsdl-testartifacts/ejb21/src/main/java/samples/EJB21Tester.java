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
package samples;

import com.javector.ser.adaptive.po.Address;
import com.javector.ser.adaptive.po.MyPurchaseOrder;

import javax.ejb.EJBObject;
import java.rmi.Remote;


public interface EJB21Tester extends EJBObject, Remote {

  public String test(String s1, String s2) throws java.rmi.RemoteException;
  public Address getBillToFromEJB21(MyPurchaseOrder myPurchaseOrder) throws java.rmi.RemoteException;
}
