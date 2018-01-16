/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.oth.gmeiner.swgmeiner.service;

import de.oth.gmeiner.swgmeiner.entity.Account;
import de.oth.gmeiner.swgmeiner.entity.Customer;
import de.oth.gmeiner.swgmeiner.entity.Transfer;
import helper.qualifier.OptionTransfer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
@WebService(serviceName = "transferService")
public class transferService {

    @Inject
    @OptionTransfer
    private Logger logger;

    @PersistenceContext(unitName = "SWGmeiner_pu")
    private EntityManager entityManager;

    @Transactional
    public Customer checkIban(String iban) {
        Customer c;
        Query q = entityManager.createQuery("SELECT c.id FROM Customer as c WHERE c.id=(Select a.customer FROM Account as a WHERE a.iban=:iban)");
        q.setParameter("iban", iban);
        //List<Integer> result=q.getResultList();
        List<Long> customer = q.getResultList();
        if (customer.isEmpty()) {
            return null;
        }
        c = entityManager.find(Customer.class, customer.get(0));
        return c;
    }

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
        List<Long> account_id = q.getResultList();
        if (account_id.isEmpty()) {
            return null;
        } else {
            return entityManager.find(Account.class, account_id.get(0));
        }
    }

    @Transactional
    public Transfer CreateTransfer(String t, String r, double amount, String purpose) {
        Account transmitter = this.getAccountbyIban(t);
        Account receiver = this.getAccountbyIban(r);
        if (r != null && t != null) {
            if (transmitter.getAccountBalance() >= amount) {
                receiver.setAccountBalance(receiver.getAccountBalance() + amount);
                transmitter.setAccountBalance(transmitter.getAccountBalance() - amount);
                entityManager.merge(receiver);
                entityManager.merge(transmitter);
                Transfer tr = new Transfer();
                tr.setAmount(amount);
                tr.setDate(new Date());
                tr.setPurpose(purpose);
                tr.setReceiver(entityManager.find(Account.class, receiver.getId()));
                tr.setTransmitter(entityManager.find(Account.class, transmitter.getId()));
                entityManager.persist(tr);
                logger.info("new Transfer created : " + tr.getId());
                return tr;
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    @Transactional
    public ArrayList getTransferbyAccount(Account account) {
        ArrayList<Transfer> array = new ArrayList();
        Query q = entityManager.createQuery("SELECT t.id FROM Transfer as t WHERE t.receiver =:acc or t.transmitter =:acc order by date desc");
        q.setParameter("acc", account);
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
