/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.oth.gmeiner.swgmeiner.service;

import de.oth.gmeiner.swgmeiner.entity.Account;
import de.oth.gmeiner.swgmeiner.entity.AccountType;
import de.oth.gmeiner.swgmeiner.entity.Customer;
import de.oth.gmeiner.swgmeiner.entity.Transfer;
import helper.BCrypt;
import helper.qualifier.OptionAccount;
import helper.qualifier.OptionCustomer;
import helper.qualifier.OptionTransfer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.ejb.Schedule;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Michael
 */
@RequestScoped
@WebService(serviceName = "customerService")
public class customerService {

    @Inject
    @OptionCustomer
    private Logger loggerCustomer;

    @Inject
    @OptionAccount
    private Logger loggerAccount;

    @Inject
    @OptionTransfer
    private Logger loggerTransfer;

    @PersistenceContext(unitName = "SWGmeiner_pu")
    private EntityManager entityManager;

    @Transactional
    public Customer login(String email, String password) {
        Query q = entityManager.createQuery("SELECT c.id FROM Customer as c WHERE c.email =:email");
        q.setParameter("email", email);
        List<Long> c = q.getResultList();
        if (!c.isEmpty()) {
            Long Id = c.get(0);
            Customer customer = entityManager.find(Customer.class, Id);
            if (checkPassword(password, customer.getPassword())) {
                return customer;
            } else {
                return null;
            }
        }
        return null;
    }

    @Transactional
    public Customer signup(Customer customer) {
        Query q = entityManager.createQuery("SELECT c.id FROM Customer as c WHERE c.email =:email");
        q.setParameter("email", customer.getEmail());
        List<Long> c = q.getResultList();
        if (c.isEmpty()) {
            entityManager.persist(customer);
            loggerCustomer.info("new Customer created: " + customer.getId());
            return customer;
        } else {
            return null;
        }
    }

    @Transactional
    public double getBankBalance(Account account) {
        Account a = entityManager.find(Account.class, account.getId());
        return a.getAccountBalance();
    }

    @Transactional
    public double depositMoney(Account account, double amount) {
        entityManager.find(Account.class, account.getId());
        account.setAccountBalance(account.getAccountBalance() + amount);
        entityManager.merge(account);
        Transfer t = new Transfer();
        t.setAmount(amount);
        t.setPurpose("Deposit Money");
        t.setDate(new Date());
        t.setReceiver(entityManager.find(Account.class, account.getId()));
        t.setTransmitter(null);
        entityManager.persist(t);
        loggerTransfer.info("new Transfer created: " + t.getId());
        return account.getAccountBalance();
    }

    @Transactional
    public boolean moneyPayout(Account account, double amount) {
        entityManager.find(Account.class, account.getId());
        account.setAccountBalance(account.getAccountBalance() - amount);
        entityManager.merge(account);
        Transfer t = new Transfer();
        t.setAmount(amount);
        t.setDate(new Date());
        t.setPurpose("Money payout");
        t.setTransmitter(entityManager.find(Account.class, account.getId()));
        t.setReceiver(null);
        entityManager.persist(t);
        loggerTransfer.info("new Transfer created: " + t.getId());
        return false;
    }

    @Transactional
    public Account createAccount(Account account, Customer c, AccountType accType) {
        Customer c1 = entityManager.find(Customer.class, c.getId());
        AccountType a = entityManager.find(AccountType.class, accType.getId());
        account.setCustomer(c1);
        account.setAccountType(a);
        entityManager.persist(account);
        loggerAccount.info("new Account created: " + account.getId());
        return account;
    }

    @Transactional
    public ArrayList getAccountbyCustomer(Customer c) {
        ArrayList<Account> array = new ArrayList();
        Query q = entityManager.createQuery("SELECT a.id FROM Account as a WHERE a.customer =:id order by date desc");
        q.setParameter("id", c);
        List<Long> account_id = q.getResultList();
        for (Long id : account_id) {
            array.add(entityManager.find(Account.class, id));
        }
        return array;
    }

    @Transactional
    public boolean deleteAccount(Account acc, ArrayList<Transfer> list) {
        for (Transfer t : list) {
            t.setReceiver(null);
            t.setTransmitter(null);
            entityManager.merge(t);
            entityManager.remove(entityManager.find(Transfer.class, t.getId()));
        }
        Account account = entityManager.find(Account.class, acc.getId());
        account.setAccountType(null);
        account.setCustomer(null);
        entityManager.merge(account);
        entityManager.remove(account);
        return true;
    }

    @Transactional
    public AccountType getTypebyId(long value) {
        AccountType a = entityManager.find(AccountType.class, value);
        return a;
    }

    @Transactional
    public Collection<AccountType> allTypes() {
        Query q = entityManager.createQuery("SELECT a FROM AccountType a");
        List<AccountType> type_id = q.getResultList();
        return type_id;
    }

    @Transactional
    public List<Account> allAccounts() {
        Query q = entityManager.createQuery("SELECT a FROM Account a");
        List<Account> accounts = q.getResultList();
        return accounts;
    }

    @Transactional
    public void updateAccount(Account a, Transfer t) {
        entityManager.merge(a);
        entityManager.merge(t);
    }

    public static String hashPassword(String password_plaintext) {
        String salt = BCrypt.gensalt(12);
        String hashed_password = BCrypt.hashpw(password_plaintext, salt);
        System.out.println("PASSWORT: " + hashed_password);
        return (hashed_password);
    }
    
    public static boolean checkPassword(String password_plaintext, String stored_hash) {
        boolean password_verified = false;
        if (null == stored_hash || !stored_hash.startsWith("$2a$")) {
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");
        }
        password_verified = BCrypt.checkpw(password_plaintext, stored_hash);
        return (password_verified);
    }

}
