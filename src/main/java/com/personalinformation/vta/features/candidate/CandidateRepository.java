package com.personalinformation.vta.features.candidate;

import com.personalinformation.vta.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;


public interface CandidateRepository extends JpaRepository<Candidate,Integer> {


    @Query("SELECT c FROM Candidate c WHERE c.email = ?1")
    public Optional<Candidate> findCandidateByEmail(String email);

}
