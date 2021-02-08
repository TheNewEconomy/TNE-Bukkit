package net.tnemc.core.common.currency.calculations;

import net.tnemc.core.common.currency.TNETier;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 2/2/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class NewCalculation {
  private CalculationProvider provider;

  Map<String, Integer> inventoryMaterials = new HashMap<>();
  TreeMap<BigDecimal, TNETier> materialValues = new TreeMap<>();

}