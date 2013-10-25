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
package com.javector.soashopper.yahoo;

import com.javector.soashopper.OfferImp;
import com.javector.soashopper.Store;

/**
 * Yahoo Shopping requires 2 classes to implement OfferImpl. This is an
 * intermediate the handles the method implementations that are common between
 * the two.
 * 
 */
public abstract class YahooOfferImp extends OfferImp {

	/*
	 * Always returns <code>null</code> since Yahoo Shopping Offers do not
	 * have unique identifiers other than the URL.
	 * 
	 * @see com.javector.soashopper.ItemImpl#getSourceId()
	 */
	@Override
	public String getSourceSpecificOfferId() {
		return null;
	}

	@Override
	public Store getSource() {
		return Store.YAHOO;
	}

}
