package test;

import com.github.tnerevival.TNE;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.mockito.Mockito;

/**
 * Created by Daniel on 10/11/2016.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
  TNEAPITest.class
})
public class TNETests {

  TNE plugin;

  @Test
  public Boolean enable() {
    Mockito.mock(TNE.class);
    return plugin.api != null;
  }
}
