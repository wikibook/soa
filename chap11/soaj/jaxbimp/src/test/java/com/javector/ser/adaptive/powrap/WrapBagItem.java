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

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Auther: Rohit Agarwal
 * Date: Jun 6, 2006
 * Time: 7:08:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class WrapBagItem {

    private Collection items;

    public WrapBagItem() {

    }

    public Collection getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }
}
