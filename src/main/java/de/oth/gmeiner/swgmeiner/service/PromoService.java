/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.oth.gmeiner.swgmeiner.service;

import de.oth.gmeiner.swgmeiner.entity.AccountType;
import helper.PromoCode;
import helper.PromoCodeService;
import helper.PromoCodeServiceService;
import helper.qualifier.OptionCustomer;
import helper.qualifier.OptionPromo;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.xml.ws.WebServiceRef;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Michael
 */
@RequestScoped
@WebService(serviceName = "PromoService")
public class PromoService {
    
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/im-lamport.oth-regensburg.de_8080/BikeRentalWeidner-0.1/PromoCodeService.wsdl")
    private PromoCodeServiceService service;
    
    @Inject
    @OptionPromo
    private Logger logger;
    
    public String getPromoCode(AccountType a) {
        
        try {
            PromoCodeService port = service.getPromoCodeServicePort();
            PromoCode result = port.generatePromoCode("Michael_Gmeiner", "Bank123", a.getPromoValue());
            return result.getPromoCodeName();
        } catch (Exception ex) {
            //throw new RuntimeException("Error, Could not generate promoCode" + ex.getMessage(), ex);
            logger.error("PromoCode Service not available");
            return "PromoCode Service currently not available";
        }
        
    }
    
}
