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
package com.javector.soaj.util;

import java.io.InputStream;
import java.net.URL;


/**
 * Utility class for discovering resources trying a variety of class loaders. 
 * If the provided class is not null, uses its loader to discover the resource.
 * If not found (or if provided class is null), uses the current thread's
 * context classloader, and lastly tries the system class loader.
 */
public class ResourceFinder {
  
  /**
   * @param relativePath path of the resource
   * @param clazz class from which to get class loader for finding resource
   * @return null if the resource is not found
   */
  public static InputStream getResourceAsStream(String relativePath, Class clazz) {
    
    InputStream inputStream = null;
    if( clazz != null){
      inputStream = clazz.getClassLoader().getResourceAsStream(relativePath);
    }
    if( inputStream == null){
      inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(relativePath);
    }
    if( inputStream == null){
      inputStream = ClassLoader.getSystemResourceAsStream(relativePath);
    }
    return inputStream;
    
  }
  
  /**
   * Convenience method that invokes {@link #getResourceAsStream(String,Class)} with class as null.
   * @param relativePath
   * @return null if the resource is not found
   */
  public static InputStream getResourceAsStream(String relativePath) {
    return getResourceAsStream(relativePath, null);
  }
  
  /**
   * @param relativePath path of the resource
   * @param clazz class from which to get class loader for finding resource
   * @return null if the resource is not found
   */
  public static URL getResourceUrl(String relativePath, Class clazz) {
    
    URL resource = null;
    if( clazz != null){
      resource = clazz.getClassLoader().getResource(relativePath);
    }
    if( resource == null){
      resource = Thread.currentThread().getContextClassLoader().getResource(relativePath);
    }
    if( resource == null){
      resource = ClassLoader.getSystemResource(relativePath);
    }
    return resource;
    
  }
  
  /**
   * Convenience method that invokes {@link #getResourceUrl(String,Class)} with class as null.
   * @param relativePath
   * @return null if the resource is not found
   */
  public static URL getResourceUrl(String relativePath) {
    return getResourceUrl(relativePath, null);
  }
  
}
