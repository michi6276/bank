package helper;

import de.oth.gmeiner.swgmeiner.service.customerService;
import de.oth.gmeiner.swgmeiner.service.transferService;
import helper.qualifier.OptionAccount;
import helper.qualifier.OptionCustomer;
import helper.qualifier.OptionPromo;
import helper.qualifier.OptionTransfer;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author Michael
 */
@ApplicationScoped
public class Logger {

    @Produces
    @ApplicationScoped
    @OptionCustomer
    public org.apache.logging.log4j.Logger customerLogger() {
        return LogManager.getLogger(customerService.class);
    }

    @Produces
    @ApplicationScoped
    @OptionAccount
    public org.apache.logging.log4j.Logger accountLogger() {
        return LogManager.getLogger(customerService.class);
    }

    @Produces
    @ApplicationScoped
    @OptionTransfer
    public org.apache.logging.log4j.Logger transferLogger() {
        return LogManager.getLogger(transferService.class);
    }
    
      @Produces
    @ApplicationScoped
    @OptionPromo
    public org.apache.logging.log4j.Logger promoLogger() {
        return LogManager.getLogger(customerService.class);
    }

}
