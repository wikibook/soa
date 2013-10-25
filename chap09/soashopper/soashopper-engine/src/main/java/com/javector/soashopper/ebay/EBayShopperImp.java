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
package com.javector.soashopper.ebay;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.apache.log4j.Logger;

import yahoo.prods.CatalogType;

import com.javector.soashopper.Category;
import com.javector.soashopper.Offer;
import com.javector.soashopper.Price;
import com.javector.soashopper.ShopperImp;
import com.javector.soashopper.ebay.RequesterCredentials;
import com.javector.soashopper.yahoo.YahooShopperImp;
import com.javector.util.TypeConverter;
import com.javector.util.Util;

import ebay.apis.eblbasecomponents.AbstractRequestType;
import ebay.apis.eblbasecomponents.DetailLevelCodeType;
import ebay.apis.eblbasecomponents.EBayAPIInterface;
import ebay.apis.eblbasecomponents.EBayAPIInterfaceService;
import ebay.apis.eblbasecomponents.GetItemRequestType;
import ebay.apis.eblbasecomponents.GetItemResponseType;
import ebay.apis.eblbasecomponents.GetSearchResultsRequestType;
import ebay.apis.eblbasecomponents.GetSearchResultsResponseType;
import ebay.apis.eblbasecomponents.ItemType;
import ebay.apis.eblbasecomponents.PriceRangeFilterType;
import ebay.apis.eblbasecomponents.SearchResultItemType;

/**
 * EBayShopperImp is a wrapper factory class around the JAX-WS compiled classes
 * that represent the eBay WSDL. It provides an intuitive interface for
 * accessing eBay that shields the business application from the complexity of
 * the underlying eBay WSDL. In addition, it provides for separation of concerns
 * in that EBayShopperImp provides a consistent interface to business
 * applications that does not need to be changed or recompiled each time the
 * EBay WSDL changes. Business applications should use this class for
 * integration with eBay rather than using the WSDL compiled classes generated
 * by JAX-WS.
 */
public class EBayShopperImp extends ShopperImp {

  private Logger log = Logger.getLogger(EBayShopperImp.class);

  private static final String ebayURL = "https://api.ebay.com/wsapi?";

  private static final String sandboxURL = "https://api.sandbox.ebay.com/wsapi?";

  private static final String localURL = "http://localhost:7070/Ebay";

  private static final String CALLNAME_TOKEN = "##callname##";

  private static final String WSDL_VERSION = "479";

  private static final String ERROR_LANGUAGE = "en_US";

  public static final String EBAY_PRODUCTION_SERVER = "eBay Production Server";

  public static final String EBAY_SANDBOX_SERVER = "eBay Sandbox Server";

  public static final String LOCAL_SERVER = "Local Server";

  public static final String SITE_ID_US = "0";

  private static final Hashtable<String, ItemType> ebayItemCache = new Hashtable<String, ItemType>();

  protected EBayAPIInterface port = null;

  protected String eBayURLTemplate = "";

  /**
   * Configure the EBayService. To use this service, you must register with eBay
   * Developer Relations. See {@link http://developer.ebay.com/}. When you
   * register, you will receive the appid, and other credentials necessary to
   * access eBay.
   * 
   * @param serverName
   *          Specifies the server to use. Should be one of
   *          EBAY_PRODUCTION_SERVER, EBAY_SANDBOX_SERVER, or LOCAL_SERVER
   * @param siteid
   *          Specified the site to use. Some examples are "0" for US, "3" for
   *          UK, etc. See
   *          {@link http://developer.ebay.com/DevZone/XML/docs/WSDL/xsd/1/}
   *          simpletype/SiteCodeType.htm for complete listing of siteid values.
   * @param appid
   *          A set of Sandbox Keys (AppId, DevId, CertId) is issued to you by
   *          Developer Relations when you first join the Developers Program.
   *          The application ID The application ID (AppId) is unique to each
   *          application created by the developer. Please pass the requesting
   *          application's AppId in the URL query string.
   * 
   */
  public EBayShopperImp(String serverName, String siteid, String appid) {

    // ! <example xn="EBayShopperImp_constructor">
    // ! <c>chap09</c><s>soapclient</s>
    EBayAPIInterfaceService svc = new EBayAPIInterfaceService();
    port = svc.getEBayAPI();
    BindingProvider bp = (BindingProvider) port;
    List<Handler> handlerChain = new ArrayList<Handler>();
    handlerChain.add(new RequesterCredentials());
    bp.getBinding().setHandlerChain(handlerChain);
    // ! </example>
    String endpointURL = "";

    if (serverName.equals(EBAY_PRODUCTION_SERVER)) {
      // ! <example xn="configuring_endpoint">
      // ! <c>chap09</c><s>soapclient</s>
      endpointURL = ebayURL + "callname=" + CALLNAME_TOKEN + "&siteid=0&appid="
          + appid + "&version=" + WSDL_VERSION + "&Routing=new";
      // ! </example>
    } else if (serverName.equals(EBAY_SANDBOX_SERVER)) {
      endpointURL = sandboxURL + "callname=" + CALLNAME_TOKEN
          + "&siteid=0&appid=" + appid + "&version=" + WSDL_VERSION
          + "&Routing=new";
    } else if (serverName.equals(LOCAL_SERVER)) {
      endpointURL = localURL;
    } else {
      throw new RuntimeException("Invalid Server Name: " + serverName);
    }
    eBayURLTemplate = endpointURL;

  }

  /**
   * Each method that invokes an eBay Call must first configure the call request
   * object and the port using this method. This method sets necessary
   * parameters (e.g., version and error language) on the request object and
   * also customizes the endpoint URL to include the call name parameter as
   * required by the eBay API.
   * 
   * @param art
   *          The request object to be configured.
   * @param callname
   *          The canonical call name as specified by the EBay API (see
   *          http://developer.ebay.com/DevZone/SOAP/docs/WebHelp/wwhelp/wwhimpl/js/html/wwhelp.htm)
   */
  // ! <example xn="EBayShopperImp_configureEBayRequestType">
  // ! <c>chap09</c><s>soapclient</s>
  private void configureEBayRequestType(AbstractRequestType art, String callname) {

    art.setVersion(WSDL_VERSION);
    art.setErrorLanguage(ERROR_LANGUAGE);
    String endpointURL = eBayURLTemplate.replace(CALLNAME_TOKEN, callname);
    ((BindingProvider) port).getRequestContext().put(
        BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);
  }

  // ! </example>

  public ItemType getItem(String itemId) {

    GetItemRequestType itemRequest = new GetItemRequestType();
    configureEBayRequestType(itemRequest, "GetItem");
    itemRequest.setItemID(itemId);
    List<DetailLevelCodeType> details = itemRequest.getDetailLevel();
    // details.add(DetailLevelCodeType.ITEM_RETURN_DESCRIPTION);
    // details.add(DetailLevelCodeType.ITEM_RETURN_ATTRIBUTES);
    details.add(DetailLevelCodeType.RETURN_ALL);
    GetItemResponseType itemResponse = null;
    try {
      itemResponse = port.getItem(itemRequest);
    } catch (Exception e) {
      throw new RuntimeException(
          "EBayAPIInterface.getItem() threw an Exception", e);
    }
    ItemType item = itemResponse.getItem();
    if (item != null) {
      EBayShopperImp.addItemToCache(itemId, item);
    }
    return item;

  }

  public List<ItemType> search(EBayCategory category, String keywords) {

    GetSearchResultsRequestType searchResultsRequest = new GetSearchResultsRequestType();
    configureEBayRequestType(searchResultsRequest, "GetSearchResults");
    searchResultsRequest.setCategoryID(category.getCategoryId());
    searchResultsRequest.setQuery(keywords);
    GetSearchResultsResponseType searchResultsResponse = null;
    try {
      searchResultsResponse = port.getSearchResults(searchResultsRequest);
    } catch (Exception e) {
      throw new RuntimeException(
          "EBayAPIInterface.getSearchResults() threw an Exception", e);
    }
    List<SearchResultItemType> searchResultList = searchResultsResponse
        .getSearchResultItemArray().getSearchResultItem();
    List<ItemType> retVal = new ArrayList<ItemType>();
    for (SearchResultItemType srit : searchResultList) {
      retVal.add(srit.getItem());
    }
    return retVal;

  }

  /*
   * eBay search is performed with the detail level set to ItemReturnAttributes.
   * 
   * @see com.javector.soashopper.SOAShopperServiceImpl#offerSearch(java.lang.String,
   *      com.javector.soashopper.Category, com.javector.soashopper.Price,
   *      com.javector.soashopper.Price)
   */
  // ! <example xn="EBayShopperImp">
  // ! <c>chap09</c><s>integration-layer</s>
  @Override
  public List<Offer> offerSearch(String keywords, Category category,
      Price lowprice, Price highprice) {

    TypeConverter tc = new TypeConverter();
    GetSearchResultsRequestType searchResultsRequest = new GetSearchResultsRequestType();
    configureEBayRequestType(searchResultsRequest, "GetSearchResults");
    List<DetailLevelCodeType> details = searchResultsRequest.getDetailLevel();
    details.add(DetailLevelCodeType.RETURN_ALL);
    if (category != null) {
      EBayCategory eBayCategory = tc.toEBayCategory(category);
      searchResultsRequest.setCategoryID(eBayCategory.getCategoryId());
    }
    searchResultsRequest.setQuery(keywords);
    if (lowprice != null || highprice != null) {
      PriceRangeFilterType prf = new PriceRangeFilterType();
      if (lowprice != null) {
        prf.setMinPrice(tc.toAmountType(lowprice));
      }
      if (highprice != null) {
        prf.setMaxPrice(tc.toAmountType(highprice));
      }
      searchResultsRequest.setPriceRangeFilter(prf);
    }
    GetSearchResultsResponseType searchResultsResponse = null;
    try {
      searchResultsResponse = port.getSearchResults(searchResultsRequest);
    } catch (Exception e) {
      throw new RuntimeException(
          "EBayAPIInterface.getSearchResults() threw an Exception", e);
    }
    List<SearchResultItemType> searchResultList = searchResultsResponse
        .getSearchResultItemArray().getSearchResultItem();
    List<Offer> retVal = new ArrayList<Offer>();
    for (SearchResultItemType srit : searchResultList) {
      retVal.add(new Offer(new EBayOfferImp(srit.getItem())));
    }
    return retVal;
  }

  // ! </example>

  public static ItemType getItemFromCache(String itemId) {
    return ebayItemCache.get(itemId);
  }

  public static void addItemToCache(String itemId, ItemType item) {
    ebayItemCache.put(itemId, item);
  }

}
