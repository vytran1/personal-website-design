package com.personalinformation.vta.features.candidate.GetPersonalInformationBelongToUser;

import com.personalinformation.vta.common.utility.Utility;
import com.personalinformation.vta.features.candidate.CandidateDTO;
import io.membrane_api.jmediator.JMediator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/candidates")
public class GetAllPersonalInformationEndpoint {

    private final JMediator jMediator;


    public GetAllPersonalInformationEndpoint(JMediator jMediator) {
        this.jMediator = jMediator;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllInformation() throws Throwable {
        //Get Integer from Security Context Holder
        String email = Utility.getEmailOfCurrentLoginUser();

        //Send to handler
        CandidateDTO candidateDTO = jMediator.send(new GetAllPersonalInformationCommand(email));

        return ResponseEntity.ok(candidateDTO);
    }
}
