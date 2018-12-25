package net.tnemc.web;

import net.tnemc.web.pages.general.FourZeroFour;
import spark.Request;
import spark.Route;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static spark.Spark.port;
import static spark.Spark.staticFiles;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 11/24/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class WebManager {

  private Map<String, Route> get = new HashMap<>();
  private Map<String, Route> post = new HashMap<>();
  private LinkedHashMap<String, String> navLinks = new LinkedHashMap<>();

  private void start(int port) throws Exception {

    //Configure Spark
    port(port);
    staticFiles.location("/login");
    staticFiles.expireTime(600L);

    //Set up routes
    get.forEach(Spark::get);
    post.forEach(Spark::post);
    Spark.get("*", new FourZeroFour(), new FreeMarkerEngine());
  }

  public void addGet(String path, Route request) {
    get.put(path, request);
  }

  public void removeGet(String path) {
    get.remove(path);
  }

  public void addPost(String path, Route request) {
    post.put(path, request);
  }

  public void removePost(String path) {
    post.remove(path);
  }

  public void addNavLink(String path, String display) {
    navLinks.put(path, display);
  }

  public void removeNavLink(String path) {
    navLinks.remove(path);
  }

  public Map<String, Route> getGet() {
    return get;
  }

  public Map<String, Route> getPost() {
    return post;
  }

  public Map<String, String> getNavLinks() {
    return navLinks;
  }

  private static boolean shouldReturnHtml(Request request) {
    String accept = request.headers("Accept");
    return accept != null && accept.contains("text/html");
  }
}