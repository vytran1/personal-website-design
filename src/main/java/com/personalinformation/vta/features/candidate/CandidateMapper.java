package com.personalinformation.vta.features.candidate;

import com.personalinformation.vta.entities.Candidate;
import org.springframework.stereotype.Component;

@Component
public class CandidateMapper {

    public CandidateMapper() {
    }


    public CandidateDTO mapToDTO(Candidate entity){
        CandidateDTO dto = new CandidateDTO();
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setAddress(entity.getAddress());
        dto.setDob(entity.getDob());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setEmail(entity.getEmail());
        dto.setShortDescription(!entity.getShortDescription().isEmpty() ? entity.getShortDescription() : "");
        dto.setLongDescription(!entity.getLongDescription().isEmpty() ? entity.getLongDescription() : "");
        return dto;

    }
}
