package net.tnemc.core.common.account;

public enum AccountStatus {

  NORMAL("Normal", true, true, true),
  LOCKED("Locked", false, false, false),
  VAULT_LOCKED("VaultLocked", true, true, false),
  BANK_LOCKED("BankLocked", true, false, true),
  BALANCE_LOCKED("BalanceLocked", false, false, true);

  private String name;
  private Boolean balance;
  private Boolean bank;
  private Boolean vault;

  AccountStatus(String name, Boolean balance, Boolean bank, Boolean vault) {
    this.name = name;
    this.balance = balance;
    this.bank = bank;
    this.vault = vault;
  }

  public static AccountStatus fromName(String name) {
    for(AccountStatus status : values()) {
      if(status.getName().equalsIgnoreCase(name)) {
        return status;
      }
    }
    return NORMAL;
  }

  public String getName() {
    return name;
  }

  public Boolean getBalance() {
    return balance;
  }

  public Boolean getBank() {
    return bank;
  }

  public Boolean getVault() {
    return vault;
  }
}