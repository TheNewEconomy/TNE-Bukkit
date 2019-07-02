package net.tnemc.banks.world;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/2/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public enum BankAccountStatus {

  OPEN("Open", true, true, true, true),
  NO_LOAN("NoLoan", true, true, true, false);

  private String id;
  private boolean deposit;
  private boolean withdraw;
  private boolean interest;
  private boolean loan;

  BankAccountStatus(String id, boolean deposit, boolean withdraw, boolean interest, boolean loan) {
    this.id = id;
    this.deposit = deposit;
    this.withdraw = withdraw;
    this.interest = interest;
    this.loan = loan;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public boolean isDeposit() {
    return deposit;
  }

  public void setDeposit(boolean deposit) {
    this.deposit = deposit;
  }

  public boolean isWithdraw() {
    return withdraw;
  }

  public void setWithdraw(boolean withdraw) {
    this.withdraw = withdraw;
  }

  public boolean isInterest() {
    return interest;
  }

  public void setInterest(boolean interest) {
    this.interest = interest;
  }

  public boolean isLoan() {
    return loan;
  }

  public void setLoan(boolean loan) {
    this.loan = loan;
  }}