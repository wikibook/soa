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
package com.javector.adaptive.dummy;

import com.javector.ser.adaptive.po.Address;

/**
 * Created by IntelliJ IDEA.
 * User: javector
 * Date: May 27, 2006
 * Time: 1:20:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class DummyHandler {

public Address invokeService(Address address){
    System.out.println("address = " + address.getZip());
    address.setZip(1222);
    return address;
}

}
