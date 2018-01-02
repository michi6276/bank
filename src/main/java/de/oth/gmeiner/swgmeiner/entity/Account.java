/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.oth.gmeiner.swgmeiner.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
import javax.persistence.OneToOne;

/**
 *
 * @author Michael
 */
@Entity
public class Account implements Serializable {

    
    String iban;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Customer customer;
    @GeneratedValue(strategy = GenerationType.AUTO)
    long AccountNr;
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
        return AccountNr;
    }

    public void setAccountNr(long AccountNr) {
        this.AccountNr = AccountNr;
    }

    public long getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(long accountCode) {
        this.accountCode = accountCode;
    }

    public double getAccountBalance() {
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
        this.AccountNr = AccountNr;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.oth.gmeiner.swgmeiner.entity.Account[ id=" + id + " ]";
    }

}
