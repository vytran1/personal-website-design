package com.personalinformation.vta.features.certificate.getallcertificatfollowingcandidate;

import com.personalinformation.vta.common.utility.Utility;
import com.personalinformation.vta.features.certificate.CertificateDTO;
import io.membrane_api.jmediator.JMediator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/certificates")
public class GetAllCertificateBelongToOneCandidateEndpoint {

    private final JMediator jMediator;


    public GetAllCertificateBelongToOneCandidateEndpoint(JMediator jMediator) {
        this.jMediator = jMediator;
    }


    @GetMapping("")
    public ResponseEntity<?> getAllCertificateBelongToOneCandidate() throws Throwable {
        Integer id = Utility.getIdOfCurrentLoginUser();
        List<CertificateDTO> certificateDTOS = jMediator.send(new GetAllCertificateBelongToOneCandidateCommand(id));
        return ResponseEntity.ok(certificateDTOS);
    }
}
