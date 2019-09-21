package net.tnemc.core.common.module;

import net.tnemc.core.TNE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Optional;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 8/8/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ModuleUpdateChecker {

  private String module;
  private String updateURL;
  private String oldVersion;
  private String current = "";
  private String jarURL = "";

  public ModuleUpdateChecker(String module, String updateURL, String oldVersion) {
    this.module = module;
    this.updateURL = updateURL;
    this.oldVersion = oldVersion;
  }
  public void check() {
    TNE.logger().info("Checking module for update: " + module);
    if(readInformation()) {
      if(!upToDate()) {
        TNE.logger().info("Updating module: " + module);
        if(download(module, jarURL)) {
          TNE.logger().info("Downloaded module update for " + module);
        } else {
          TNE.logger().info("Failed to download module update for " + module);
        }
        TNE.loader().load(module);
      } else {
        TNE.logger().info("Module " + module + " is up to date.");
      }
    }
  }

  public boolean upToDate() {
    if(current.trim().equalsIgnoreCase("")) {
      return true;
    }

    String[] oldSplit = oldVersion.split("\\.");
    String[] currentSplit = current.split("\\.");

    for(int i = 0; i < currentSplit.length; i++) {

      if(i >= oldSplit.length && !currentSplit[i].equalsIgnoreCase("0")) return false;
      if(i >= oldSplit.length && currentSplit[i].equalsIgnoreCase("0")) continue;

      if(Integer.valueOf(currentSplit[i]) > Integer.valueOf(oldSplit[i])) return false;
    }
    return true;
  }

  public static boolean download(String module, String jarURL) {
    if(jarURL.trim().equalsIgnoreCase("")) {
      return false;
    }

    if(TNE.loader().hasModule(module)) {
      TNE.loader().unload(module);
    }

    try {

      TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
          return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
      }};

      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(null, trustAllCerts, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

      URL url = new URL(jarURL);
      HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
      int responseCode = connection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        String fileName = jarURL.substring(jarURL.lastIndexOf("/") + 1);

        InputStream in = connection.getInputStream();
        File file = new File(TNE.instance().getDataFolder() + File.separator + "modules", fileName);

        if(file.exists()) {
          if(!file.renameTo(new File(TNE.instance().getDataFolder() + File.separator + "modules", "outdated-" + fileName))) {
            return false;
          }
        }

        FileOutputStream out = new FileOutputStream(file);

        int bytesRead = -1;
        byte[] buffer = new byte[4096];
        while ((bytesRead = in.read(buffer)) != -1) {
          out.write(buffer, 0, bytesRead);
        }

        out.close();
        in.close();
        return true;
      }
    } catch (Exception e) {
      return false;
    }
    return false;
  }

  public boolean readInformation() {
    Optional<Document> document = readUpdateURL(updateURL);
    if(document.isPresent()) {
      Document doc = document.get();

      NodeList mainNodes = doc.getElementsByTagName("modules");
      if(mainNodes != null && mainNodes.getLength() > 0) {
        Node modulesNode = mainNodes.item(0);
        Element element = (Element)modulesNode;

        NodeList modules = element.getElementsByTagName("module");

        for(int i = 0; i < modules.getLength(); i++) {
          Node moduleNode = modules.item(i);

          if(moduleNode.hasAttributes()) {

            Node nameNode = moduleNode.getAttributes().getNamedItem("name");
            if (nameNode != null) {

              if (nameNode.getTextContent().equalsIgnoreCase(module)) {

                Node releasedNode = moduleNode.getAttributes().getNamedItem("released");
                if (releasedNode != null) {
                  if(releasedNode.getTextContent().equalsIgnoreCase("yes")) {

                    //We have the correct name, and this module is released.
                    Element moduleElement = (Element)moduleNode;

                    NodeList versions = moduleElement.getElementsByTagName("versions");

                    if(versions != null && versions.getLength() > 0) {

                      NodeList versionsNodes = moduleElement.getElementsByTagName("version");

                      for(int v = 0; v < versionsNodes.getLength(); v++) {

                        Node versionNode = versionsNodes.item(v);
                        if(versionNode != null && versionNode.hasAttributes()) {

                          Element versionElement = (Element)versionNode;

                          Node latest = versionNode.getAttributes().getNamedItem("latest");
                          Node versionReleased = versionNode.getAttributes().getNamedItem("released");

                          if(latest != null && latest.getTextContent().equalsIgnoreCase("yes") &&
                          versionReleased != null && versionReleased.getTextContent().equalsIgnoreCase("yes")) {

                            //We have the latest module version
                            NodeList name = versionElement.getElementsByTagName("name");
                            NodeList jar = versionElement.getElementsByTagName("jar");

                            if(name != null && name.getLength() > 0) {
                              this.current = name.item(0).getTextContent();
                            }

                            if(jar != null && jar.getLength() > 0) {
                              this.jarURL = jar.item(0).getTextContent();
                            }
                            return true;
                          }
                        }
                      }

                    }

                  }

                }
              }
            }
          }
        }
      }
    }
    return false;
  }

  public static Optional<Document> readUpdateURL(String updateURL) {
    try {

      TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
          return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
      }};

      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(null, trustAllCerts, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

      URL url = new URL(updateURL);
      HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.parse(connection.getInputStream());

      return Optional.of(document);

    } catch(Exception e) {
      return Optional.empty();
    }
  }

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public String getURL() {
    return updateURL;
  }

  public void setURL(String updateURL) {
    this.updateURL = updateURL;
  }

  public String getOldVersion() {
    return oldVersion;
  }

  public void setOldVersion(String oldVersion) {
    this.oldVersion = oldVersion;
  }

  public String getCurrent() {
    return current;
  }

  public void setCurrent(String current) {
    this.current = current;
  }

  public String getJarURL() {
    return jarURL;
  }

  public void setJarURL(String jarURL) {
    this.jarURL = jarURL;
  }
}