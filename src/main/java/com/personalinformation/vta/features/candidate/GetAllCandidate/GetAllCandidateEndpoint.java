package com.personalinformation.vta.features.candidate.GetAllCandidate;

import io.membrane_api.jmediator.JMediator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/candidates")
public class GetAllCandidateEndpoint {

    private final JMediator jMediator;

    public GetAllCandidateEndpoint(JMediator jMediator){
        this.jMediator = jMediator;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllCandidate(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "2") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir
    ) throws Throwable {
        GetAllCandidateCommand command = new GetAllCandidateCommand(pageNum,pageSize,sortField,sortDir);
        return (ResponseEntity<?>) jMediator.send(command);
    }
}
