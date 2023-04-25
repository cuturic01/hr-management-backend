package com.example.demo.service;

import com.example.demo.HrManagementBackend;
import com.example.demo.dto.CandidateDto;
import com.example.demo.dto.SkillDto;
import com.example.demo.exception.AlreadyHasSkillException;
import com.example.demo.exception.CandidateDoesNotExistException;
import com.example.demo.exception.DoesNotHaveSkillException;
import com.example.demo.exception.SkillDoesNotExistException;
import com.example.demo.model.Candidate;
import com.example.demo.model.Skill;
import com.example.demo.repository.CandidateRepository;
import com.example.demo.repository.SkillRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HrManagementBackend.class)
public class CandidateServiceTest {

    @Autowired
    private CandidateService candidateService;

    @MockBean
    private SkillService skillService;

    @MockBean
    private CandidateRepository candidateRepository;

    private Candidate createDummyCandidate() {
        List<Skill> skills = new ArrayList<>();

        return new Candidate(
                1,
                "John Doe",
                LocalDate.of(1990, 1, 1),
                "555-555-5555",
                "john.doe@example.com",
                skills
        );
    }

    private Skill createDummySkill() {
        return new Skill(1, "Java");
    }

    private SkillDto createDummySkillDto() {
        return new SkillDto(1, "Java");
    }

    @Test
    public void testAddCandidate() {
        Candidate candidate = createDummyCandidate();
        Skill skill = createDummySkill();
        candidate.getSkills().add(skill);

        when(candidateRepository.save(any(Candidate.class))).thenReturn(candidate);
        when(skillService.findByName(any(String.class))).thenReturn(skill);

        CandidateDto result = candidateService.addCandidate(new CandidateDto(candidate));

        assertNotNull(result);
        assertEquals(candidate.getName(), result.getName());

        verify(candidateRepository, times(1)).save(any(Candidate.class));
    }

    @Test
    public void testUpdateSkills()
            throws CandidateDoesNotExistException, SkillDoesNotExistException, AlreadyHasSkillException {
        Candidate candidate = createDummyCandidate();
        Skill skill = createDummySkill();
        CandidateDto candidateDto = new CandidateDto(createDummyCandidate());
        candidateDto.getSkills().add(new SkillDto(skill));
        when(candidateRepository.findById(candidate.getId())).thenReturn(Optional.of(candidate));
        when(skillService.findById(skill.getId())).thenReturn(skill);
        when(candidateRepository.save(candidate)).thenReturn(candidate);

        CandidateDto updatedCandidateDto = candidateService.updateSkills(candidate.getId(), skill.getId());

        assertNotNull(updatedCandidateDto);
        assertEquals(candidateDto.getSkills().size(), updatedCandidateDto.getSkills().size());
    }

    @Test
    public void testUpdateSkillsWithInvalidCandidateId() {
        Skill skill = createDummySkill();
        when(candidateRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(CandidateDoesNotExistException.class, () ->
                candidateService.updateSkills(1, skill.getId()));

        verify(candidateRepository, times(1)).findById(anyInt());
    }

    @Test
    public void testUpdateSkillsWithInvalidSkillId() throws SkillDoesNotExistException {
        Candidate candidate = createDummyCandidate();
        when(candidateRepository.findById(candidate.getId())).thenReturn(Optional.of(candidate));
        when(skillService.findById(anyInt())).thenThrow(new SkillDoesNotExistException("Skill does not exist!"));

        assertThrows(SkillDoesNotExistException.class, () ->
                candidateService.updateSkills(candidate.getId(), 1));

        verify(candidateRepository, times(1)).findById(candidate.getId());
        verify(skillService, times(1)).findById(anyInt());
    }

    @Test
    public void testUpdateSkillsWithAlreadyHasSkillException() throws SkillDoesNotExistException {
        Candidate candidate = createDummyCandidate();
        Skill skill = createDummySkill();
        candidate.getSkills().add(skill);
        when(candidateRepository.findById(candidate.getId())).thenReturn(Optional.of(candidate));
        when(skillService.findById(skill.getId())).thenReturn(skill);

        assertThrows(AlreadyHasSkillException.class,
                () -> candidateService.updateSkills(candidate.getId(), skill.getId()));

        verify(candidateRepository, times(1)).findById(candidate.getId());
        verify(skillService, times(1)).findById(skill.getId());
    }

    @Test
    public void testFindByIdWithExistingCandidate() throws CandidateDoesNotExistException {
        Candidate candidate = createDummyCandidate();
        when(this.candidateRepository.findById(1)).thenReturn(Optional.of(candidate));

        Candidate result = this.candidateService.findById(1);

        assertNotNull(result);
        assertEquals(candidate.getId(), result.getId());
    }

    @Test
    public void testFindByIdWithNonExistingCandidate() {
        when(this.candidateRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CandidateDoesNotExistException.class, () -> {
            this.candidateService.findById(1);
        });
    }

    @Test
    public void testRemoveSkill()
            throws CandidateDoesNotExistException, SkillDoesNotExistException, DoesNotHaveSkillException {
        Candidate candidate = createDummyCandidate();
        Skill skillToRemove = createDummySkill();
        candidate.getSkills().add(skillToRemove);

        when(candidateRepository.findById(candidate.getId())).thenReturn(Optional.of(candidate));
        when(skillService.findById(skillToRemove.getId())).thenReturn(skillToRemove);
        when(candidateRepository.save(candidate)).thenReturn(candidate);

        CandidateDto updatedCandidateDto = candidateService.removeSkill(candidate.getId(), skillToRemove.getId());

        assertNotNull(updatedCandidateDto);
        assertEquals(candidate.getId(), updatedCandidateDto.getId());

        List<SkillDto> updatedSkills = updatedCandidateDto.getSkills();
        assertNotNull(updatedSkills);
        assertEquals(0, updatedSkills.size());
    }

    @Test
    public void testRemoveSkillCandidateNotFound() {
        Skill skillToRemove = createDummySkill();

        when(candidateRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(CandidateDoesNotExistException.class,
                () -> candidateService.removeSkill(1, skillToRemove.getId()));
    }

    @Test
    public void testRemoveSkillSkillNotFound() throws SkillDoesNotExistException {
        Candidate candidate = createDummyCandidate();

        when(candidateRepository.findById(candidate.getId())).thenReturn(Optional.of(candidate));
        when(skillService.findById(anyInt())).thenThrow(new SkillDoesNotExistException("Skill does not exist!"));

        assertThrows(SkillDoesNotExistException.class, () -> candidateService.removeSkill(candidate.getId(), 3));
    }

    @Test
    public void testRemoveSkillDoesNotHaveSkill() throws SkillDoesNotExistException {
        Candidate candidate = createDummyCandidate();
        Skill skillToRemove = createDummySkill();

        when(candidateRepository.findById(candidate.getId())).thenReturn(Optional.of(candidate));
        when(skillService.findById(skillToRemove.getId())).thenReturn(skillToRemove);

        assertThrows(DoesNotHaveSkillException.class, () -> candidateService.removeSkill(candidate.getId(), skillToRemove.getId()));
        verify(candidateRepository, never()).save(any());
    }

    @Test
    public void testRemoveCandidate() throws CandidateDoesNotExistException {
        Candidate candidate = createDummyCandidate();
        when(this.candidateRepository.findById(candidate.getId())).thenReturn(Optional.of(candidate));

        this.candidateService.removeCandidate(candidate.getId());

        verify(this.candidateRepository, times(1)).delete(candidate);
    }

    @Test
    public void testRemoveCandidateNotFound() {
        int candidateId = 1;
        when(this.candidateRepository.findById(candidateId)).thenReturn(Optional.empty());

        assertThrows(CandidateDoesNotExistException.class, () -> {
            this.candidateService.removeCandidate(candidateId);
        });

        verify(this.candidateRepository, never()).delete(any(Candidate.class));
    }

    @Test
    public void testFindByName() throws CandidateDoesNotExistException {
        Candidate candidate = createDummyCandidate();
        when(this.candidateRepository.findByName(candidate.getName())).thenReturn(Optional.of(candidate));

        CandidateDto foundCandidateDto = this.candidateService.findByName(candidate.getName());

        assertNotNull(foundCandidateDto);
        assertEquals(candidate.getName(), foundCandidateDto.getName());
    }

    @Test
    public void testFindByNameThrowsException() {
        String name = "Non-existent Candidate";
        when(this.candidateRepository.findByName(name)).thenReturn(Optional.empty());

        assertThrows(CandidateDoesNotExistException.class, () -> {
            this.candidateService.findByName(name);
        });
    }

    @Test
    public void testFindBySkills() throws SkillDoesNotExistException, CandidateDoesNotExistException {
        SkillDto skillDto = createDummySkillDto();
        Skill skill = createDummySkill();
        Candidate candidate = createDummyCandidate();
        List<SkillDto> skillDtos = Collections.singletonList(skillDto);
        List<Candidate> candidates = Collections.singletonList(candidate);
        when(this.skillService.findById(skillDto.getId())).thenReturn(skill);
        when(this.candidateRepository.findBySkills(Collections.singletonList(skill.getId())))
                .thenReturn(Optional.of(candidates));

        List<CandidateDto> foundCandidates = candidateService.findBySkills(skillDtos);

        assertNotNull(foundCandidates);
        assertEquals(1, foundCandidates.size());
        assertEquals(candidate.getId(), foundCandidates.get(0).getId());
    }

    @Test
    public void testFindBySkillsWhenSkillDoesNotExist() throws SkillDoesNotExistException {
        SkillDto skillDto = createDummySkillDto();
        when(this.skillService.findById(skillDto.getId())).thenReturn(null);

        assertThrows(SkillDoesNotExistException.class, () -> {
            candidateService.findBySkills(Collections.singletonList(skillDto));
        });
    }

    @Test
    public void testFindBySkillsWhenCandidatesDoNotExist() throws SkillDoesNotExistException {
        SkillDto skillDto = createDummySkillDto();
        Skill skill = createDummySkill();
        List<SkillDto> skillDtos = Collections.singletonList(skillDto);
        when(this.skillService.findById(skillDto.getId())).thenReturn(skill);
        when(this.candidateRepository.findBySkills(Collections.singletonList(skill.getId())))
                .thenReturn(Optional.empty());

        assertThrows(CandidateDoesNotExistException.class, () -> {
            candidateService.findBySkills(skillDtos);
        });
    }

}
