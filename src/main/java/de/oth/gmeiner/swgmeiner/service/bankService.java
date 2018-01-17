/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.oth.gmeiner.swgmeiner.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import de.oth.gmeiner.swgmeiner.entity.Account;
import de.oth.gmeiner.swgmeiner.entity.Transfer;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *
 * @author Michael
 */
@Singleton
public class bankService {

    @Inject
    customerService custService;

    @PersistenceContext(unitName = "SWGmeiner_pu")
    private EntityManager entityManager;

    // Interest
    @Transactional
    @Schedule(hour = "*/6", persistent = false)
    public void interest() {
        List<Account> acc = custService.allAccounts();
        for (Account a : acc) {
            if (a.getAccountBalance() > 0) {
                if (a.getAccountType() != null) {
                    double interest = (a.getAccountType().getInterest() / 100) * a.getAccountBalance();
                    interest = interest * 100;
                    interest = Math.round(interest);
                    interest = interest / 100;
                    a.setAccountBalance(a.getAccountBalance() + interest);
                    Transfer t = new Transfer();
                    t.setAmount(interest);
                    t.setDate(new Date());
                    t.setPurpose("Interest");
                    t.setReceiver(a);
                    t.setTransmitter(null);
                    entityManager.merge(a);
                    entityManager.merge(t);
                }
            }
        }

    }

    // Charges
    @Transactional
    @Schedule(hour = "*/24", persistent = false)
    public void charges() {
        List<Account> acc = custService.allAccounts();
        for (Account a : acc) {
            if (a.getAccountBalance() > 0) {
                if (a.getAccountType() != null) {
                    double charges = a.getAccountType().getCharges();
                    charges = charges * 100;
                    charges = Math.round(charges);
                    charges = charges / 100;
                    a.setAccountBalance(a.getAccountBalance() - charges);
                    Transfer t = new Transfer();
                    t.setAmount(charges);
                    t.setDate(new Date());
                    t.setPurpose("Charges");
                    t.setTransmitter(a);
                    t.setReceiver(null);
                    entityManager.merge(a);
                    entityManager.merge(t);
                }
            }
        }

    }

}
