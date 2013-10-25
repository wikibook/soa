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
package com.javector.soaj.deploy.invocation;

import com.javector.soaj.SoajRuntimeException;

public class SoajInvocationRuntimeException extends SoajRuntimeException {

  public SoajInvocationRuntimeException() {
    super();
  }

  public SoajInvocationRuntimeException(String reason) {
    super(reason);
  }
  
  public SoajInvocationRuntimeException(String[] reasons) {
    super(reasons);
  }
  
  public SoajInvocationRuntimeException(String reason, Throwable cause) {
    super (reason, cause);
  }
  
  public SoajInvocationRuntimeException(String[] reasons, Throwable cause) {
    super(reasons, cause);
  }
 
  public SoajInvocationRuntimeException(Throwable cause) {
  super(cause);
  }

}
