package test;

import org.junit.Test;

/**
 * Created by Daniel on 10/11/2016.
 */
public class TNEAPITest {

  @Test
  public void enabled() {
    System.out.println(TNETest.instance().api() != null);
  }
}