package net.tnemc.core.common.currency;

import net.tnemc.core.economy.currency.Tier;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * Created by creatorfromhell on 10/22/2016.
 */
public class TNETier implements Tier {
  private boolean major;
  private String single;
  private String plural;
  private Integer weight;
  private ItemTier itemInfo;

  public static TNETier fromReserve(Tier tier) {
    TNETier tneTier = new TNETier();
    tneTier.setPlural(tier.plural());
    tneTier.setSingle(tier.singular());
    tneTier.setWeight(tier.weight());
    tneTier.setMajor(tier.isMajor());
    return tneTier;
  }

  public ItemTier getItemInfo() {
    return itemInfo;
  }

  public void setItemInfo(ItemTier itemInfo) {
    this.itemInfo = itemInfo;
  }

  public void setSingle(String single) {
    this.single = single;
  }

  public void setPlural(String plural) {
    this.plural = plural;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
  }

  public void setMajor(boolean major) {
    this.major = major;
  }

  @Override
  public String singular() {
    return single;
  }

  @Override
  public String plural() {
    return plural;
  }

  @Override
  public boolean isMajor() {
    return major;
  }

  @Override
  public int weight() {
    return weight;
  }
}
