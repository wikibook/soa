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

public class MyFooAdapter extends XmlAdapter<Foo, MyFoo> {

  public MyFoo unmarshal(Foo arg0) throws Exception {
    MyFoo myFoo = new MyFoo();
    myFoo.myFooString = arg0.fooString;
    return myFoo;
  }
  
  public Foo marshal(MyFoo arg0) throws Exception {
    Foo foo = new Foo();
    foo.fooString = arg0.myFooString;
    return foo;
  }

}
