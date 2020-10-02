package net.tnemc.core.common.uuid;

import com.github.tnerevival.TNELib;
import net.tnemc.core.common.api.IDFinder;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 9/9/2020.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public interface UUIDAPI {

  /**
   * @return The URL for this UUID API Service.
   */
  String url();

  /**
   * @return True if the site uses SSL technology, otherwise false.
   */
  default boolean isSSL() {
    return url().contains("https");
  }

  default UUID getUUID(String username) {
    JSONObject object = sendRequestJSON(username);

    UUID id = (object != null && object.containsKey("uuid"))? UUID.fromString(object.get("uuid").toString())
        : IDFinder.ecoID(username, true);

    if(id == null) {
      id = IDFinder.ecoID(username, true);
    }
    TNELib.instance().getUuidManager().addUUID(username, id);
    return id;
  }

  static String dashUUIDString(String uuid) {
    return uuid.replaceAll(TNELib.uuidCreator.pattern(), "$1-$2-$3-$4-$5");
  }

  default JSONObject sendRequestJSON(final String linkAddition) {
    return (JSONObject) JSONValue.parse(sendRequest(linkAddition));
  }

  default String sendRequest(final String linkAddition) {
    StringBuilder builder = new StringBuilder();
    try {

      TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
        public X509Certificate[] getAcceptedIssuers(){return null;}
        public void checkClientTrusted(X509Certificate[] certs, String authType){}
        public void checkServerTrusted(X509Certificate[] certs, String authType){}
      }};

      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(null, trustAllCerts, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

      HttpsURLConnection connection = (HttpsURLConnection) new URL(url() + linkAddition).openConnection();
      connection.setRequestMethod("GET");
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String response;
      while((response = reader.readLine()) != null) {
        builder.append(response);
      }
      reader.close();
    } catch (Exception e) {
      return "";
    }
    return builder.toString();
  }
}