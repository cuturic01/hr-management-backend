package com.example.demo.controller;

import com.example.demo.dto.CandidateDto;
import com.example.demo.dto.SkillDto;
import com.example.demo.exception.AlreadyHasSkillException;
import com.example.demo.exception.CandidateDoesNotExistException;
import com.example.demo.exception.DoesNotHaveSkillException;
import com.example.demo.exception.SkillDoesNotExistException;
import com.example.demo.service.CandidateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/candidate")
@Tag(name = "Candidates", description = "Endpoints dealing with candidates.")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @Operation(summary = "Add a candidate", description = "Add a candidate")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CandidateDto.class))
    })
    @PostMapping(value = "/add-candidate", consumes = "application/json")
    public ResponseEntity<?> addCandidate(@RequestBody  @Validated CandidateDto candidateDto) {
        return new ResponseEntity<>(candidateService.addCandidate(candidateDto), HttpStatus.OK);
    }

    @Operation(summary = "Add a skill to a candidate", description = "Update candidate skill list with a new skill.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CandidateDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Candidate already has that skill!", content = {
                    @Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "Candidate does not exist!", content = {
                    @Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "Skill does not exist!", content = {
                    @Content(mediaType = "text/plain")})
    })
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

    @Operation(summary = "Remove a skill from the job candidate.", description = "Remove a skill from the job candidate.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CandidateDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Candidate does not have that skill!", content = {
                    @Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "Candidate does not exist!", content = {
                    @Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "Skill does not exist!", content = {
                    @Content(mediaType = "text/plain")})
    })
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

    @Operation(summary = "Remove a candidate.", description = "Remove a candidate from the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "text/plain")
            }),
            @ApiResponse(responseCode = "404", description = "Candidate does not exist does not exist!", content = {
                    @Content(mediaType = "text/plain")})
    })
    @DeleteMapping(value = "/remove-candidate/{candidate-id}")
    public ResponseEntity<?> removeCandidate(@PathVariable("candidate-id") Integer candidateId) {
        try {
            this.candidateService.removeCandidate(candidateId);
            return new ResponseEntity<>("Candidate removed!", HttpStatus.OK);
        } catch (CandidateDoesNotExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Search by name.", description = "Search for a candidate by full name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CandidateDto.class))
            }),
            @ApiResponse(responseCode = "404", description = "Candidate does not exist does not exist!", content = {
                    @Content(mediaType = "text/plain")})
    })
    @GetMapping(value = "/search-by-name/{candidate-name}")
    public ResponseEntity<?> searchByName(@PathVariable("candidate-name") String candidateName) {
        try {
            return new ResponseEntity<>(this.candidateService.findByName(candidateName), HttpStatus.OK);
        } catch (CandidateDoesNotExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Search by skills.", description = "Search for candidates by skills.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
            }),
            @ApiResponse(responseCode = "404", description = "Candidate does not exist does not exist!", content = {
                    @Content(mediaType = "text/plain")})
    })
    @GetMapping(value = "/search-by-skills", consumes = "application/json")
    public ResponseEntity<?> searchBySkills(@RequestBody List<SkillDto> skills) {
        try {
            return new ResponseEntity<>(this.candidateService.findBySkills(skills), HttpStatus.OK);
        } catch (SkillDoesNotExistException | CandidateDoesNotExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
