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

import javax.ejb.Stateless;

//! <example xn="EJB_JAR_GOODBYE">
//! <c>chap08</c><s>descriptor</s>
@Stateless
public class GoodbyeEJB implements Goodbye {
  
  public String sayGoodbye(String s) {
    return "Goodbye: " + s;
  }

}
//! </example>
