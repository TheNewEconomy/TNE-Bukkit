package net.tnemc.core.common.transaction.result;

public class CustomTransactionResult implements TransactionResult {

  private String name;
  private String initiatorMessage;
  private String recipientMessage;
  private boolean proceed;

  public CustomTransactionResult(String name, String initiatorMessage, String recipientMessage, boolean proceed) {
    this.name = name;
    this.initiatorMessage = initiatorMessage;
    this.recipientMessage = recipientMessage;
    this.proceed = proceed;
  }

  @Override
  public String name() {
    return null;
  }

  @Override
  public String initiatorMessage() {
    return null;
  }

  @Override
  public String recipientMessage() {
    return null;
  }

  @Override
  public boolean proceed() {
    return false;
  }
}
