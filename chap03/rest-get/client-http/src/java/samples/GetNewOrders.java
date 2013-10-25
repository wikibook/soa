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

import java.io.InputStream;
import java.net.URL;
import java.net.HttpURLConnection;

public class GetNewOrders {

  //! <example xn="GetNewOrders_Client_HTTP">
  //! <c>chap03</c><s>rest-get</s>
  public static void main(String[] args) throws Exception {

    if (args.length != 1) {
      System.err.println
      ("Usage: java GetNewOrders <Web Service URL>");
      System.exit(1);
    } 
    // Create the HTTP connection to the URL
    URL url = new URL(args[0]);
    HttpURLConnection con = 
      (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");
    con.connect();
    // write the XML from the input stream to standard out
    InputStream in = con.getInputStream();
    byte[] b = new byte[1024];  // 1K buffer
    int result = in.read(b);
    while (result != -1) {
      System.out.write(b,0,result);
      result =in.read(b);
    }
    in.close();
    con.disconnect();
  }
  //! </example>
}
