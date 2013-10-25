package samples;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestClient extends TestCase {
  public static Test suite() {
    return new TestSuite(TestClient.class);
  }

  protected void setup() throws Exception {}

  public void testClient() throws Exception {
    String host = System.getProperty("glassfish.host");	   
    String port = System.getProperty("glassfish.deploy.port");	 
    try { 
      String args[] = {host, port};
      Client.main(args); 
    } catch (Exception e) {
      e.printStackTrace();
      fail("Client failed to run to completion.");
    }

  }

}
