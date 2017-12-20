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
@WebService(serviceName = "PromoService")
public class PromoService {

    public String getPromoCode() {

        return "hallo";
    }

}
