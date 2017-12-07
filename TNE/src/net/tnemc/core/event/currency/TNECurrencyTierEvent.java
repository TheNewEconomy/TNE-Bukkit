package net.tnemc.core.event.currency;

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
 * Created by Daniel on 7/7/2017.
 */
public abstract class TNECurrencyTierEvent extends TNECurrencyEvent {

  protected String tier;
  protected String tierType;

  public TNECurrencyTierEvent(String world, String currency, String tier, String tierType) {
    super(world, currency);
    this.tier = tier;
    this.tierType = tierType;
  }

  public String getTier() {
    return tier;
  }

  public void setTier(String tier) {
    this.tier = tier;
  }

  public String getTierType() {
    return tierType;
  }

  public void setTierType(String tierType) {
    this.tierType = tierType;
  }
}