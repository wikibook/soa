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
package ebay.apis.eblbasecomponents;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestEBayAPIInterfaceService extends TestCase {

  public static Test suite() {
    return new TestSuite(TestEBayAPIInterfaceService.class);
  }

  public TestEBayAPIInterfaceService(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
  }

  public void testConstructor() {
    new EBayAPIInterfaceService();
    assertTrue(true);
  }

}
