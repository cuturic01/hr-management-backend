package com.example.demo.controller;

import com.example.demo.dto.CandidateDto;
import com.example.demo.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "api/candidate")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @PostMapping(value = "/add-candidate", consumes = "application/json")
    public ResponseEntity<?> addCandidate(@RequestBody @Validated CandidateDto candidateDto) {
        return new ResponseEntity<>(candidateService.addCandidate(candidateDto), HttpStatus.OK);
    }

    @PutMapping(value = "/update-skills/{candidate-id}/{skill-id}", consumes = "application/json")
    public ResponseEntity<?> updateSkills(@PathVariable("candidate-id") Integer candidateId,
                                          @PathVariable("skill-id") Integer skillId) {
        return null;
    }

    @PutMapping(value = "/remove-skills/{candidate-id}/{skill-id}", consumes = "application/json")
    public ResponseEntity<?> removeSkill(@PathVariable("candidate-id") Integer candidateId,
                                          @PathVariable("skill-id") Integer skillId) {
        return null;
    }

    @DeleteMapping(value = "/remove-candidate/{candidate-id}", consumes = "application/json")
    public ResponseEntity<?> removeCandidate(@PathVariable("candidate-id") Integer candidateId) {
        return null;
    }

    @GetMapping(value = "/search-by-name/{candidate-name}", consumes = "application/json")
    public ResponseEntity<?> searchByName(@PathVariable("candidate-name") String candidateName) {
        return null;
    }

    @GetMapping(value = "/search-by-skills", consumes = "application/json")
    public ResponseEntity<?> searchBySkills() {
        return null;
    }

}
