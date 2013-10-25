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
package com.javector.adaptive.framework.util;

import com.javector.adaptive.framework.interfaces.BaseSOAContext;
import com.javector.adaptive.framework.dto.DeserializeBindingRuleDTO;
import com.javector.soaj.SoajException;

import javax.xml.namespace.QName;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Apr 4, 2006
 * Time: 10:58:01 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseXPathEvaluator {


     public abstract Object applyXPath(DeserializeBindingRuleDTO bindingRuleDTO, Object sourceObject);

}
