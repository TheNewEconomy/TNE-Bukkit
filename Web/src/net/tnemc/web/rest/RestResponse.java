package net.tnemc.web.rest;

import com.google.gson.JsonElement;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/16/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class RestResponse {

  private RestResponseType type;
  private String message;
  private JsonElement data;

  public RestResponse(RestResponseType type) {
    this.type = type;
  }

  public RestResponse(RestResponseType type, String message) {
    this.type = type;
    this.message = message;
  }

  public RestResponse(RestResponseType type, JsonElement data) {
    this.type = type;
    this.data = data;
  }

  public RestResponseType getType() {
    return type;
  }

  public void setType(RestResponseType type) {
    this.type = type;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public JsonElement getData() {
    return data;
  }

  public void setData(JsonElement data) {
    this.data = data;
  }
}