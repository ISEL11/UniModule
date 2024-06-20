package com.example.demo.university.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;


import java.util.List;

@Entity
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String country;
    private String department;
    private String website;
    private String contactPerson;
    private int incomingStudents;
    private int outgoingStudents;
    private String springSemesterStart;
    private String autumnSemesterStart;

    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Module> modules;

    public University() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public void setIncomingStudents(int incomingStudents) {
        this.incomingStudents = incomingStudents;
    }

    public void setOutgoingStudents(int outgoingStudents) {
        this.outgoingStudents = outgoingStudents;
    }

    public void setSpringSemesterStart(String springSemesterStart) {
        this.springSemesterStart = springSemesterStart;
    }

    public void setAutumnSemesterStart(String autumnSemesterStart) {
        this.autumnSemesterStart = autumnSemesterStart;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getDepartment() {
        return department;
    }

    public String getWebsite() {
        return website;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public int getIncomingStudents() {
        return incomingStudents;
    }

    public int getOutgoingStudents() {
        return outgoingStudents;
    }

    public String getSpringSemesterStart() {
        return springSemesterStart;
    }

    public String getAutumnSemesterStart() {
        return autumnSemesterStart;
    }

    public List<Module> getModules() {
        return modules;
    }
}