package com.personalinformation.vta.features.candidate;

import com.personalinformation.vta.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;


public interface CandidateRepository extends JpaRepository<Candidate,Integer> {

}
