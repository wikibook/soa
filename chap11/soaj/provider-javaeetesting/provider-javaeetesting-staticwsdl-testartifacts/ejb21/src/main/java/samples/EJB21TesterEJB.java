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

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.rmi.RemoteException;


public class EJB21TesterEJB implements SessionBean {

  public String test(String s, String t) {
    return s + " " + t;
  }

    public void ejbCreate () {
          System.out.println("ejbCreate called");
      }

  public Address getBillToFromEJB21(MyPurchaseOrder myPurchaseOrder){
    return  myPurchaseOrder.getBillTo();
  }

    public void setSessionContext(SessionContext sessionContext) throws EJBException, RemoteException {
    }

    public void ejbRemove() throws EJBException, RemoteException {
    }

    public void ejbActivate() throws EJBException, RemoteException {
    }

    public void ejbPassivate() throws EJBException, RemoteException {
    }
}
