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
package com.javector.soashopper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class provides credentials to the various online shopping sources as
 * required. All credentials are stored in the file "soashopper.properties"
 * which must be included in the runtime classpath.
 * <p>
 * <b>eBay</b>
 * <p>
 * Before you can communicate with the eBay production servers, you must
 * hardwire four requester credential codes into your online application:
 * <ul>
 * <li>An application ID key. (ebay.appid)</li>
 * <li>A developer ID key. (ebay.devid)</li>
 * <li>A certificate ID key. (ebay.certid)</li>
 * <li>An authentication token.(ebay.authtoken)</li>
 * </ul>
 * Note that the authentication token is a single-user token. The eBay server
 * assigns all four of these codes when you first sign up for the developer
 * program. To register, see http://developer.ebay.com/. Once you have these
 * codes, you can perform live calls against the eBay server. You configure this
 * application by placing the values you receive from eBay into a file named
 * "soashopper.properties" on your classpath. If you are using the test server
 * that this article demonstrated earlier, that server ignores these values. For
 * more information about eBay authentication see
 * http://developer.ebay.com/DevZone/SOAP/docs/WebHelp/wwhelp/wwhimpl/js/html/wwhelp.htm
 * <p>
 * <b>Yahoo Shopping</b>
 * <p>
 * To use the Yahoo Shopping service, you must register with Yahoo and get an
 * access key id (amazon.accesskeyid). See
 * http://api.search.yahoo.com/webservices/register_application. *
 * <p>
 * <b>Amazon</b>
 * <p>
 * To use the Amazon E-Commerce service, you must register with Amazon and get
 * an application id (amazon.accesskeyid). See
 * https://aws-portal.amazon.com/gp/aws/developer/registration/index.html
 */
public abstract class ShopperCredentials {

  private static String ebay_devid;

  private static String ebay_appid;

  private static String ebay_certid;

  private static String ebay_authtoken;

  private static String yahoo_appid;

  private static String amazon_accesskeyid;

  static {

    InputStream src = Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("soashopper.properties");
    if (src == null) {
      System.out.println("Error reading soashopper.properties.");
      System.exit(1);
    }
    Properties props = new Properties();
    try {
      props.load(src);
    } catch (IOException e) {
      System.out.println("Failed to load soashopper.properties.");
      System.exit(1);
    }
    ebay_devid = props.getProperty("ebay.devid");
    ebay_appid = props.getProperty("ebay.appid");
    ebay_certid = props.getProperty("ebay.certid");
    ebay_authtoken = props.getProperty("ebay.authtoken");
    yahoo_appid = props.getProperty("yahoo.appid");
    amazon_accesskeyid = props.getProperty("amazon.accesskeyid");
    if (ebay_devid == null || ebay_appid == null || ebay_certid == null
        || ebay_authtoken == null || yahoo_appid == null
        || amazon_accesskeyid == null) {
      System.out.println("Failed to load soashopper.properties.");
      System.exit(1);
    }

  }

  public static String getEBayAppID() {

    return ebay_appid;
  }

  public static String getEBayAuthToken() {

    return ebay_authtoken;
  }

  public static String getEBayCertID() {

    return ebay_certid;
  }

  public static String getEBayDevID() {

    return ebay_devid;
  }

  public static String getYahooAppID() {

    return yahoo_appid;
  }
  
  public static String getAmazonAccessKeyID() {

    return amazon_accesskeyid;
  }
}