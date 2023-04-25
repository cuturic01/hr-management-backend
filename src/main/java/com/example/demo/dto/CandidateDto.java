package com.example.demo.dto;

import com.example.demo.model.Candidate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class CandidateDto {

    private Integer id;

    @Length(min = 2, max = 50)
    @Pattern(regexp = "^[A-Za-z ]*$", message = "The name can only contain letters.")
    @Schema(example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private String name;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Schema(example = "01-01-2001", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate dateOfBirth;

    @Pattern(regexp = "^[^a-zA-Z]*$")
    @Schema(example = "+38162547126", accessMode = Schema.AccessMode.READ_ONLY)
    private String contactNumber;

    @Email
    @Schema(example = "example@email.com", accessMode = Schema.AccessMode.READ_ONLY)
    private String email;

    private List<SkillDto> skills;

    // region Constructors

    public CandidateDto() {}

    public CandidateDto(Integer id, String name, LocalDate dateOfBirth, String contactNumber,
                        String email, List<SkillDto> skills) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.contactNumber = contactNumber;
        this.email = email;
        this.skills = skills;
    }

    public CandidateDto(Candidate candidate) {
        this.id = candidate.getId();
        this.name = candidate.getName();
        this.dateOfBirth = candidate.getDateOfBirth();
        this.contactNumber = candidate.getContactNumber();
        this.email = candidate.getEmail();
        this.skills = candidate.getSkills().stream().map(SkillDto::new).collect(Collectors.toList());
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

    public List<SkillDto> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillDto> skills) {
        this.skills = skills;
    }


    // endregion
}
