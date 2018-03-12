package net.tnemc.core.common.module.injectors;

import java.util.HashMap;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/12/2017.
 */
public class InjectMethod {

  private Map<String, Object> parameters = new HashMap<>();
  private String identifier;

  public InjectMethod(String identifier, Map<String, Object> parameters) {
    this.identifier = identifier;
    this.parameters = parameters;
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public Map<String, Object> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, Object> parameters) {
    this.parameters = parameters;
  }

  public Object getParameter(String identifier) {
    return parameters.get(identifier);
  }

  public void setParameter(String identifier, Object value) {
    parameters.put(identifier, value);
  }
}