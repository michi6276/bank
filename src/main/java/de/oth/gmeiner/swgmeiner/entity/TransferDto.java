/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.oth.gmeiner.swgmeiner.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Michael
 */
@Entity
public class TransferDto implements Serializable {

    String receiver;
    String transmitter;
    double amount;

    public TransferDto() {
    }

    public TransferDto(String rec, String trans, double amount) {
        this.receiver = rec;
        this.transmitter = trans;
        this.amount = amount;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setTransmitter(String transmitter) {
        this.transmitter = transmitter;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getTransmitter() {
        return transmitter;
    }

    public double getAmount() {
        return amount;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransferDto)) {
            return false;
        }
        TransferDto other = (TransferDto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.oth.gmeiner.swgmeiner.entity.TransferDto[ id=" + id + " ]";
    }

}
