package de.oth.gmeiner.swgmeiner.model;

import de.oth.gmeiner.swgmeiner.entity.Account;
import de.oth.gmeiner.swgmeiner.entity.Customer;
import de.oth.gmeiner.swgmeiner.entity.Address;
import de.oth.gmeiner.swgmeiner.entity.Transfer;
import de.oth.gmeiner.swgmeiner.entity.TransferDto;
import de.oth.gmeiner.swgmeiner.entity.Util;
import de.oth.gmeiner.swgmeiner.service.customerService;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import de.oth.gmeiner.swgmeiner.model.customerModel;
import de.oth.gmeiner.swgmeiner.service.transferService;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Named
@SessionScoped
public class transferModel implements Serializable {

    Account receiver;
    Account transmitter;
    Transfer transfer;
    TransferDto dto;
    Customer receiver_c;
    Customer transmitter_c;
    double amount;
    String iban;
    String purpose;
    Date date;
    ArrayList<Transfer> transfers;

    @Inject
    private customerModel customerModel;
    @Inject
    private transferService transferService;
    @Inject
    private customerService customerService;

    public String checkIban() {

        this.receiver_c = transferService.checkIban(this.iban);
        this.receiver = transferService.getAccountbyIban(this.iban);

        this.transmitter_c = customerModel.getCustomer();
        this.transmitter = customerModel.getCurrent_account();

        return "transfer";
    }

    public String createTransfer() {
        if (this.receiver_c == null) {
            FacesContext.getCurrentInstance().addMessage("registerForm:ibanVal", new FacesMessage("Check IBAN first!"));
            return "transfer";
        }
        this.transfer = transferService.CreateTransfer(transmitter.getIban(), receiver.getIban(), this.amount, this.purpose);
        if (this.transfer == null) {
            FacesContext.getCurrentInstance().addMessage("registerForm:transferVal", new FacesMessage("Not enough money!"));
            return "transfer";
        }
        this.receiver_c = null;
        return "home";
    }
    
     public String toTransfer() {
        this.receiver = null;
        this.receiver_c = null;
        this.iban = null;
        return "transfer";
    }

    public String isTransmitter(Transfer t, Account a) {
        if (transferService.isTransmitter(t, a)) {
            return "-";
        } else {
            return "+";
        }
    }

    public ArrayList transferbyAccount() {
        return transferService.getTransferbyAccount(customerModel.getCurrent_account());
    }

    public Account getReceiver() {
        return receiver;
    }

    public void setReceiver(Account receiver) {
        this.receiver = receiver;
    }

    public Account getTransmitter() {
        return transmitter;
    }

    public void setTransmitter(Account transmitter) {
        this.transmitter = transmitter;
    }

    public Customer getReceiver_c() {
        return receiver_c;
    }

    public void setReceiver_c(Customer receiver_c) {
        this.receiver_c = receiver_c;
    }

    public Customer getTransmitter_c() {
        return transmitter_c;
    }

    public void setTransmitter_c(Customer transmitter_c) {
        this.transmitter_c = transmitter_c;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public transferService getTransferService() {
        return transferService;
    }

    public void setTransferService(transferService transferService) {
        this.transferService = transferService;
    }

    public Transfer getTransfer() {
        return transfer;
    }

    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
    }

    public TransferDto getDto() {
        return dto;
    }

    public void setDto(TransferDto dto) {
        this.dto = dto;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

}
