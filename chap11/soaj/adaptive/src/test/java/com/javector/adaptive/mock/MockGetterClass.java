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
package com.javector.adaptive.mock;

import java.util.Collection;


public class MockGetterClass {
	
	public MockGetterClass(){
		this.name="500";
		this.mockMyOwnItem="500";
	}
	
	String name;
	String mockMyOwnItem;
	String Array[];
	Collection collection;
	
    public Collection getCollection() {
		return collection;
	}

	public void setCollection(Collection collection) {
		this.collection = collection;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getArray() {
		return Array;
	}

	public void setArray(String[] Array) {
		this.Array = Array;
	}

	public String getName() {
        return "500";
    }

    public String  getMockMyOwnItem() {
    	return "500";
    }

	public void setMockMyOwnItem(String mockMyOwnItem) {
		this.mockMyOwnItem = mockMyOwnItem;
	}

}
