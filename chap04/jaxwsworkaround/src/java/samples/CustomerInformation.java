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

import javax.annotation.Resource;
import javax.jws.WebService;

import com.example.corp.AddressType;
import com.example.css.CustomerHistoryEntriesType;
import com.example.css.custinfo.CustomerInformationPort;

//! <example xn="INTERFACE_SPLITTER_IMPLEMENTATION">
//! <c>chap04</c><s>jaxwsworkaround</s>
@WebService(
    endpointInterface="com.example.css.custinfo.CustomerInformationPort",
    serviceName="CustomerInformationService")
public class CustomerInformation implements CustomerInformationPort {
  
  @Resource
  private Customer cust;
  
  @Resource
  private CustomerHistory custHist;
  
  public AddressType getAddress(String custId) {
    Address addr = cust.getAddress(custId);
    AddressType addrType = convertAddressToAddressType(addr);
    return addrType;
  }
  
  public CustomerHistoryEntriesType getCustomerHistory(String custId) {
    History hist = custHist.getCustomerHistory(custId);
    CustomerHistoryEntriesType entries = 
      convertHistoryToCustomerHistoryEntriesType(hist);
    return entries;
  }
  //! </example>
    
  private static AddressType convertAddressToAddressType(Address addr) {
    return null;
  }
  
  private static CustomerHistoryEntriesType 
  convertHistoryToCustomerHistoryEntriesType(History hist) {
    return null;
  }
  
  
}
