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
package com.javector.soashopper;

import java.net.URL;
import java.util.List;

public abstract class ProductImp {

  public abstract String getSourceSpecificId();

  public abstract Store getSource();

  public abstract Picture getThumbnail();

  public abstract String getSummary();

  public abstract URL getUrl();

  public abstract List<Offer> getSourceSpecificOffers();

  
}
