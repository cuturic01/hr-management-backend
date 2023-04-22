package com.example.demo.service;

import com.example.demo.dto.CandidateDto;
import com.example.demo.model.Candidate;
import com.example.demo.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private SkillService skillService;

    public CandidateDto addCandidate(CandidateDto candidateDto) {
        Candidate candidate = new Candidate(candidateDto);
        candidate.setSkills(candidateDto.getSkills()
                .stream()
                .map(skillDto -> this.skillService.findByName(skillDto.getName()))
                .collect(Collectors.toList()));
        candidateRepository.save(candidate);
        return new CandidateDto(candidate);
    }

}
