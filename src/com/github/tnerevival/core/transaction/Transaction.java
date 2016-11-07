package com.github.tnerevival.core.transaction;

import com.github.tnerevival.TNE;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;

import java.util.UUID;

public class Transaction {
  private String initiator;
  private String recipient;
  private TransactionResult result = TransactionResult.FAILED;
  private TransactionCost cost;
  private TransactionType type;
  private String world;
  private Double initiatorOldBalance;
  private Double initiatorBalance;
  private Double recipientOldBalance;
  private Double recipientBalance;

  public Transaction(String initiator, String recipient, TransactionCost cost) {
    this(initiator, recipient, cost, TransactionType.MONEY_GIVE, TNE.instance.defaultWorld);
  }

  public Transaction(String initiator, String recipient, TransactionCost cost, TransactionType type) {
    this(initiator, recipient, cost, type, TNE.instance.defaultWorld);
  }

  public Transaction(String initiator, String recipient, TransactionCost cost, TransactionType type, String world) {
    this.initiator = initiator;
    this.recipient = recipient;
    this.cost = cost;
    this.type = type;
    this.world = world;
  }

  public boolean perform() {
    boolean failed = (handleInitiator() == TransactionResult.FAILED || handleRecipient() == TransactionResult.FAILED);
    if(!failed) {
      result = TransactionResult.SUCCESS;
      MISCUtils.debug("ADDING TRANSACTION TO HISTORY.");
      TNE.instance.manager.transactions.add(this);
    }
    return !failed;
  }

  private TransactionResult handleInitiator() {
    if(type.equals(TransactionType.MONEY_INQUIRY)) {
      if(recipient == null) {
        UUID id = MISCUtils.distringuishId(initiator);
        if (!AccountUtils.exists(id))
          return TransactionResult.FAILED;

        if(cost.getAmount() > 0.0 && AccountUtils.getFunds(id, world, cost.getCurrency().getName()) < cost.getAmount()) {
          return TransactionResult.FAILED;
        }

        if(cost.getItems().size() > 0 && !MISCUtils.hasItems(id, cost.getItems())) {
          return TransactionResult.FAILED;
        }

        initiatorOldBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
        initiatorBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
        return TransactionResult.SUCCESS;
      }
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.MONEY_REMOVE)) {
      if(recipient == null) {
        UUID id = MISCUtils.distringuishId(initiator);
        if (!AccountUtils.exists(id))
          return TransactionResult.FAILED;

        if(cost.getAmount() > 0.0 && AccountUtils.getFunds(id, world, cost.getCurrency().getName()) < cost.getAmount()) {
          return TransactionResult.FAILED;
        }

        if(cost.getItems().size() > 0 && !MISCUtils.hasItems(id, cost.getItems())) {
          return TransactionResult.FAILED;
        }

        initiatorOldBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
        AccountUtils.removeFunds(id, world, cost.getAmount(), cost.getCurrency().getName());
        MISCUtils.setItems(id, cost.getItems(), false);
        initiatorBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
        return TransactionResult.SUCCESS;
      }
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.MONEY_SET)) {
      if(recipient == null) {
        UUID id = MISCUtils.distringuishId(initiator);
        if(cost.getAmount() > 0.0) {
          initiatorOldBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
          AccountUtils.setFunds(id, world, cost.getAmount(), cost.getCurrency().getName());
          initiatorBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
        }

        if(cost.getItems().size() > 0) {
          MISCUtils.setItems(id, cost.getItems(), true, true);
        }
      }
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.MONEY_GIVE)) {
      if(recipient == null) {
        UUID id = MISCUtils.distringuishId(initiator);
        if(cost.getAmount() > 0.0) {
          initiatorOldBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
          AccountUtils.setFunds(id, world, (AccountUtils.getFunds(id, world, cost.getCurrency().getName()) + cost.getAmount()), cost.getCurrency().getName());
          initiatorBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
        }

        if(cost.getItems().size() > 0) {
          MISCUtils.setItems(id, cost.getItems(), true);
        }
      }
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.MONEY_PAY)) {
      UUID id = MISCUtils.distringuishId(initiator);
      if(initiator == null || recipient == null) return TransactionResult.FAILED;
      if(!AccountUtils.exists(id) || AccountUtils.exists(MISCUtils.distringuishId(recipient))) return TransactionResult.FAILED;
      if(cost.getAmount() > 0.0 && AccountUtils.getFunds(id, world, cost.getCurrency().getName()) < cost.getAmount()) {
        return TransactionResult.FAILED;
      }

      if(cost.getItems().size() > 0 && !MISCUtils.hasItems(id, cost.getItems())) {
        return TransactionResult.FAILED;
      }

      initiatorOldBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
      AccountUtils.removeFunds(id, world, cost.getAmount(), cost.getCurrency().getName());
      MISCUtils.setItems(id, cost.getItems(), false);
      initiatorBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.BANK_INQUIRY)) {
      UUID id = MISCUtils.distringuishId(initiator);
      if(recipient != null && !BankUtils.bankMember(id, MISCUtils.distringuishId(recipient), world)) return TransactionResult.FAILED;
      if(BankUtils.getBankBalance(id, world) < cost.getAmount()) return TransactionResult.FAILED;

      initiatorOldBalance = BankUtils.getBankBalance(id, world);
      initiatorBalance = BankUtils.getBankBalance(id, world);
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.BANK_WITHDRAWAL)) {
      UUID id = MISCUtils.distringuishId(initiator);
      if(recipient != null && !BankUtils.bankMember(id, MISCUtils.distringuishId(recipient), world)) return TransactionResult.FAILED;
      if(BankUtils.getBankBalance(id, world) < cost.getAmount()) return TransactionResult.FAILED;
      initiatorOldBalance = BankUtils.getBankBalance(id, world);
      BankUtils.setBankBalance(id, world, (BankUtils.getBankBalance(id, world) - cost.getAmount()));
      initiatorBalance = BankUtils.getBankBalance(id, world);
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.BANK_DEPOSIT)) {
      UUID id = MISCUtils.distringuishId(initiator);
      if(!BankUtils.hasBank(MISCUtils.distringuishId(recipient), world)) return TransactionResult.FAILED;
      if(cost.getAmount() > 0 && AccountUtils.getFunds(id, world, TNE.instance.manager.currencyManager.get(world).getName()) < cost.getAmount()) return TransactionResult.FAILED;
      if(!BankUtils.bankMember(MISCUtils.distringuishId(recipient), id, world)) return TransactionResult.FAILED;
      AccountUtils.setFunds(id, world, (AccountUtils.getFunds(id, world, TNE.instance.manager.currencyManager.get(world).getName()) - cost.getAmount()), TNE.instance.manager.currencyManager.get(world).getName());
      return TransactionResult.SUCCESS;
    }
    return TransactionResult.SUCCESS;
  }

  private TransactionResult handleRecipient() {
    if(type.equals(TransactionType.MONEY_INQUIRY)) {
      if(recipient != null) {
        UUID id = MISCUtils.distringuishId(recipient);
        if (!AccountUtils.exists(id))
          return TransactionResult.FAILED;
        if (cost.getAmount() > 0.0 && AccountUtils.getFunds(id, world, cost.getCurrency().getName()) < cost.getAmount()) {
          return TransactionResult.FAILED;
        }

        if(cost.getItems().size() > 0 && !MISCUtils.hasItems(id, cost.getItems())) {
          return TransactionResult.FAILED;
        }

        recipientOldBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
        recipientBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
        return TransactionResult.SUCCESS;
      }
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.MONEY_REMOVE)) {
      if(recipient != null) {
        UUID id = MISCUtils.distringuishId(recipient);
        if (!AccountUtils.exists(id))
          return TransactionResult.FAILED;
        if (cost.getAmount() > 0.0 && AccountUtils.getFunds(id, world, cost.getCurrency().getName()) < cost.getAmount()) {
          return TransactionResult.FAILED;
        }

        if(cost.getItems().size() > 0 && !MISCUtils.hasItems(id, cost.getItems())) {
          return TransactionResult.FAILED;
        }

        recipientOldBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
        AccountUtils.removeFunds(id, world, cost.getAmount(), cost.getCurrency().getName());
        MISCUtils.setItems(id, cost.getItems(), false);
        recipientBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
        return TransactionResult.SUCCESS;
      }
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.MONEY_SET)) {
      if(recipient != null) {
        UUID id = MISCUtils.distringuishId(recipient);
        recipientOldBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
        AccountUtils.setFunds(id, world, cost.getAmount(), cost.getCurrency().getName());
        MISCUtils.setItems(id, cost.getItems(), true, true);
        recipientBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
      }
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.MONEY_GIVE)) {
      if(recipient != null) {
        UUID id = MISCUtils.distringuishId(recipient);
        recipientOldBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
        AccountUtils.setFunds(id, world, (AccountUtils.getFunds(id, world, cost.getCurrency().getName()) + cost.getAmount()), cost.getCurrency().getName());
        MISCUtils.setItems(id, cost.getItems(), true);
        recipientBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
      }
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.MONEY_PAY)) {
      UUID id = MISCUtils.distringuishId(recipient);
      if(initiator == null || recipient == null) return TransactionResult.FAILED;
      if(!AccountUtils.exists(id) || AccountUtils.exists(MISCUtils.distringuishId(recipient))) return TransactionResult.FAILED;
      if(cost.getAmount() > 0.0 && AccountUtils.getFunds(MISCUtils.distringuishId(initiator), world, cost.getCurrency().getName()) < cost.getAmount()) {
        return TransactionResult.FAILED;
      }

      if(cost.getItems().size() > 0 && !MISCUtils.hasItems(MISCUtils.distringuishId(initiator), cost.getItems())) {
        return TransactionResult.FAILED;
      }

      recipientOldBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
      AccountUtils.setFunds(id, world, (AccountUtils.getFunds(id, world, cost.getCurrency().getName()) + cost.getAmount()), cost.getCurrency().getName());
      MISCUtils.setItems(id, cost.getItems(), true);
      recipientBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.BANK_INQUIRY)) {
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.BANK_WITHDRAWAL)) {
      UUID id = MISCUtils.distringuishId(recipient);
      UUID initID = MISCUtils.distringuishId(initiator);
      if(!BankUtils.hasBank(initID, world)) return TransactionResult.FAILED;
      if(recipient != null && !BankUtils.bankMember(initID, id, world)) return TransactionResult.FAILED;
      recipientOldBalance = AccountUtils.getFunds(id, world, TNE.instance.manager.currencyManager.get(world).getName());
      AccountUtils.setFunds(id, world, (AccountUtils.getFunds(id, world, TNE.instance.manager.currencyManager.get(world).getName()) + cost.getAmount()), TNE.instance.manager.currencyManager.get(world).getName());
      recipientBalance = AccountUtils.getFunds(id, world, TNE.instance.manager.currencyManager.get(world).getName());
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.BANK_DEPOSIT)) {
      UUID id = MISCUtils.distringuishId(recipient);
      if(!BankUtils.hasBank(id, world)) return TransactionResult.FAILED;
      if(cost.getAmount() > 0 && AccountUtils.getFunds(MISCUtils.distringuishId(initiator), world, TNE.instance.manager.currencyManager.get(world).getName()) < cost.getAmount()) return TransactionResult.FAILED;
      if(recipient != null && !BankUtils.bankMember(id, MISCUtils.distringuishId(initiator), world)) return TransactionResult.FAILED;
      recipientOldBalance = BankUtils.getBankBalance(id, world);
      BankUtils.setBankBalance(id, world, (BankUtils.getBankBalance(id, world) + cost.getAmount()));
      recipientBalance = BankUtils.getBankBalance(id, world);
      return TransactionResult.SUCCESS;
    }
    return TransactionResult.SUCCESS;
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

  public TransactionResult getResult() {
    return result;
  }

  public double getAmount() {
    return cost.getAmount();
  }

  public void setAmount(double amount) {
    this.cost.setAmount(amount);
  }

  public TransactionCost getCost() {
    return cost;
  }

  public void setCost(TransactionCost cost) {
    this.cost = cost;
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

  public Double getInitiatorOldBalance() {
    return initiatorOldBalance;
  }

  public void setInitiatorOldBalance(Double initiatorOldBalance) {
    this.initiatorOldBalance = initiatorOldBalance;
  }

  public Double getInitiatorBalance() {
    return initiatorBalance;
  }

  public void setInitiatorBalance(Double initiatorBalance) {
    this.initiatorBalance = initiatorBalance;
  }

  public Double getRecipientOldBalance() {
    return recipientOldBalance;
  }

  public void setRecipientOldBalance(Double recipientOldBalance) {
    this.recipientOldBalance = recipientOldBalance;
  }

  public Double getRecipientBalance() {
    return recipientBalance;
  }

  public void setRecipientBalance(Double recipientBalance) {
    this.recipientBalance = recipientBalance;
  }
}