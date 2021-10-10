package net.tnemc.core.common.transaction.result;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/7/2017.
 */
public class TransactionResultLost implements TransactionResult {

  @Override
  public String name() {
    return "lost";
  }

  @Override
  public String initiatorMessage() {
    return "Messages.Money.Took";
  }

  @Override
  public String recipientMessage() {
    return "Messages.Money.Taken";
  }

  @Override
  public boolean proceed() {
    return true;
  }
}