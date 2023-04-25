package com.example.demo.controller;

import com.example.demo.dto.SkillDto;
import com.example.demo.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "api/skill")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @PostMapping(value = "/add-skill", consumes = "application/json")
    public ResponseEntity<?> addSkill(@RequestBody @Validated SkillDto skillDto) {
        return new ResponseEntity<>(this.skillService.addSkill(skillDto), HttpStatus.OK);
    }

}
