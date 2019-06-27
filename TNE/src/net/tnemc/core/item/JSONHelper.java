package net.tnemc.core.item;

import org.json.simple.JSONObject;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 12/1/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class JSONHelper {

  private JSONObject object;

  public JSONHelper(JSONObject object) {
    this.object = object;
  }

  public boolean has(String identifier) {
    return object.containsKey(identifier);
  }

  public boolean isNull(String identifier) {
    return object.get(identifier) == null;
  }

  public JSONHelper getHelper(String identifier) {
    return new JSONHelper(getJSON(identifier));
  }

  public JSONObject getJSON(String identifier) {
    return (JSONObject)object.get(identifier);
  }

  public Short getShort(String identifier) {
    return Short.valueOf(getString(identifier));
  }

  public Double getDouble(String identifier) {
    return Double.valueOf(getString(identifier));
  }

  public Integer getInteger(String identifier) {
    return Integer.valueOf(getString(identifier));
  }

  public Boolean getBoolean(String identifier) {
    return Boolean.valueOf(getString(identifier));
  }

  public String getString(String identifier) {
    return object.get(identifier).toString();
  }

  public JSONObject getObject() {
    return object;
  }

  public void setObject(JSONObject object) {
    this.object = object;
  }
}