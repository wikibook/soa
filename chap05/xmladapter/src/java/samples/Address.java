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

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

//! <example xn="Address">
//! <c>chap05</c><s>xmladapter</s>
@XmlJavaTypeAdapter(AddressAdapter.class)
public class Address {
  private int streetNum;
  private String streetName;
  private String city;
  private State state;
  private int zip;
  private Phone phoneNumber;
//! </example>
  
  public Address() {
  }
  
  public Address(int streetNum, String streetName, 
      String city, State state, int zip, Phone phoneNumber) {
    this.streetNum = streetNum;
    this.streetName = streetName;
    this.city = city;
    this.state = state;
    this.zip = zip;
    this.phoneNumber = phoneNumber;
  }
  
  public int getStreetNum() {
    return streetNum;
  }
  
  public void setStreetNum(int streetNum) {
    this.streetNum = streetNum;
  }
  
  public String getStreetName() {
    return streetName;
  }
  
  public void setStreetName(String streetName) {
    this.streetName = streetName;
  }
  
  public String getCity() {
    return city;
  }
  
  public void setCity(String city) {
    this.city = city;
  }
  
  public State getState() {
    return state;
  }
  
  public void setState(State state) {
    this.state = state;
  }
  
  public int getZip() {
    return zip;
  }
  
  public void setZip(int zip) {
    this.zip = zip;
  }
  
  public Phone getPhoneNumber() {
    return phoneNumber;
  }
  
  public void setPhoneNumber(Phone phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
  
}
