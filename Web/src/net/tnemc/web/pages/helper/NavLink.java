package net.tnemc.web.pages.helper;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 12/18/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class NavLink {

  private String path;
  private String display;
  private String active;

  public NavLink(String path, String display, boolean active) {
    this.path = path;
    this.display = display;
    this.active = (active)? " class='active'" : "";
  }

  public String getPath() {
    return path;
  }

  public String getDisplay() {
    return display;
  }

  public String getActive() {
    return active;
  }
}