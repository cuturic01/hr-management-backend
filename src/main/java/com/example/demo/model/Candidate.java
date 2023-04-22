package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.Pattern;


import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "candidate_name")
    @Length(min = 2, max = 50)
    @Pattern(regexp = "^[A-Za-z ]*$", message = "The name can only contain letters.")
    private String name;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    @Pattern(regexp = "^[^a-zA-Z]*$")
    private String contactNumber;

    @Column(nullable = false)
    @Email
    private String email;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinTable(name = "candidate_skill", joinColumns = {
            @JoinColumn(name = "candidate_id", referencedColumnName = "id") }, inverseJoinColumns = {
            @JoinColumn(name = "skill_id", referencedColumnName = "id") })
    private List<Skill> skills;

    // region Constructors

    public Candidate() {}

    public Candidate(Integer id, String name, LocalDate dateOfBirth, String contactNumber,
                     String email, List<Skill> skills) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.contactNumber = contactNumber;
        this.email = email;
        this.skills = skills;
    }

    // endregion

    // region Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    // endregion
}
