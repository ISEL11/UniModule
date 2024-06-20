package com.example.demo.university.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int semester;
    private int creditPoints;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    @JsonBackReference
    private University university;

    public Module() {
    }

    public Module(int creditPoints, int semester, String name, University university) {
        this.creditPoints = creditPoints;
        this.semester = semester;
        this.name = name;
        this.university = university;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public void setCreditPoints(int creditPoints) {
        this.creditPoints = creditPoints;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSemester() {
        return semester;
    }

    public int getCreditPoints() {
        return creditPoints;
    }

    public University getUniversity() {
        return university;
    }
}