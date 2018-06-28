package net.tnemc.conversion;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class InvalidDatabaseImport extends Exception {

  public InvalidDatabaseImport(String type, String importer) {
    super("The importer \"" + importer + "\" does not support the database type \"" + type + "\".");
  }
}