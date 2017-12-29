/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.oth.gmeiner.swgmeiner.service;

import de.oth.gmeiner.swgmeiner.entity.Account;
import de.oth.gmeiner.swgmeiner.entity.Customer;
import de.oth.gmeiner.swgmeiner.entity.Student;
import de.oth.gmeiner.swgmeiner.entity.Transfer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
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
@WebService(serviceName = "transferService")
public class transferService {

    public static void newAccount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Customer checkIban(String iban) {
        Customer c = new Customer();

        Query q = entityManager.createQuery("SELECT c.id FROM Customer as c WHERE c.id=(Select a.customer FROM Account as a WHERE a.iban=:iban)");
        q.setParameter("iban", iban);
        //List<Integer> result=q.getResultList();
        List<Long> account_id = q.getResultList();
 
            c = entityManager.find(Customer.class, account_id.get(0));
            
            return c;
        
    }
    @PersistenceContext(unitName = "SWGmeiner_pu")
    private EntityManager entityManager;

    @Transactional
    public boolean deleteAccount(Account acc) {
        Account account = entityManager.find(Account.class, acc.getId());
        entityManager.merge(account);
        entityManager.remove(account);

        return false;

    }

    @Transactional
    public Account getAccountbyIban(String iban) {
        ArrayList<Account> array = new ArrayList();
        Query q = entityManager.createQuery("SELECT a.id FROM Account as a WHERE a.iban =:iban");
        q.setParameter("iban", iban);
        //List<Integer> result=q.getResultList();
        List<Long> account_id = q.getResultList();
        if (account_id.isEmpty()) {
            return null;
        } else {
            return entityManager.find(Account.class, account_id.get(0));
        }
    }

    @Transactional
    public Transfer CreateTransfer(Account transmitter, Account receiver, double amount) {
        receiver.setAccountBalance(receiver.getAccountBalance() + amount);
        transmitter.setAccountBalance(transmitter.getAccountBalance() - amount);
        entityManager.merge(receiver);
        entityManager.merge(transmitter);

        Transfer t = new Transfer();
        t.setAmount(amount);
        t.setDate(new Date());
        t.setReceiver(entityManager.find(Account.class, receiver.getId()));

        t.setTransmitter(entityManager.find(Account.class, transmitter.getId()));

        entityManager.persist(t);

        return t;
    }

    @Transactional
    public ArrayList getTransferbyAccount(Account account) {
        ArrayList<Transfer> array = new ArrayList();
        Query q = entityManager.createQuery("SELECT t.id FROM Transfer as t WHERE t.receiver =:acc or t.transmitter =:acc order by date desc");
        q.setParameter("acc", account);
        //List<Integer> result=q.getResultList();
        List<Long> account_id = q.getResultList();
        for (Long id : account_id) {
            array.add(entityManager.find(Transfer.class, id));
        }
        return array;

    }

    public boolean isTransmitter(Transfer t, Account a) {
        Query q = entityManager.createQuery("SELECT t.id FROM Transfer as t WHERE t.transmitter =:acc and t.id =:id");
        q.setParameter("acc", a);
        q.setParameter("id", t.getId());
        List<Long> id = q.getResultList();
        return !id.isEmpty();
    }
}
