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

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.HttpURLConnection;

public class PostCustomerHistory {

  //! <example xn="PostCustomerHistory_Client_HTTP">
  //! <c>chap03</c><s>rest-post</s>
  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.err.println
	("Usage: java PostCustomerHistory <XML file name> " 
	 + "<Web Service URL>");
      System.exit(1);
    } 
    FileInputStream in = new FileInputStream(args[0]);
    URL url = new URL(args[1]);
    HttpURLConnection con = 
      (HttpURLConnection) url.openConnection();
    con.setDoOutput(true);
    con.setRequestMethod("POST");
    con.connect();
    OutputStream out = con.getOutputStream();
    // write the XML doc from file to the HTTP connection
    byte[] b = new byte[1024];  // 1K buffer
    int result = in.read(b);
    while (result != -1) {
      out.write(b,0,result);
      result = in.read(b);
    }
    out.close();
    in.close();
    // write HTTP response to console
    System.out.println(con.getResponseCode() + 
		       " " + con.getResponseMessage());
  }
  //! </example>
}
