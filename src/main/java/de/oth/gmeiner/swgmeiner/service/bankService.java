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

/**
 *
 * @author Michael
 */
@Singleton
public class bankService {

    @Inject
    customerService custService;
    // Interest
    @Schedule( hour = "*/3", persistent = false)
    public void interest() {

        List<Account> acc = custService.allAccounts();

        for (Account a : acc) {
            if (a.getAccountBalance() > 0) {

                if (a.getAccountType() != null) {
                    double newBalance = a.getAccountBalance() + (a.getAccountType().getInterest() / 100) * a.getAccountBalance();
                    newBalance = newBalance*100;
                    newBalance = Math.round(newBalance);
                    newBalance = newBalance/100;
                    a.setAccountBalance(newBalance);
                    Transfer t = new Transfer();
                    t.setAmount((a.getAccountType().getInterest() / 100) * a.getAccountBalance());
                    t.setDate(new Date());
                    t.setPurpose("Interest");
                    t.setReceiver(a);
                    t.setTransmitter(null);
                    
                    custService.updateAccount(a,t);
                }
            }
        }

    }
    
    // Charges
    @Schedule( hour = "*/24", persistent = false)
    public void charges() {

        List<Account> acc = custService.allAccounts();

        for (Account a : acc) {
            if (a.getAccountBalance() > 0) {

                if (a.getAccountType() != null) {
                    a.setAccountBalance(a.getAccountBalance() - a.getAccountType().getCharges());
                     Transfer t = new Transfer();
                    t.setAmount(a.getAccountType().getCharges());
                    t.setDate(new Date());
                    t.setPurpose("Charges");
                    t.setTransmitter(a);
                    t.setReceiver(null);
                    custService.updateAccount(a,t);
                }
            }
        }

    }

}
