/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.oth.gmeiner.swgmeiner.service;

import de.oth.gmeiner.swgmeiner.entity.Customer;
import de.oth.gmeiner.swgmeiner.entity.Transfer;
import javax.enterprise.context.RequestScoped;
import javax.jws.WebService;

/**
 *
 * @author Michael
 */

@RequestScoped
@WebService
public interface transferServiceIF {

    public Transfer CreateTransfer(String t, String r, double amount, String purpose);

    public Customer checkIban(String iban);
}
