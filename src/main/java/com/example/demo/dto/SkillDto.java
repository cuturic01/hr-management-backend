package com.example.demo.dto;

import com.example.demo.model.Skill;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class SkillDto {

    @NotNull
    @Length(min = 2, max = 50)
    private String name;

    public SkillDto() {}

    public SkillDto(String name) {
        this.name = name;
    }

    public SkillDto(Skill skill) {
        this.name = skill.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
