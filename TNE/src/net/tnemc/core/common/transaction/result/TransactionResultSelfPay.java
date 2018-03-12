package net.tnemc.core.common.transaction.result;

import net.tnemc.core.economy.transaction.result.TransactionResult;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/12/2017.
 */
public class TransactionResultSelfPay implements TransactionResult {

  @Override
  public String name() {
    return "selfpay";
  }

  @Override
  public String initiatorMessage() {
    return "Messages.Money.SelfPay";
  }

  @Override
  public String recipientMessage() {
    return "";
  }

  @Override
  public boolean proceed() {
    return false;
  }
}