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

/**
 *
 * @author Michael
 */
@Singleton
public class interestService {
    
@Inject
customerService custService;
      
    @Schedule(second="*", minute="*/10", hour="*", persistent=false)
    public void interest() {
        System.out.println("hallo");
        List<Account> acc = custService.allAccounts();
        
        for(Account a : acc){
            if(a.getAccountBalance() < 0){
               
            } 
            else{
            a.setAccountBalance(a.getAccountBalance() + 0.002* a.getAccountBalance());
            custService.updateAccount(a);
            }
        }
        
    }
    
}
