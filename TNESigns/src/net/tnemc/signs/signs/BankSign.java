package net.tnemc.signs.signs;

import com.github.tnerevival.serializable.SerializableLocation;

import java.util.UUID;

/**
 * Created by creatorfromhell on 3/13/2017.
 * All rights reserved.
 **/
public class BankSign extends TNESign {

  public BankSign(UUID owner, SerializableLocation location) {
    super(owner, location);
    setType(SignType.VAULT);
  }
}