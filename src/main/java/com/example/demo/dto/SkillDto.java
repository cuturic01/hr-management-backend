package com.example.demo.dto;

import com.example.demo.model.Skill;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class SkillDto {

    private Integer id;

    @NotNull
    @Length(min = 2, max = 50)
    private String name;

    public SkillDto() {}

    public SkillDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public SkillDto(Skill skill) {
        this.id = skill.getId();
        this.name = skill.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
