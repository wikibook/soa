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
package com.javector.soashopper.endpoint.rest;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;
import javax.xml.ws.BindingType;
import javax.xml.ws.Provider;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.http.HTTPException;

import com.example.retail.OfferList;
import com.example.retail.OfferType;
import com.javector.soashopper.services.ShopperService;

//! <example xn="ShopperServiceRESTImp">
//! <c>chap09</c><s>rest</s>
@WebServiceProvider
@BindingType(HTTPBinding.HTTP_BINDING)
public class ShopperServiceRESTImp implements ShopperServiceREST,
    Provider<Source> {

  @Resource
  WebServiceContext wsContext;

  JAXBContext jaxbContext;

  public ShopperServiceRESTImp() {
    try {
      jaxbContext = JAXBContext.newInstance(OfferList.class);
    } catch (JAXBException je) {
      throw new HTTPException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  //! </example>

  //! <example xn="ShopperServiceREST_offerSearch">
  //! <c>chap09</c><s>rest</s>
  public OfferList offerSearch(String keywords, String category,
      String currencyId, Double lowprice, Double highprice) {

    ShopperService shopperService = new ShopperService();
    List<OfferType> offers = shopperService.offerSearch(keywords,
        category, currencyId, lowprice, highprice);
    OfferList offerList = new OfferList();
    List<OfferType> offerListOffers = offerList.getOffer();
    offerListOffers.addAll(offers);
    return offerList;

  }

  //! </example>

  //! <example xn="ShopperServiceREST_invoke">
  //! <c>chap09</c><s>rest</s>
  public Source invoke(Source source) {

    MessageContext msgContext = wsContext.getMessageContext();
    String httpMethod = (String) msgContext
        .get(MessageContext.HTTP_REQUEST_METHOD);
    Map<String, String[]> params = null;
    if (httpMethod.equals("GET") || httpMethod.equals("POST")) {
      HttpServletRequest httpReq = (HttpServletRequest) msgContext
          .get(MessageContext.SERVLET_REQUEST);
      params = httpReq.getParameterMap();
    } else {
      throw new HTTPException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
    String keywords = getParam(params, "keywords");
    if (keywords == null) {
      throw new HTTPException(HttpServletResponse.SC_NOT_FOUND);
    }
    String category = getParam(params, "category");
    String currencyId = getParam(params, "currencyId");
    String lowpriceStr = getParam(params, "lowprice");
    String highpriceStr = getParam(params, "highprice");
    Double lowprice = lowpriceStr == null ? null : Double.valueOf(lowpriceStr);
    Double highprice = highpriceStr == null ? null : Double
        .valueOf(highpriceStr);
    OfferList offerList = offerSearch(keywords, category, currencyId, lowprice,
        highprice);
    if (offerList == null) {
      return null;
    }
    try {
      return new JAXBSource(jaxbContext, offerList);
    } catch (JAXBException e) {
      throw new HTTPException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

  }

  //! </example>

  /**
   * @param params
   * @param key
   * @return the first value in the array, or if array is empty returns null.
   */
  private String getParam(Map<String, String[]> params, String key) {

    String[] values = params.get(key);
    if (values == null || values.length == 0) {
      return null;
    }
    return values[0];
  }

}
