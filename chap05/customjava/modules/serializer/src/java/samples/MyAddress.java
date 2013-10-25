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

//! <example xn="MyAddress">
//! <c>chap05</c><s>customjava</s>
public class MyAddress {
  
  protected String name;
  protected String street;
  protected String city;
  protected String state;
  protected String zip;
  protected String phone;
  
  public MyAddress(String name, String street, String city, String state,
      String zip, String phone) {

    this.name = name;
    this.street = street;
    this.city = city;
    this.state = state;
    this.zip = zip;
    this.phone = phone;
    
  }
  
}
//! </example>
