package com.github.test;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.api.TNEAPI;
import org.junit.Test;

/**
 * Created by Daniel on 10/11/2016.
 */
public class TNEAPITest {

  TNEAPI api;

  @Test
  public boolean apiEnabledTest() {
    api = TNE.instance.api;

    return api != null;
  }
}