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

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.File;
import java.io.IOException;

/**
 * A set of public static utility method for managing common input/output tasks.
 * These methods are synchronized and thread-safe, although for that reason they
 * may hamper performance.
 * 
 * @author Mark Hansen (mark@javector.com)
 *
 */
public abstract class IOUtil {

  // ----------------------------------------------------------------- Constants

  public static final String NL =     // new line character
    System.getProperty("line.separator");
  public static final String FS =     // file separator
    System.getProperty("file.separator");
  public static final String USER_HOME = System.getProperty("user.home");

  // ------------------------------------------------------------ Public Methods
 
  public synchronized static String toString(File f) throws IOException {
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    copy(new FileInputStream(f), baos);
    return baos.toString();
    
  }
  
  /**
   * Reads the bytes from the input and writes them to the
   * output.
   *
   * @param input - the input stream
   * @param output - the output stream
   *
   */
  public synchronized static void copy
    (InputStream in, OutputStream out) throws IOException {

    byte[] b = new byte[1024];  // 1K buffer
    int result = in.read(b);
    while (result != -1) {
      out.write(b,0,result);
      result = in.read(b);
    }

  }

  /**
   * Returns the stack trace of an <code>Throwable</code>
   * as a <code>String</code>.
   *
   * @param e - an instance of <code>Throwable</code>
   *
   * @return the exception's stack trace as a
   *    <code>String</code>
   */
  public synchronized static String stackTrace(Throwable e) {

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintStream printOut = new PrintStream(out);
    e.printStackTrace(printOut);
    return out.toString();

  }
  
  public synchronized static String toString(InputStream xmlStream) 
  throws IOException {
    
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    copy(xmlStream, out);
    return out.toString();
    
  }

}	

