package com.example.demo.service;

import com.example.demo.HrManagementBackend;
import com.example.demo.dto.SkillDto;
import com.example.demo.exception.SkillDoesNotExistException;
import com.example.demo.model.Skill;
import com.example.demo.repository.SkillRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HrManagementBackend.class)
public class SkillServiceTest {

    @Autowired
    private SkillService skillService;

    @MockBean
    private SkillRepository skillRepository;

    private SkillDto createDummySkillDto() {
        return new SkillDto(1, "Java");
    }

    private Skill createDummySkill() {
        return new Skill(1, "Java");
    }

    @Test
    public void testAddSkill() {
        SkillDto skillDto = this.createDummySkillDto();
        Skill skill = this.createDummySkill();
        when(this.skillRepository.save(any(Skill.class))).thenReturn(skill);

        SkillDto savedSkillDto = skillService.addSkill(skillDto);

        assertNotNull(savedSkillDto);
        assertEquals(skillDto.getName(), savedSkillDto.getName());
    }

    @Test
    public void testFindByName() {
        String skillName = "Java";
        Skill expectedSkill = createDummySkill();
        when(skillRepository.findByName(skillName)).thenReturn(expectedSkill);

        Skill actualSkill = skillService.findByName(skillName);

        assertNotNull(actualSkill);
        assertEquals(expectedSkill, actualSkill);
    }

    @Test
    public void testFindByIdSuccess() throws SkillDoesNotExistException {
        int skillId = 1;
        Skill expectedSkill = createDummySkill();
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(expectedSkill));

        Skill actualSkill = skillService.findById(skillId);

        assertNotNull(actualSkill);
        assertEquals(expectedSkill, actualSkill);
    }

    @Test(expected = SkillDoesNotExistException.class)
    public void testFindByIdFailure() throws SkillDoesNotExistException {
        int skillId = 1;
        when(skillRepository.findById(skillId)).thenReturn(Optional.empty());

        skillService.findById(skillId);
    }

}
