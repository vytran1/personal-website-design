package com.personalinformation.vta.features.certificate;

import com.personalinformation.vta.entities.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate,Integer> {

     @Query("SELECT c FROM Certificate c WHERE c.candidate.id = ?1")
     public List<Certificate> findCertificateByCandidateId(Integer candidateId);
}
