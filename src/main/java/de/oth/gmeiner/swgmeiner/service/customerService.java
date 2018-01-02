/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.oth.gmeiner.swgmeiner.service;

import de.oth.gmeiner.swgmeiner.entity.Account;
import de.oth.gmeiner.swgmeiner.entity.AccountType;

import de.oth.gmeiner.swgmeiner.entity.Customer;
import de.oth.gmeiner.swgmeiner.entity.Student;
import de.oth.gmeiner.swgmeiner.entity.Transfer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.ejb.Schedule;
import javax.enterprise.context.RequestScoped;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author Michael
 */
@RequestScoped
@WebService(serviceName = "customerService")
public class customerService {

    public static void newAccount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @PersistenceContext(unitName = "SWGmeiner_pu")
    private EntityManager entityManager;

    @Transactional
    public Customer login(String email, String password) {

        Query q = entityManager.createQuery("SELECT c.id FROM Customer as c WHERE c.email =:email");
        q.setParameter("email", email);
        //List<Integer> result=q.getResultList();
        List<Long> c = q.getResultList();
        if (!c.isEmpty()) {
            Long Id = c.get(0);

            Customer customer = entityManager.find(Customer.class, Id);
            // entityManager.find(Customer.class, Id);
            if (customer.getPassword().equals(password)) {
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
        //List<Integer> result=q.getResultList();
        List<Long> c = q.getResultList();
        if (c.isEmpty()) {
            entityManager.persist(customer);
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
        t.setDate(new Date());
        t.setReceiver( entityManager.find(Account.class, account.getId()));
        t.setTransmitter(null);
        entityManager.persist(t);
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
        t.setTransmitter(entityManager.find(Account.class, account.getId()));
        t.setReceiver(null);
        entityManager.persist(t);
        return false;
    }

    @Transactional
    public Account createAccount(Account account, Customer c, AccountType accType) {

        Customer c1 = entityManager.find(Customer.class, c.getId());
       // AccountType a = entityManager.find(AccountType.class, accType);
        account.setCustomer(c1);
       // account.setAccountType(a);
        //account.setAccountType(AccountType.BankBook);
        entityManager.persist(account);
        return account;
    }

    @Transactional
    public ArrayList getAccountbyCustomer(Customer c) {
             
        ArrayList<Account> array = new ArrayList();
        Query q = entityManager.createQuery("SELECT a.id FROM Account as a WHERE a.customer =:id order by date desc");
        q.setParameter("id", c);
        //List<Integer> result=q.getResultList();
        List<Long> account_id = q.getResultList();
        for (Long id : account_id) {
            array.add(entityManager.find(Account.class, id));
        }

        return array;
    }

    @Transactional
    public boolean deleteAccount(Account acc) {
        Account account = entityManager.find(Account.class, acc.getId());
        entityManager.remove(account);

        return true;

    }

    public AccountType getTypebyId(long value) {
        
     /*   Query q = entityManager.createQuery("SELECT a.id FROM AccountType as a WHERE a.name =:name");
        q.setParameter("name", value);
        //List<Integer> result=q.getResultList();
        List<Long> type_id = q.getResultList();
       AccountType a = entityManager.find(AccountType.class,type_id.get(0)); */
     AccountType a = entityManager.find(AccountType.class, value);
        return a;
    }
    public Collection<AccountType> allTypes(){
         
        Query q = entityManager.createQuery("SELECT a FROM AccountType a");
        
        //List<Integer> result=q.getResultList();
        List<AccountType> type_id = q.getResultList();
        
        
        return type_id;
    }
    
        public List<Account> allAccounts(){
         
        Query q = entityManager.createQuery("SELECT a FROM Account a");
        
        //List<Integer> result=q.getResultList();
        List<Account> accounts = q.getResultList();
        
        
        return accounts;
    }
          @Transactional
        public void updateAccount(Account a){
            entityManager.merge(a);
        }
        
        
        

   

}
