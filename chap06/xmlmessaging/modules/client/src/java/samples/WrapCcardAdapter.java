/*
* Copyright 2006 Javector Software LLC
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

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class WrapCcardAdapter extends XmlAdapter<MyCreditCard.OrderCcard, MyCreditCard> {

  public MyCreditCard unmarshal(MyCreditCard.OrderCcard wrapper) throws Exception {
    return wrapper.ccard;
  }

  public MyCreditCard.OrderCcard marshal(MyCreditCard ccard) throws Exception {
    MyCreditCard.OrderCcard wrapper = new MyCreditCard.OrderCcard();
    wrapper.ccard = ccard;
    return wrapper;
  }

}
