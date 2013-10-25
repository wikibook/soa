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

import java.net.URL;

public class Picture {

  private URL url;
  private Integer pixelWidth;
  private Integer pixelHeight;

  public Picture(URL url, Integer pixelWidth, Integer pixelHeight) {

    if (url == null) {
      throw new IllegalArgumentException("Picture URL cannot be null.");
    }
    this.url = url;
    this.pixelWidth = pixelWidth;
    this.pixelHeight = pixelHeight;
  }

  public Integer getPixelHeight() {
    return pixelHeight;
  }

  public Integer getPixelWidth() {
    return pixelWidth;
  }

  public URL getUrl() {
    return url;
  }

  public void setPixelHeight(Integer pixelHeight) {
    this.pixelHeight = pixelHeight;
  }

  public void setPixelWidth(Integer pixelWidth) {
    this.pixelWidth = pixelWidth;
  }

  public void setUrl(URL url) {
    this.url = url;
  }

}
