package net.tnemc.core.message;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 12/17/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class BungeeMessageWrapper {

  private byte[] data;
  private DataInputStream in;

  public BungeeMessageWrapper(byte[] data) {
    this.data = data;
    open();
  }

  public void open() {
    this.in = new DataInputStream(new ByteArrayInputStream(data));
  }

  public void close() {
    try {
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String readUTF() throws IOException {
    return in.readUTF();
  }

  public Optional<UUID> readUUID() throws IOException {
    final String str = readUTF();

    try {
      return Optional.of(UUID.fromString(str));
    } catch (Exception ignore) {
      return Optional.empty();
    }
  }

  public Optional<BigDecimal> readBigDecimal() throws IOException {
    final String str = readUTF();

    try {
      return Optional.of(new BigDecimal(str));
    } catch (Exception ignore) {
      return Optional.empty();
    }
  }
}