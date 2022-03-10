package com.revature.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

public class Account {

    private int id;
    private String accountType;
    private String accountNumber;
    private BigDecimal balance;
    private String dateOpened;
    private int clientId;

    public Account(){
    }


    public Account(int id, String accountType, String accountNumber, BigDecimal balance, String dateOpened, int clientId) {
        this.id = id;
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.dateOpened = dateOpened;
        this.clientId = clientId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getDateOpened() {
        return dateOpened;
    }

    public void setDateOpened(String dateOpened) {
        this.dateOpened = dateOpened;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && clientId == account.clientId && Objects.equals(accountType, account.accountType) && Objects.equals(accountNumber, account.accountNumber) && Objects.equals(balance, account.balance) && Objects.equals(dateOpened, account.dateOpened);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountType, accountNumber, balance, dateOpened, clientId);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accountType='" + accountType + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", dateOpened='" + dateOpened + '\'' +
                ", clientId=" + clientId +
                '}';
    }
}
