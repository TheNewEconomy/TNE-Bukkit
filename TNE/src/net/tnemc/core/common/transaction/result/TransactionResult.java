package net.tnemc.core.common.transaction.result;

/**
 * Created by creatorfromhell on 8/9/2017.
 * All rights reserved.
 **/
public interface TransactionResult {

  /**
   * @return The name of this transaction result for storage purposes.
   */
  String name();

  /**
   * @return The message that the initiator sees, or "" for no message.
   */
  String initiatorMessage();

  /**
   * @return The message that the recipient sees, or "" for no message.
   */
  String recipientMessage();

  /**
   * @return True if the transaction may proceed, otherwise false.
   */
  boolean proceed();
}