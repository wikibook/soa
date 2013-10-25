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
import com.javector.soashopper.OfferImpFactory;

public class YahooOfferImpFactory extends OfferImpFactory {

  /*
   * Yahoo does not support offer IDs. So this method always throws an
   * UnsupportedOperationException. This is a runtime exception because the code
   * isn't expected to handle it. This method should never be invoked because
   * Yahoo Shopping will never be the source of any offer IDs.
   * 
   * @see com.javector.soashopper.OfferImplFactory#getOffer(java.lang.String)
   */
  @Override
  public OfferImp getOffer(String storeSpecificOfferId) {
    throw new UnsupportedOperationException(
        "Yahoo Shopping does not support Offer IDs.");
  }

}
