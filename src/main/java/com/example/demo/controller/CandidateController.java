package com.example.demo.controller;

import com.example.demo.dto.CandidateDto;
import com.example.demo.dto.SkillDto;
import com.example.demo.exception.AlreadyHasSkillException;
import com.example.demo.exception.CandidateDoesNotExistException;
import com.example.demo.exception.DoesNotHaveSkillException;
import com.example.demo.exception.SkillDoesNotExistException;
import com.example.demo.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping(value = "/update-skills/{candidate-id}/{skill-id}")
    public ResponseEntity<?> updateSkills(@PathVariable("candidate-id") Integer candidateId,
                                          @PathVariable("skill-id") Integer skillId) {
        try {
            return new ResponseEntity<>(this.candidateService.updateSkills(candidateId, skillId), HttpStatus.OK);
        } catch (CandidateDoesNotExistException | SkillDoesNotExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AlreadyHasSkillException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/remove-skills/{candidate-id}/{skill-id}")
    public ResponseEntity<?> removeSkill(@PathVariable("candidate-id") Integer candidateId,
                                          @PathVariable("skill-id") Integer skillId) {
        try {
            return new ResponseEntity<>(this.candidateService.removeSkill(candidateId, skillId), HttpStatus.OK);
        } catch (CandidateDoesNotExistException | SkillDoesNotExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DoesNotHaveSkillException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/remove-candidate/{candidate-id}")
    public ResponseEntity<?> removeCandidate(@PathVariable("candidate-id") Integer candidateId) {
        try {
            this.candidateService.removeCandidate(candidateId);
            return new ResponseEntity<>("Candidate removed!", HttpStatus.OK);
        } catch (CandidateDoesNotExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/search-by-name/{candidate-name}")
    public ResponseEntity<?> searchByName(@PathVariable("candidate-name") String candidateName) {
        try {
            return new ResponseEntity<>(this.candidateService.findByName(candidateName), HttpStatus.OK);
        } catch (CandidateDoesNotExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/search-by-skills", consumes = "application/json")
    public ResponseEntity<?> searchBySkills(@RequestBody List<SkillDto> skills) {
        try {
            return new ResponseEntity<>(this.candidateService.findBySkills(skills), HttpStatus.OK);
        } catch (SkillDoesNotExistException | CandidateDoesNotExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
