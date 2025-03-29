package com.personalinformation.vta.features.certificate;
import static org.assertj.core.api.Assertions.assertThat;

import com.personalinformation.vta.entities.Candidate;
import com.personalinformation.vta.entities.Certificate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CertificateRepositoryTest {

    @Autowired
    private CertificateRepository certificateRepository;

    @Test
    public void testCreateTable(){

    }

    @Test
    public void testCreateNewCertificate(){
        //Define Information
        Certificate certificate = new Certificate();
        certificate.setName("IELTS 6.5");
        certificate.setDescription("IELTS 6.5");
        certificate.setCandidate(new Candidate(1));

        //Save to database
        Certificate savedCertificate = certificateRepository.save(certificate);

        //assert
        assertThat(savedCertificate).isNotNull();
    }

    @Test
    public void testFindAllCertificatesByCandidateId(){
        Integer candidateId = 1;

        List<Certificate> certificateOPT = certificateRepository.findCertificateByCandidateId(candidateId);

        assertThat(certificateOPT.size()).isGreaterThan(0);

    }


}
