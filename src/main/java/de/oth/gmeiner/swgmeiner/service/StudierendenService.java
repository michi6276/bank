/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.oth.gmeiner.swgmeiner.service;

import de.oth.gmeiner.swgmeiner.entity.Student;
import java.util.Random;
import javax.enterprise.context.RequestScoped;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *
 * @author Jon
 */
@RequestScoped
public class StudierendenService {
    @PersistenceContext(unitName="SWGmeiner_pu")
    private EntityManager entityManager;
    @Transactional
    public Student immatrikulieren(Student student) {
        student.setMatrikelNr(new Random().nextInt(99999999));
        entityManager.persist(student);
        return student;
    }
}
