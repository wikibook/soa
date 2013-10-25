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
package test;

import java.util.ArrayList;

import com.javector.soaj.deploy.invocation.SoajEJB30Method;

public class IntegrationTest_SoajEJB30Method {
  
  public String runTest() throws Exception {
    
    ArrayList<String> paramClass = new ArrayList<String>();
    paramClass.add("java.lang.String");
    paramClass.add("java.lang.String");
    SoajEJB30Method method = 
      new SoajEJB30Method("samples.EJB30Tester", "test", paramClass, null);
    ArrayList<Object> paramObject = new ArrayList<Object>();
    paramObject.add("Hello");
    paramObject.add("World");
    return (String) method.invoke(paramObject);
  
    
  }
  
}
