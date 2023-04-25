package com.example.demo.controller;

import com.example.demo.dto.SkillDto;
import com.example.demo.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "api/skill")
@Tag(name = "Skills", description = "Endpoints dealing with skills.")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @Operation(summary = "Add a skill", description = "Add a skill")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = SkillDto.class))
    })
    @PostMapping(value = "/add-skill", consumes = "application/json")
    public ResponseEntity<?> addSkill(@RequestBody @Validated SkillDto skillDto) {
        return new ResponseEntity<>(this.skillService.addSkill(skillDto), HttpStatus.OK);
    }

}
