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
package com.javector.adaptive.framework.interfaces;

import javax.xml.transform.Source;

public interface AdaptiveContext {
  
  public Object deserialize(Source source, XMLMapping mapping);
  public Object serialize(Object javaObj, XMLMapping tm);
  public Object deserialize(Object xml, XMLMapping tm);
  public Source serializeAsSource(Object javaObj, XMLMapping mapping);
  
}
