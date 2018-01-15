package de.oth.gmeiner.swgmeiner.model;

import Converter.AccountTypeConverter;
import de.oth.gmeiner.swgmeiner.entity.Account;
import de.oth.gmeiner.swgmeiner.entity.AccountType;
import de.oth.gmeiner.swgmeiner.entity.Customer;
import de.oth.gmeiner.swgmeiner.entity.Address;
import de.oth.gmeiner.swgmeiner.entity.Util;
import de.oth.gmeiner.swgmeiner.service.PromoService;
import de.oth.gmeiner.swgmeiner.service.customerService;
import de.oth.gmeiner.swgmeiner.service.transferService;
import helper.BCrypt;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

@Named
@SessionScoped
public class customerModel implements Serializable {

    //Customer
    private String surname = "";
    private String prename = "";
    private String email = "";
    private String password = "";
    private String password2 = "";
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
    private AccountTypeConverter accountTypeConverter;
    @Inject
    private PromoService promoService;

    private Matcher matcher;
    private Pattern pattern;
    private static final String EMAIL_FORM = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public Collection<AccountType> allTypes() {
        return this.customerService.allTypes();
    }

    public String loginCustomer() {

        if (this.email.equals("admin") && this.email.equals("admin")) {
            return "admin";
        }
        this.customer = customerService.login(this.email, this.password);
        if (this.customer != null) {
            HttpSession session = Util.getSession();

            session.setAttribute("user", this.customer);
            return "home";
        } else {
            FacesContext.getCurrentInstance().addMessage("registerForm:loginVal", new FacesMessage("Password or E-mail are not correct"));
            this.password = "";

            return "login";

        }
    }
   

    public String logout() {

        HttpSession session = Util.getSession();
        session.invalidate();
        this.customer = null;
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
        if (this.city.equals("") || this.password2.equals("") || this.country.equals("") || this.postalCode.equals("") || this.street.equals("") || this.prename.equals("") || this.surname.equals("") || this.email.equals("") || this.password.equals("")) {
            FacesContext.getCurrentInstance().addMessage("registerForm:registerVal", new FacesMessage("Please fill all fields"));
        } else {
            pattern = Pattern.compile(EMAIL_FORM);
            matcher = pattern.matcher(this.email);
            if (matcher.matches()) {
                if (this.password.equals(this.password2)) {
                    Address a = new Address();
                    a.setCity(this.city);
                    a.setCountry(this.country);
                    a.setPostCode(this.postalCode);
                    a.setStreet(this.street);

                    Customer c = new Customer();
                    c.setPrename(this.prename);
                    c.setSurname(this.surname);
                    c.setEmail(this.email);
                    
                    c.setPassword(customerService.hashPassword(this.password));
                    c.setAddress(a);

                    this.customer = customerService.signup(c);
                    if (this.customer != null) {
                        return "home";
                    } else {
                        FacesContext.getCurrentInstance().addMessage("registerForm:EmailVal", new FacesMessage("E-mail already in use"));
                        return "signup";
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage("registerForm:PasswordVal", new FacesMessage("Passwords do not match!"));
                    return "signup";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage("registerForm:EmailFalse", new FacesMessage("Email is not in the correct form!"));
            }
        }
        return "signup";
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
        a.setAccountBalance(1000);
        a.setAccountCode(753000);
        a.setAccountNr(i);
        a.setIban(x);
        a.setDate(new Date());
        a.setAccountType(this.selectedType);

        this.current_account = a;
        return "newAccount";
    }

    public String createNewAccount() {

        this.account.add(0, customerService.createAccount(current_account, this.customer, this.getSelectedType()));
        return "promoCode";
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

        customerService.deleteAccount(this.current_account, transferService.getTransferbyAccount(current_account));
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
    @Inject
    private transferService transferService;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getPrename() {
        return prename;
    }

    public void setPrename(String prename) {
        this.prename = prename;
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

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

}
