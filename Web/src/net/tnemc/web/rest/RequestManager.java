package net.tnemc.web.rest;

import net.tnemc.web.rest.impl.account.AccountExistsRequest;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

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
public class RequestManager {
  private List<IRequest> requests = new ArrayList<>();

  public RequestManager() {
    initBaseRequests();
  }

  public void initSpark() {
    requests.forEach(req->{
      switch(req.type()) {
        case GET:
          get(req.route(), req::work);
          break;
        case POST:
          post(req.route(), req::work);
          break;
        case DELETE:
          delete(req.route(), req::work);
          break;
        default:
          break;
      }
    });
  }

  private void initBaseRequests() {
    requests.add(new AccountExistsRequest());
  }
}