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

import com.javector.soaj.deploy.invocation.SoajEJB21Method;

import java.util.ArrayList;

public class IntegrationTest_SoajEJB21Method {

     public String runTest() throws Exception {

    ArrayList<String> paramClass = new ArrayList<String>();
    paramClass.add("java.lang.String");
    paramClass.add("java.lang.String");
    SoajEJB21Method method =
//            String javaClass, String javaMethod,
//                         List<String> paramClass, String jndiName, String homeInterface,
//                         boolean local
      new SoajEJB21Method("samples.EJB21Tester", "test", paramClass,"samples.EJB21TesterHome","samples.EJB21TesterHome",true);
    ArrayList<Object> paramObject = new ArrayList<Object>();
    paramObject.add("Hello");
    paramObject.add("World");
    return (String) method.invoke(paramObject);


  }
}
