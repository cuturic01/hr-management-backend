package com.example.demo.service;

import com.example.demo.dto.CandidateDto;
import com.example.demo.dto.SkillDto;
import com.example.demo.exception.AlreadyHasSkillException;
import com.example.demo.exception.CandidateDoesNotExistException;
import com.example.demo.exception.DoesNotHaveSkillException;
import com.example.demo.exception.SkillDoesNotExistException;
import com.example.demo.model.Candidate;
import com.example.demo.model.Skill;
import com.example.demo.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public CandidateDto updateSkills(Integer candidateId, Integer skillId)
            throws CandidateDoesNotExistException, SkillDoesNotExistException, AlreadyHasSkillException {
        Candidate candidate = this.findById(candidateId);
        Skill skill = this.skillService.findById(skillId);
        if (candidate.getSkills().contains(skill))
            throw new AlreadyHasSkillException("Candidate already has this skill!");
        candidate.getSkills().add(skill);
        Candidate newCandidate = candidateRepository.save(candidate);
        return new CandidateDto(newCandidate);
    }

    public Candidate findById(Integer id) throws CandidateDoesNotExistException {
        return this.candidateRepository.findById(id)
                .orElseThrow(() -> new CandidateDoesNotExistException("Candidate does not exist!"));
    }

    public CandidateDto removeSkill(Integer candidateId, Integer skillId)
            throws CandidateDoesNotExistException, SkillDoesNotExistException, DoesNotHaveSkillException {
        Candidate candidate = this.findById(candidateId);
        Skill skill = this.skillService.findById(skillId);
        if (!candidate.getSkills().contains(skill))
            throw new DoesNotHaveSkillException("Candidate does not have this skill!");
        candidate.getSkills().removeIf(s -> s.getId().equals(skill.getId()));
        Candidate newCandidate = this.candidateRepository.save(candidate);
        return new CandidateDto(newCandidate);
    }

    public void removeCandidate(Integer id) throws CandidateDoesNotExistException {
        this.candidateRepository.delete(this.findById(id));
    }

    public CandidateDto findByName(String name) throws CandidateDoesNotExistException {
        return new CandidateDto(this.candidateRepository.findByName(name)
                .orElseThrow(() -> new CandidateDoesNotExistException("Candidate does not exist!")));
    }

    public List<CandidateDto> findBySkills(List<SkillDto> skills) throws SkillDoesNotExistException {
        for (SkillDto skillDto : skills)
            if (skillService.findById(skillDto.getId()) == null)
                throw  new SkillDoesNotExistException("Skill does not exist!");

        List<Candidate> candidates = this.candidateRepository.findBySkills(skills
                        .stream()
                        .map(SkillDto::getId)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new SkillDoesNotExistException("Skill does not exist!"));
        return candidates.stream().map(CandidateDto::new).collect(Collectors.toList());
    }

}
