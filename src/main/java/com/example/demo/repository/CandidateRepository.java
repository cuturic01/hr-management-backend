package com.example.demo.repository;

import com.example.demo.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Integer> {

    Optional<Candidate> findByName(String name);

    @Query("select c from Candidate c inner join c.skills s where s.id in :skillIds")
    Optional<List<Candidate>> findBySkills(List<Integer> skillIds);

}
