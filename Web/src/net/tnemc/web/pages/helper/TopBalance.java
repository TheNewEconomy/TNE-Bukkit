package net.tnemc.web.pages.helper;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 12/25/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class TopBalance {

  private String uuid;
  private String username;
  private String amount;

  public TopBalance(String uuid, String username, String amount) {
    this.uuid = uuid;
    this.username = username;
    this.amount = amount;
  }

  public String getUuid() {
    return uuid;
  }

  public String getUsername() {
    return username;
  }

  public String getAmount() {
    return amount;
  }
}