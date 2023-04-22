package com.example.demo.service;

import com.example.demo.dto.SkillDto;
import com.example.demo.model.Skill;
import com.example.demo.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    public SkillDto addSkill(SkillDto skillDto) {
        Skill skill = skillRepository.save(new Skill(skillDto));
        return new SkillDto(skill);
    }

    public Skill findByName(String name) {
        return skillRepository.findByName(name);
    }

}
