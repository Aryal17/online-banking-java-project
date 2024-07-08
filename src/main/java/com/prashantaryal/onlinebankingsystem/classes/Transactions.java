package com.prashantaryal.onlinebankingsystem.classes;

public class Transactions {
    private int id;
    private String  accountFrom;
    private String  accountTo;

    private double amount;

    public Transactions() {}

    public Transactions(int id, String accountTo, String accountFrom, double amount) {
        this.id = id;
        this.accountTo = accountTo;
        this.accountFrom = accountFrom;
        this.amount = amount;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(String accountTo) {
        this.accountTo = accountTo;
    }

     public String  getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(String accountFrom) {
        this.accountFrom = accountFrom;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
