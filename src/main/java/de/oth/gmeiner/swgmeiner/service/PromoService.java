/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.oth.gmeiner.swgmeiner.service;

import de.oth.gmeiner.swgmeiner.entity.Account;
import de.oth.gmeiner.swgmeiner.entity.Customer;
import de.oth.gmeiner.swgmeiner.entity.Transfer;
import helper.PromoCode;
import helper.PromoCodeService;
import helper.PromoCodeServiceService;
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
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Michael
 */
@RequestScoped
@WebService(serviceName = "PromoService")
public class PromoService {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/im-lamport.oth-regensburg.de_8080/BikeRentalWeidner-0.1/PromoCodeService.wsdl")
    private PromoCodeServiceService service;

    public String getPromoCode() {

        
        try { // Call Web Service Operation
          PromoCodeService port = service.getPromoCodeServicePort();
            // TODO initialize WS operation arguments here
            
            // TODO process result here
            PromoCode result = port.generatePromoCode("Michael_Gmeiner","Bank123", 10);
            System.out.println("Result = "+result);
            return result.getPromoCodeName();
            
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
        return null;
    }

}
