package com.personalinformation.vta.features.candidate.GetPersonalInformationBelongToUser;


import com.personalinformation.vta.common.IQuery;
import com.personalinformation.vta.entities.Candidate;
import com.personalinformation.vta.features.candidate.CandidateDTO;
import com.personalinformation.vta.features.candidate.CandidateMapper;
import com.personalinformation.vta.features.candidate.CandidateRepository;
import io.membrane_api.jmediator.Handler;

import java.util.Optional;

record GetAllPersonalInformationCommand(String email) implements IQuery<CandidateDTO>{}

@Handler
public class GetAllPersonalInformationHandler {



    private final CandidateRepository candidateRepository;

    private final CandidateMapper candidateMapper;

    public GetAllPersonalInformationHandler(CandidateRepository candidateRepository, CandidateMapper candidateMapper) {
        this.candidateRepository = candidateRepository;
        this.candidateMapper = candidateMapper;
    }


    public CandidateDTO getPersonalInformation(GetAllPersonalInformationCommand command){
        //Get Email From Command
        String email = command.email();

        //Query Database
        Candidate candidate = candidateRepository.findCandidateByEmail(email).get();

        //Map to DTO
        CandidateDTO candidateDTO = candidateMapper.mapToDTO(candidate);

        return candidateDTO;
    }

}
