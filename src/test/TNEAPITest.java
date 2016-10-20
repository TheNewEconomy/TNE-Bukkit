package test;

import com.github.tnerevival.core.api.TNEAPI;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by Daniel on 10/11/2016.
 */
public class TNEAPITest {

  @Test
  public void enable() {
    TNETest.instance.api = Mockito.mock(TNEAPI.class);
  }

  @Test
  public void enabled() {
    enable();
    System.out.println(TNETest.instance.api != null);
  }
}