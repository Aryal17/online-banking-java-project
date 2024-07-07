package com.prashantaryal.onlinebankingsystem.classes;
public class Account {
    public int id;
    public String accountNumber;
    public String owner;
    public double balance;

    public Account(int id, String accountNumber, String owner, double balance) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = balance;
    }

    // Getters (optional)
    public int getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    // Setters (optional)
    public void setId(int id) {
        this.id = id;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
