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
package com.javector.util;

import com.javector.soashopper.Offer;
import com.javector.soashopper.Price;

public class Util {

  public static final String NL = System.getProperty("line.separator");

  public boolean validZip(String zip) {
    if (zip == null)
      return false;
    // insert zip code validation
    return true;
  }

  /**
   * Rounds down a value to the nearest decimal out to the specified number of
   * places.
   * 
   * @param x
   *          the value to be rounded down
   * @param places
   *          the decimal places to round down to
   * @return a decimal representation of the result with the specified number of
   *         places after the decimal point
   */
  public String floor(Double x, int places) {
    return roundNumber(Operation.FLOOR, x, places);
  }

  public String ceiling(Double x, int places) {
    return roundNumber(Operation.CEILING, x, places);
  }

  public String toString(Offer o) {
    if (o == null) {
      return null;
    }
    String store = "Store: " + o.getSource().name();
    String id = "ID : " + o.getSourceSpecificOfferId();
    String merchant = "Merchant: " + o.getMerchantName();
    String summary = "Summary: " + o.getSummary();
    String price = "Price: " + toString(o.getPrice());
    return store + NL + merchant + NL + summary + NL + price + NL + id;
  }

  public String toString(Price p) {

    String price = null;
    if (p != null) {
      price = p.getCurrencyID().getCurrencyCode() + " " + floor(p.getValue(), 2);
    }
    return price;
  }

  private String roundNumber(Operation op, Double x, int places) {
    if (x == null) {
      throw new IllegalArgumentException("Cannot round down null.");
    }
    if (places < 0) {
      throw new IllegalArgumentException(
          "places parameter must be greater than or equal to 0");
    }
    double xx = x * (long) Math.pow(10, places);
    long roundedxx;
    switch (op) {
    case FLOOR:
      roundedxx = (long) Math.floor(xx);
      break;
    case CEILING:
      roundedxx = (long) Math.ceil(xx);
      break;
    default:
      throw new RuntimeException("Operation: " + op.toString()
          + "is not implemented.");
    }
    String roundedxxString = Long.toString(roundedxx);
    int len = roundedxxString.length();
    String leftOfDecimalPoint = "0";
    if (len > places) {
      leftOfDecimalPoint = roundedxxString.substring(0, len - places);
    }
    if (places == 0) {
      return leftOfDecimalPoint;
    }
    String rightOfDecimalPoint = "";
    if (len >= places) {
      rightOfDecimalPoint = roundedxxString.substring(len - places, len);
    } else {
      rightOfDecimalPoint = roundedxxString;
    }
    while (rightOfDecimalPoint.length() < places) {
      rightOfDecimalPoint = "0" + rightOfDecimalPoint;
    }
    return leftOfDecimalPoint + "." + rightOfDecimalPoint;
  }

  private enum Operation {
    CEILING, FLOOR;
  }

}
