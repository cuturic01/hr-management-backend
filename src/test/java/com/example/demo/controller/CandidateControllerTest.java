package com.example.demo.controller;

import com.example.demo.dto.CandidateDto;
import com.example.demo.dto.SkillDto;
import com.example.demo.exception.AlreadyHasSkillException;
import com.example.demo.exception.CandidateDoesNotExistException;
import com.example.demo.exception.DoesNotHaveSkillException;
import com.example.demo.exception.SkillDoesNotExistException;
import com.example.demo.model.Candidate;
import com.example.demo.model.Skill;
import com.example.demo.service.CandidateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CandidateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CandidateService candidateService;

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

    private CandidateDto createDummyCandidateDto() {
        return new CandidateDto(createDummyCandidate());
    }

    private Skill createDummySkill() {
        return new Skill(1, "Java");
    }

    private SkillDto createDummySkillDto() {
        return new SkillDto(1, "Java");
    }

    @Test
    public void testAddCandidate() throws Exception {
        CandidateDto candidateDto = createDummyCandidateDto();

        when(candidateService.addCandidate(any(CandidateDto.class))).thenReturn(candidateDto);
        System.out.println(candidateDto.getId());

        mockMvc.perform(post("/api/candidate/add-candidate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .registerModule(new JavaTimeModule()).writeValueAsString(candidateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.contactNumber").value("555-555-5555"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    public void testUpdateSkills() throws Exception {
        Integer candidateId = 1;
        Integer skillId = 1;
        CandidateDto candidateDto = createDummyCandidateDto();
        when(candidateService.updateSkills(any(Integer.class), any(Integer.class))).thenReturn(candidateDto);

        mockMvc.perform(put("/api/candidate/update-skills/{candidateId}/{skillId}", candidateId, skillId)
//                        .param("candidate-id", String.valueOf(candidateId))
//                        .param("skill-id", String.valueOf(skillId))
                        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void testUpdateSkillsCandidateDoesNotExist() throws Exception {
        Integer candidateId = 1;
        Integer skillId = 1;
        when(candidateService.updateSkills(any(Integer.class), any(Integer.class)))
                .thenThrow(CandidateDoesNotExistException.class);

        mockMvc.perform(put("/api/candidate/update-skills/{candidate-id}/{skill-id}", candidateId, skillId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateSkillsSkillDoesNotExist() throws Exception {
        Integer candidateId = 1;
        Integer skillId = 1;
        when(candidateService.updateSkills(any(Integer.class), any(Integer.class)))
                .thenThrow(SkillDoesNotExistException.class);

        mockMvc.perform(put("/api/candidate/update-skills/{candidate-id}/{skill-id}", candidateId, skillId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateSkillsAlreadyHasSkill() throws Exception {
        Integer candidateId = 1;
        Integer skillId = 1;
        when(candidateService.updateSkills(any(Integer.class), any(Integer.class)))
                .thenThrow(AlreadyHasSkillException.class);

        mockMvc.perform(put("/api/candidate/update-skills/{candidate-id}/{skill-id}", candidateId, skillId))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRemoveSkill() throws Exception {
        Integer candidateId = 1;
        Integer skillId = 1;
        when(candidateService.removeSkill(any(Integer.class), any(Integer.class))).thenReturn(createDummyCandidateDto());

        mockMvc.perform(put("/api/candidate/remove-skills/{candidate-id}/{skill-id}", candidateId, skillId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void testRemoveSkillCandidateDoesNotExistException() throws Exception {
        Integer candidateId = 1;
        Integer skillId = 1;
        String exceptionMessage = "Candidate does not exist";
        when(candidateService.removeSkill(any(Integer.class), any(Integer.class))).thenThrow(new CandidateDoesNotExistException(exceptionMessage));

        mockMvc.perform(put("/api/candidate/remove-skills/{candidate-id}/{skill-id}", candidateId, skillId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(exceptionMessage));
    }

    @Test
    public void testRemoveSkillSkillDoesNotExistException() throws Exception {
        Integer candidateId = 1;
        Integer skillId = 1;
        String exceptionMessage = "Skill does not exist";
        when(candidateService.removeSkill(any(Integer.class), any(Integer.class)))
                .thenThrow(new SkillDoesNotExistException(exceptionMessage));

        mockMvc.perform(put("/api/candidate/remove-skills/{candidate-id}/{skill-id}", candidateId, skillId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(exceptionMessage));
    }

    @Test
    public void testRemoveSkillDoesNotHaveSkillException() throws Exception {
        Integer candidateId = 1;
        Integer skillId = 1;
        String exceptionMessage = "Candidate does not have the specified skill";
        when(candidateService.removeSkill(any(Integer.class), any(Integer.class)))
                .thenThrow(new DoesNotHaveSkillException(exceptionMessage));

        mockMvc.perform(put("/api/candidate/remove-skills/{candidate-id}/{skill-id}", candidateId, skillId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(exceptionMessage));
    }

    @Test
    public void testSearchByName() throws Exception {
        String candidateName = "John Doe";
        CandidateDto candidateDto = createDummyCandidateDto();
        when(candidateService.findByName(candidateName)).thenReturn(candidateDto);

        mockMvc.perform(get("/api/candidate/search-by-name/{candidate-name}", candidateName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(candidateDto.getId()))
                .andExpect(jsonPath("$.name").value(candidateDto.getName()));
    }

    @Test
    public void testSearchByNameReturnsNotFound() throws Exception {
        String candidateName = "John";
        String errorMessage = "Candidate not found";
        when(candidateService.findByName(candidateName)).thenThrow(new CandidateDoesNotExistException(errorMessage));

        mockMvc.perform(get("/api/candidate/search-by-name/" + candidateName))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));
    }

    @Test
    public void testSearchBySkills() throws Exception {
        List<SkillDto> skills = Arrays.asList(new SkillDto(1, "Java"), new SkillDto(2, "Python"));
        CandidateDto candidateDto1 = createDummyCandidateDto();
        CandidateDto candidateDto2 = createDummyCandidateDto();
        candidateDto2.setId(2);
        when(candidateService.findBySkills(any(List.class))).thenReturn(Arrays.asList(candidateDto1, candidateDto2));

        mockMvc.perform(get("/api/candidate/search-by-skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .registerModule(new JavaTimeModule()).writeValueAsString(skills)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[1].id").exists());
    }

    @Test
    public void testSearchBySkillsCandidatesFound() throws Exception {
        List<SkillDto> skills = Arrays.asList(new SkillDto(1, "Java"), new SkillDto(2, "Python"));
        when(candidateService.findBySkills(any(List.class)))
                .thenThrow(new CandidateDoesNotExistException("No candidate found with the given skills"));

        mockMvc.perform(get("/api/candidate/search-by-skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .registerModule(new JavaTimeModule()).writeValueAsString(skills)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("No candidate found with the given skills"));
    }

    @Test
    public void testSearchBySkillsWhenInvalidSkillsProvided() throws Exception {
        List<SkillDto> skills = Arrays.asList(new SkillDto(1, "Java"), new SkillDto(2, "invalidSkill"));
        when(candidateService.findBySkills(any(List.class))).thenThrow(
                new SkillDoesNotExistException("Skill does not exist"));

        mockMvc.perform(get("/api/candidate/search-by-skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .registerModule(new JavaTimeModule()).writeValueAsString(skills)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Skill does not exist"));
    }

}
