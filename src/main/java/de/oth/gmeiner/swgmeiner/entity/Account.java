/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.oth.gmeiner.swgmeiner.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 *
 * @author Michael
 */
@Entity
public class Account extends SuperEntity implements Serializable {

    String iban;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Customer customer;
    @GeneratedValue(strategy = GenerationType.AUTO)
    long accountNr;
    Date date;

    @GeneratedValue(strategy = GenerationType.AUTO)
    long accountCode;
    double accountBalance;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    AccountType accountType;

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public long getAccountNr() {
        return accountNr;
    }

    public void setAccountNr(long AccountNr) {
        this.accountNr = AccountNr;
    }

    public long getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(long accountCode) {
        this.accountCode = accountCode;
    }

    public double getAccountBalance() {
        //DecimalFormat f = new DecimalFormat("#0.00"); 
        accountBalance = accountBalance * 100;
        accountBalance = Math.round(accountBalance);
        accountBalance = accountBalance / 100;
        return accountBalance;
    }

    // KontoTyp accountType;
    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Account() {

    }

    public Account(String iban, long AccountNr, long accountCode) {
        this.iban = iban;
        this.accountNr = AccountNr;
        this.accountBalance = 0;
        this.accountCode = accountCode;

    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
