package com.github.tnerevival.core.transaction;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.event.transaction.TNETransactionEvent;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Bukkit;

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
    this(initiator, recipient, cost, TransactionType.MONEY_GIVE, IDFinder.getWorld(IDFinder.getID(initiator)));
  }

  public Transaction(String initiator, String recipient, TransactionCost cost, TransactionType type) {
    this(initiator, recipient, cost, type, IDFinder.getWorld(IDFinder.getID(initiator)));
  }

  public Transaction(String initiator, String recipient, TransactionCost cost, TransactionType type, String world) {
    this.initiator = initiator;
    this.recipient = recipient;
    this.cost = cost;
    this.type = type;
    this.world = world;
  }

  public boolean perform() {
    if(initiator == null && recipient == null) {
      MISCUtils.debug("Initiator and recipient are both null!");
      return false;
    }
    boolean failed = (handleInitiator() == TransactionResult.FAILED || handleRecipient() == TransactionResult.FAILED);
    if(!failed) {
      TNETransactionEvent e = new TNETransactionEvent(this);
      Bukkit.getServer().getPluginManager().callEvent(e);
      if(e.isCancelled()) return false;

      result = TransactionResult.SUCCESS;
      work();
      MISCUtils.debug("ADDING TRANSACTION TO HISTORY.");
      TNE.instance.manager.transactions.add(this);
    }
    return !failed;
  }

  private TransactionResult handleInitiator() {
    MISCUtils.debug("[Initiator] Transaction info: " + world + ":" + cost.getCurrency().getName());
    if(type.equals(TransactionType.MONEY_INQUIRY)) {
      if(recipient == null) {
        UUID id = IDFinder.getID(initiator);
        if (!AccountUtils.exists(id))
          return TransactionResult.FAILED;

        if(cost.getAmount() > 0.0 && AccountUtils.getFunds(id, world, cost.getCurrency().getName()) < cost.getAmount()) {
          return TransactionResult.FAILED;
        }

        if(cost.getItems().size() > 0 && !MISCUtils.hasItems(id, cost.getItems())) {
          return TransactionResult.FAILED;
        }
        return TransactionResult.SUCCESS;
      }
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.MONEY_REMOVE)) {
      if(recipient == null) {
        UUID id = IDFinder.getID(initiator);
        if (!AccountUtils.exists(id))
          return TransactionResult.FAILED;

        if(cost.getAmount() > 0.0 && AccountUtils.getFunds(id, world, cost.getCurrency().getName()) < cost.getAmount()) {
          return TransactionResult.FAILED;
        }

        if(cost.getItems().size() > 0 && !MISCUtils.hasItems(id, cost.getItems())) {
          return TransactionResult.FAILED;
        }
        return TransactionResult.SUCCESS;
      }
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.MONEY_SET)) {
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.MONEY_GIVE)) {
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.MONEY_PAY)) {
      UUID id = IDFinder.getID(initiator);
      if(initiator == null || recipient == null) return TransactionResult.FAILED;
      if(!AccountUtils.exists(id) || !AccountUtils.exists(IDFinder.getID(recipient))) return TransactionResult.FAILED;
      if(cost.getAmount() > 0.0 && AccountUtils.getFunds(id, world, cost.getCurrency().getName()) < cost.getAmount()) {
        return TransactionResult.FAILED;
      }

      if(cost.getItems().size() > 0 && !MISCUtils.hasItems(id, cost.getItems())) {
        return TransactionResult.FAILED;
      }
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.BANK_INQUIRY)) {
      UUID id = IDFinder.getID(initiator);
      if(recipient != null && !BankUtils.bankMember(id, IDFinder.getID(recipient), world)) return TransactionResult.FAILED;
      if(BankUtils.getBankBalance(id, world) < cost.getAmount()) return TransactionResult.FAILED;
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.BANK_WITHDRAWAL)) {
      UUID id = IDFinder.getID(initiator);
      if(recipient != null && !BankUtils.bankMember(id, IDFinder.getID(recipient), world)) return TransactionResult.FAILED;
      if(BankUtils.getBankBalance(id, world) < cost.getAmount()) return TransactionResult.FAILED;
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.BANK_DEPOSIT)) {
      UUID id = IDFinder.getID(initiator);
      if(!AccountUtils.getAccount(IDFinder.getID(recipient)).hasBank(world)) return TransactionResult.FAILED;
      if(cost.getAmount() > 0 && AccountUtils.getFunds(id, world, TNE.instance.manager.currencyManager.get(world).getName()) < cost.getAmount()) return TransactionResult.FAILED;
      if(!BankUtils.bankMember(IDFinder.getID(recipient), id, world)) return TransactionResult.FAILED;
      return TransactionResult.SUCCESS;
    }
    return TransactionResult.SUCCESS;
  }

  private TransactionResult handleRecipient() {
    MISCUtils.debug("[Recipient] Transaction info: " + world + ":" + cost.getCurrency().getName());
    if(type.equals(TransactionType.MONEY_INQUIRY)) {
      if(recipient != null) {
        UUID id = IDFinder.getID(recipient);
        if (!AccountUtils.exists(id))
          return TransactionResult.FAILED;
        if (cost.getAmount() > 0.0 && AccountUtils.getFunds(id, world, cost.getCurrency().getName()) < cost.getAmount()) {
          return TransactionResult.FAILED;
        }

        if(cost.getItems().size() > 0 && !MISCUtils.hasItems(id, cost.getItems())) {
          return TransactionResult.FAILED;
        }
        return TransactionResult.SUCCESS;
      }
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.MONEY_REMOVE)) {
      if(recipient != null) {
        UUID id = IDFinder.getID(recipient);
        if (!AccountUtils.exists(id))
          return TransactionResult.FAILED;
        if (cost.getAmount() > 0.0 && AccountUtils.getFunds(id, world, cost.getCurrency().getName()) < cost.getAmount()) {
          return TransactionResult.FAILED;
        }

        if(cost.getItems().size() > 0 && !MISCUtils.hasItems(id, cost.getItems())) {
          return TransactionResult.FAILED;
        }
        return TransactionResult.SUCCESS;
      }
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.MONEY_SET)) {
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.MONEY_GIVE)) {
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.MONEY_PAY)) {
      UUID id = IDFinder.getID(recipient);
      MISCUtils.debug("Found id");
      if(initiator == null || recipient == null) return TransactionResult.FAILED;
      if(!AccountUtils.exists(id) || !AccountUtils.exists(IDFinder.getID(initiator))) return TransactionResult.FAILED;
      if(cost.getAmount() > 0.0 && AccountUtils.getFunds(IDFinder.getID(initiator), world, cost.getCurrency().getName()) < cost.getAmount()) {
        return TransactionResult.FAILED;
      }

      if(cost.getItems().size() > 0 && !MISCUtils.hasItems(IDFinder.getID(initiator), cost.getItems())) {
        return TransactionResult.FAILED;
      }
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.BANK_INQUIRY)) {
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.BANK_WITHDRAWAL)) {
      UUID id = IDFinder.getID(recipient);
      UUID initID = IDFinder.getID(initiator);
      if(!AccountUtils.getAccount(initID).hasBank(world)) return TransactionResult.FAILED;
      if(recipient != null && !BankUtils.bankMember(initID, id, world)) return TransactionResult.FAILED;
      return TransactionResult.SUCCESS;
    } else if(type.equals(TransactionType.BANK_DEPOSIT)) {
      UUID id = IDFinder.getID(recipient);
      if(!AccountUtils.getAccount(id).hasBank(world)) return TransactionResult.FAILED;
      if(cost.getAmount() > 0 && AccountUtils.getFunds(IDFinder.getID(initiator), world, TNE.instance.manager.currencyManager.get(world).getName()) < cost.getAmount()) return TransactionResult.FAILED;
      if(recipient != null && !BankUtils.bankMember(id, IDFinder.getID(initiator), world)) return TransactionResult.FAILED;
      return TransactionResult.SUCCESS;
    }
    return TransactionResult.SUCCESS;
  }

  public void work() {
    MISCUtils.debug("Working transaction.");
    UUID id = (recipient == null)? IDFinder.getID(initiator) : IDFinder.getID(recipient);
    if(type.equals(TransactionType.MONEY_INQUIRY)) {
      if(recipient == null) {
        initiatorOldBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
        initiatorBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
        return;
      }
      recipientOldBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
      recipientBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
    } else if(type.equals(TransactionType.MONEY_REMOVE)) {
      if(recipient == null) {
        initiatorOldBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
        AccountUtils.removeFunds(id, world, cost.getAmount(), cost.getCurrency().getName());
        MISCUtils.setItems(id, cost.getItems(), false);
        initiatorBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
        return;
      }
      recipientOldBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
      AccountUtils.removeFunds(id, world, cost.getAmount(), cost.getCurrency().getName());
      MISCUtils.setItems(id, cost.getItems(), false);
      recipientBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
    } else if(type.equals(TransactionType.MONEY_SET)) {
      if(recipient == null) {
        if(cost.getAmount() > 0.0) {
          initiatorOldBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
          AccountUtils.setFunds(id, world, cost.getAmount(), cost.getCurrency().getName());
          initiatorBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
        }

        if(cost.getItems().size() > 0) {
          MISCUtils.setItems(id, cost.getItems(), true, true);
        }
        return;
      }
      recipientOldBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
      AccountUtils.setFunds(id, world, cost.getAmount(), cost.getCurrency().getName());
      MISCUtils.setItems(id, cost.getItems(), true, true);
      recipientBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
    } else if(type.equals(TransactionType.MONEY_GIVE)) {
      if(recipient == null) {
        if(cost.getAmount() > 0.0) {
          initiatorOldBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
          AccountUtils.setFunds(id, world, (AccountUtils.getFunds(id, world, cost.getCurrency().getName()) + cost.getAmount()), cost.getCurrency().getName());
          initiatorBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
        }

        if(cost.getItems().size() > 0) {
          MISCUtils.setItems(id, cost.getItems(), true);
        }
        return;
      }
      recipientOldBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
      AccountUtils.setFunds(id, world, (AccountUtils.getFunds(id, world, cost.getCurrency().getName()) + cost.getAmount()), cost.getCurrency().getName());
      MISCUtils.setItems(id, cost.getItems(), true);
      recipientBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
    } else if(type.equals(TransactionType.MONEY_PAY)) {
      initiatorOldBalance = AccountUtils.getFunds(IDFinder.getID(initiator), world, cost.getCurrency().getName());
      AccountUtils.removeFunds(IDFinder.getID(initiator), world, cost.getAmount(), cost.getCurrency().getName());
      MISCUtils.setItems(IDFinder.getID(initiator), cost.getItems(), false);
      initiatorBalance = AccountUtils.getFunds(IDFinder.getID(initiator), world, cost.getCurrency().getName());
      recipientOldBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
      AccountUtils.setFunds(id, world, (AccountUtils.getFunds(id, world, cost.getCurrency().getName()) + cost.getAmount()), cost.getCurrency().getName());
      MISCUtils.setItems(id, cost.getItems(), true);
      recipientBalance = AccountUtils.getFunds(id, world, cost.getCurrency().getName());
    } else if(type.equals(TransactionType.BANK_INQUIRY)) {
      initiatorOldBalance = BankUtils.getBankBalance(IDFinder.getID(initiator), world);
      initiatorBalance = BankUtils.getBankBalance(IDFinder.getID(initiator), world);
    } else if(type.equals(TransactionType.BANK_WITHDRAWAL)) {
      initiatorOldBalance = BankUtils.getBankBalance(IDFinder.getID(initiator), world);
      BankUtils.setBankBalance(IDFinder.getID(initiator), world, (BankUtils.getBankBalance(IDFinder.getID(initiator), world) - cost.getAmount()));
      initiatorBalance = BankUtils.getBankBalance(IDFinder.getID(initiator), world);
      recipientOldBalance = AccountUtils.getFunds(IDFinder.getID(recipient), world, TNE.instance.manager.currencyManager.get(world).getName());
      AccountUtils.setFunds(IDFinder.getID(recipient), world, (AccountUtils.getFunds(IDFinder.getID(recipient), world, TNE.instance.manager.currencyManager.get(world).getName()) + cost.getAmount()), TNE.instance.manager.currencyManager.get(world).getName());
      recipientBalance = AccountUtils.getFunds(IDFinder.getID(recipient), world, TNE.instance.manager.currencyManager.get(world).getName());
    } else if(type.equals(TransactionType.BANK_DEPOSIT)) {
      AccountUtils.setFunds(IDFinder.getID(initiator), world,
          (AccountUtils.getFunds(IDFinder.getID(initiator), world,
          TNE.instance.manager.currencyManager.get(world).getName()) - cost.getAmount()),
          TNE.instance.manager.currencyManager.get(world).getName()
      );
      recipientOldBalance = BankUtils.getBankBalance(IDFinder.getID(recipient), world);
      BankUtils.setBankBalance(IDFinder.getID(recipient), world, (BankUtils.getBankBalance(IDFinder.getID(recipient), world) + cost.getAmount()));
      recipientBalance = BankUtils.getBankBalance(IDFinder.getID(recipient), world);
    }
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