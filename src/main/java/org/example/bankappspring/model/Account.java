package org.example.bankappspring.model;

public class Account {
  int accountNo;
  String owner;
  double balance;

  public Account(int accountNo, String owner, double balance) {
    this.accountNo = accountNo;
    this.owner = owner;
    this.balance = balance;
  }

  @Override
  public String toString() {
    return "Account " + accountNo + ": " + owner + "\t" + balance;
  }

  public int getAccountNo() {
    return accountNo;
  }

  public void setAccountNo(int accountNo) {
    this.accountNo = accountNo;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public double getBalance() {
    return balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  public static void main(String[] args) {
    Account a1=new Account(1, "Ole", 100);
    System.out.println(a1);
  }
}
