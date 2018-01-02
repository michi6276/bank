package de.oth.gmeiner.swgmeiner.model;

import Converter.AccountTypeConverter;
import de.oth.gmeiner.swgmeiner.entity.Account;
import de.oth.gmeiner.swgmeiner.entity.AccountType;
import de.oth.gmeiner.swgmeiner.entity.Customer;
import de.oth.gmeiner.swgmeiner.entity.Address;
import de.oth.gmeiner.swgmeiner.entity.Util;
import de.oth.gmeiner.swgmeiner.service.PromoService;
import de.oth.gmeiner.swgmeiner.service.customerService;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

@Named
@SessionScoped
public class customerModel implements Serializable {

    //Customer
    private String surname = "";
    private String prename = "";
    private String username = "";
    private String email = "";
    private String password = "";
    private Customer customer;
    private double amount;
    //Address
    private String street = "";
    private String city = "";
    private String country = "";
    private String postalCode;
    //Account
    ArrayList<Account> account;
    private Account current_account;
    private String iban = "";
    private long accountNr = 0;
    private long accountCode = 0;
    private double accountBalance = 0.0;
    private AccountType selectedType;
    @Inject
    AccountTypeConverter accountTypeConverter;
    @Inject
    private PromoService promoService;

        public void updateBalance() {
        
        List<Account> acc = customerService.allAccounts();
        
        for(Account a : acc){
            a.setAccountBalance(a.getAccountBalance() + 2* a.getAccountBalance());
            customerService.updateAccount(a);
        }
        
    }
    
    
    public Collection<AccountType> allTypes(){
        return this.customerService.allTypes();
    }
     public String loginCustomer() {
        if(this.email.equals("admin") && this.email.equals("admin")){
            return "admin";
        }
        this.customer = customerService.login(this.email, this.password);
        if (this.customer != null) {
            HttpSession session = Util.getSession();

            session.setAttribute("user", this.customer);
            return "home";
        } else {
            this.password = "";
            //FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid Login!", "Please try again!"));
            return "login";

        }
    }

    public String logout() {
        HttpSession session = Util.getSession();

        session.setAttribute("user", null);
        this.current_account = null;
        this.customer = null;
        this.account = null;

        return "login";
    }

    public ArrayList getAccountbyC() {
        this.account = customerService.getAccountbyCustomer(this.customer);
        return account;
    }

    public String depositMoney() {
        if (this.current_account != null) {
            customerService.depositMoney(this.current_account, this.amount);
            this.current_account = null;
            return "home.xhtml";
        } else {
            return "home";
        }
    }

    public String moneyPayout() {
        if (this.current_account != null) {
            customerService.moneyPayout(this.current_account, this.amount);
            this.current_account = null;
            return "home.xhtml";
        } else {
            return "home";
        }
    }

    public String verifyCustomer() {

        Address a = new Address();
        a.setCity(this.city);
        a.setCountry(this.country);
        a.setPostCode(this.postalCode);
        a.setStreet(this.street);

        Customer c = new Customer();
        c.setPrename(this.prename);
        c.setSurname(this.surname);
        c.setEmail(this.email);
        c.setUsername(this.username);
        c.setPassword(this.password);
        c.setAddress(a);

        this.customer = customerService.signup(c);

        return "login";
    }

    public void cleanAttributs() {
        this.prename = "";
        this.surname = "";
        this.password = "";
        this.email = "";
    }

    public String newAccount() {
        Account a = new Account();
        int i = new Random().nextInt(9999999);
        String x = "DE" + new Random().nextInt(9999999) + new Random().nextInt(9999999);
        a.setAccountBalance(0);
        a.setAccountCode(753000);
        a.setAccountNr(i);
        a.setIban(x);
        a.setDate(new Date());
        a.setAccountType(this.selectedType);
        System.out.println("Seeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeelected Tyyyyyyyyyyyyyyyyyyyyyyyyype" +this.getSelectedType());
       
        // this.accountBalance = 0.0;
        //this.accountNr =i;
        //this.accountCode = 753000;
        //this.iban = x;
        this.current_account = a;
        //this.account.add(0, customerService.createAccount(a, this.customer));

        return "newAccount";
    }
    public String createNewAccount(){
      
        this.account.add(0, customerService.createAccount(current_account, this.customer,this.getSelectedType()));
        return "home";
    }

    public String checkBankBalance() {

        String output = "";
        for (Account acc : account) {
            output = output + acc.getIban() + "   :   " + acc.getAccountBalance() + "\n\n";
        }
        if (output == "") {
            return "No Account for this Customer";
        } else {
            return output;
        }
    }

    public String deleteAccount() {

        customerService.deleteAccount(this.current_account);
        return "home";
    }

    public String getPromoCode() {
        return promoService.getPromoCode();

    }
    
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Account getCurrent_account() {
        return current_account;
    }

    public String setCurrent_account(Account current_account) {
        this.current_account = current_account;
        return "Account";
    }

    public ArrayList getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account.add(account);
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public long getAccountNr() {
        return accountNr;
    }

    public void setAccountNr(long accountNr) {
        this.accountNr = accountNr;
    }

    public long getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(long accountCode) {
        this.accountCode = accountCode;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    @Inject
    private customerService customerService;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPrename() {
        return prename;
    }

    public void setPrename(String prename) {
        this.prename = prename;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String PostalCode) {
        this.postalCode = PostalCode;
    }

    public customerService getCustomerService() {
        return customerService;
    }

    public void setCustomerService(customerService customerService) {
        this.customerService = customerService;
    }

    public AccountType getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(AccountType selectedType) {
        this.selectedType = selectedType;
    }

    public AccountTypeConverter getAccountTypeConverter() {
        return accountTypeConverter;
    }

    public void setAccountTypeConverter(AccountTypeConverter AccountTypeConverter) {
        this.accountTypeConverter = AccountTypeConverter;
    }
    
    
 

}
