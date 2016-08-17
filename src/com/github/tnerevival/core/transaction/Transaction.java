package com.github.tnerevival.core.transaction;

import com.github.tnerevival.TNE;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;

import java.util.UUID;

public class Transaction {
  private String initiator;
  private String recipient;
  private double amount;
  private TransactionType type;
  private String world;

  public Transaction(String initiator, String recipient, double amount) {
    this(initiator, recipient, amount, TransactionType.MONEY_GIVE, TNE.instance.defaultWorld);
  }

  public Transaction(String initiator, String recipient, double amount, TransactionType type) {
    this(initiator, recipient, amount, type, TNE.instance.defaultWorld);
  }

  public Transaction(String initiator, String recipient, double amount, TransactionType type, String world) {
    this.initiator = initiator;
    this.recipient = recipient;
    this.amount = amount;
    this.type = type;
    this.world = world;
  }

  public boolean perform() {
    return(!handleInitiator() || !handleRecipient());
  }

  private boolean handleInitiator() {
    if(type.equals(TransactionType.MONEY_INQUIRY)) {
      if(recipient == null) {
        UUID id = MISCUtils.distringuishId(initiator);
        if (!AccountUtils.exists(id))
          return false;
        if (AccountUtils.getFunds(id, world) < amount) {
          double difference = amount - AccountUtils.getFunds(id, world);
          //TODO: Check if bank balance pay is enabled?
          if (BankUtils.hasBank(id, world)) {
            return BankUtils.getBankBalance(id, world) >= difference;
          }
          return false;
        }
        return true;
      }
      return true;
    } else if(type.equals(TransactionType.MONEY_REMOVE)) {
      if(recipient == null) {
        UUID id = MISCUtils.distringuishId(initiator);
        if (!AccountUtils.exists(id))
          return false;
        if (AccountUtils.getFunds(id, world) < amount) {
          double difference = amount - AccountUtils.getFunds(id, world);
          //TODO: Check if bank balance pay is enabled?
          if (BankUtils.hasBank(id, world) && BankUtils.getBankBalance(id, world) >= difference) {
            BankUtils.setBankBalance(id, world, (BankUtils.getBankBalance(id, world) - difference));
            AccountUtils.setFunds(id, world, 0.0);
            return true;
          }
          return false;
        }
        AccountUtils.setFunds(id, world, (AccountUtils.getFunds(id, world) - amount));
        return true;
      }
      return true;
    } else if(type.equals(TransactionType.MONEY_GIVE)) {
      if(recipient == null) {
        UUID id = MISCUtils.distringuishId(initiator);
        AccountUtils.setFunds(id, world, (AccountUtils.getFunds(id, world) + amount));
      }
      return true;
    } else if(type.equals(TransactionType.MONEY_PAY)) {
      UUID id = MISCUtils.distringuishId(initiator);
      if(initiator == null || recipient == null) return false;
      if(!AccountUtils.exists(id) || AccountUtils.exists(MISCUtils.distringuishId(recipient))) return false;
      if(AccountUtils.getFunds(id, world) < amount) {
        double difference = amount - AccountUtils.getFunds(id, world);
        //TODO: Check if bank balance pay is enabled?
        if(BankUtils.hasBank(id, world) && BankUtils.getBankBalance(id, world) >= difference) {
          BankUtils.setBankBalance(id, world, (BankUtils.getBankBalance(id, world) - difference));
          AccountUtils.setFunds(id, world, 0.0);
          return true;
        }
        return false;
      }
      AccountUtils.setFunds(id, world, (AccountUtils.getFunds(id, world) - amount));
      return true;
    } else if(type.equals(TransactionType.BANK_INQUIRY)) {
      UUID id = MISCUtils.distringuishId(initiator);
      if(!BankUtils.hasBank(id, world)) return false;
      if(recipient != null && !BankUtils.bankMember(id, MISCUtils.distringuishId(recipient), world)) return false;
      return (BankUtils.getBankBalance(id, world) >= amount);
    } else if(type.equals(TransactionType.BANK_WITHDRAWAL)) {
      UUID id = MISCUtils.distringuishId(initiator);
      if(!BankUtils.hasBank(id, world)) return false;
      if(recipient != null && !BankUtils.bankMember(id, MISCUtils.distringuishId(recipient), world)) return false;
      if(BankUtils.getBankBalance(id, world) < amount) return false;
      BankUtils.setBankBalance(id, world, (BankUtils.getBankBalance(id, world) - amount));
      return true;
    } else if(type.equals(TransactionType.BANK_DEPOSIT)) {
      UUID id = MISCUtils.distringuishId(initiator);
      if(!BankUtils.hasBank(MISCUtils.distringuishId(recipient), world)) return false;
      if(AccountUtils.getFunds(id, world) < amount) return false;
      if(!BankUtils.bankMember(MISCUtils.distringuishId(recipient), id, world)) return false;
      AccountUtils.setFunds(id, world, (AccountUtils.getFunds(id, world) - amount));
      return true;
    }
    return true;
  }

  private boolean handleRecipient() {
    if(type.equals(TransactionType.MONEY_INQUIRY)) {
      if(recipient != null) {
        UUID id = MISCUtils.distringuishId(recipient);
        if (!AccountUtils.exists(id))
          return false;
        if (AccountUtils.getFunds(id, world) < amount) {
          double difference = amount - AccountUtils.getFunds(id, world);
          //TODO: Check if bank balance pay is enabled?
          if (BankUtils.hasBank(id, world)) {
            return BankUtils.getBankBalance(id, world) >= difference;
          }
          return false;
        }
        return true;
      }
      return true;
    } else if(type.equals(TransactionType.MONEY_REMOVE)) {
      if(recipient != null) {
        UUID id = MISCUtils.distringuishId(recipient);
        if (!AccountUtils.exists(id))
          return false;
        if (AccountUtils.getFunds(id, world) < amount) {
          double difference = amount - AccountUtils.getFunds(id, world);
          //TODO: Check if bank balance pay is enabled?
          if (BankUtils.hasBank(id, world) && BankUtils.getBankBalance(id, world) >= difference) {
            BankUtils.setBankBalance(id, world, (BankUtils.getBankBalance(id, world) - difference));
            AccountUtils.setFunds(id, world, 0.0);
            return true;
          }
          return false;
        }
        AccountUtils.setFunds(id, world, (AccountUtils.getFunds(id, world) - amount));
        return true;
      }
      return true;
    } else if(type.equals(TransactionType.MONEY_GIVE)) {
      if(recipient != null) {
        UUID id = MISCUtils.distringuishId(recipient);
        AccountUtils.setFunds(id, world, (AccountUtils.getFunds(id, world) + amount));
      }
      return true;
    } else if(type.equals(TransactionType.MONEY_PAY)) {
      UUID id = MISCUtils.distringuishId(recipient);
      if(initiator == null || recipient == null) return false;
      if(!AccountUtils.exists(id) || AccountUtils.exists(MISCUtils.distringuishId(recipient))) return false;
      if(AccountUtils.getFunds(MISCUtils.distringuishId(initiator), world) < amount) {
        double difference = amount - AccountUtils.getFunds(MISCUtils.distringuishId(initiator), world);
        //TODO: Check if bank balance pay is enabled?
        if(BankUtils.hasBank(MISCUtils.distringuishId(initiator), world) && BankUtils.getBankBalance(MISCUtils.distringuishId(initiator), world) >= difference) {
          AccountUtils.setFunds(id, world, (AccountUtils.getFunds(id, world) + amount));
          return true;
        }
        return false;
      }
      AccountUtils.setFunds(id, world, (AccountUtils.getFunds(id, world) + amount));
      return true;
    } else if(type.equals(TransactionType.BANK_INQUIRY)) {
      return true;
    } else if(type.equals(TransactionType.BANK_WITHDRAWAL)) {
      UUID id = MISCUtils.distringuishId(recipient);
      if(!BankUtils.hasBank(id, world)) return false;
      if(recipient != null && !BankUtils.bankMember(MISCUtils.distringuishId(initiator), id, world)) return false;
      if(BankUtils.getBankBalance(MISCUtils.distringuishId(initiator), world) < amount) return false;
      AccountUtils.setFunds(id, world, (AccountUtils.getFunds(id, world) + amount));
      return true;
    } else if(type.equals(TransactionType.BANK_DEPOSIT)) {
      UUID id = MISCUtils.distringuishId(recipient);
      if(!BankUtils.hasBank(id, world)) return false;
      if(AccountUtils.getFunds(MISCUtils.distringuishId(initiator), world) < amount) return false;
      if(recipient != null && !BankUtils.bankMember(id, MISCUtils.distringuishId(initiator), world)) return false;
      BankUtils.setBankBalance(id, world, (BankUtils.getBankBalance(id, world) + amount));
      return true;
    }
    return true;
  }

  public String getInitiator() {
    return initiator;
  }

  public String getRecipient() {
    return recipient;
  }

  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public TransactionType getType() {
    return type;
  }

  public void setType(TransactionType type) {
    this.type = type;
  }

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }
}