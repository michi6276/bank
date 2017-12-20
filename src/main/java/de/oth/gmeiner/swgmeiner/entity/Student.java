/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.oth.gmeiner.swgmeiner.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Jon
 */
@Entity
public class Student {

    public Student() {
    }
    private String vorname;
    private String nachname;
    @Id
    private long matrikelNr;

    public Student(String vorname, String nachname, long matrikelNr) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.matrikelNr = matrikelNr;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public long getMatrikelNr() {
        return matrikelNr;
    }

    public void setMatrikelNr(long matrikelNr) {
        this.matrikelNr = matrikelNr;
    }

}
