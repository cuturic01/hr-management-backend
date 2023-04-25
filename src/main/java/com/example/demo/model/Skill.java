package com.example.demo.model;

import com.example.demo.dto.SkillDto;
import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "skill_name", unique = true)
    @Length(min = 2, max = 50)
    private String name;

    // region Constructors

    public Skill() {}

    public Skill(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Skill(SkillDto skillDto) {
        this.name = skillDto.getName();
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

    // endregion
}
