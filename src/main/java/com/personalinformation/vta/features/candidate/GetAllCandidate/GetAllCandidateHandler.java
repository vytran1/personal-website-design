package com.personalinformation.vta.features.candidate.GetAllCandidate;

import com.personalinformation.vta.common.IQuery;
import com.personalinformation.vta.entities.Candidate;
import com.personalinformation.vta.features.candidate.CandidateRepository;
import io.membrane_api.jmediator.Handler;
import io.membrane_api.jmediator.IRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import java.util.List;

record GetAllCandidateCommand(Integer pageNum, Integer pageSize, String sortField, String sortDir) implements IQuery<List<Candidate>> {}

@Handler
public class GetAllCandidateHandler{
    CandidateRepository candidateRepository;

    public GetAllCandidateHandler(CandidateRepository candidateRepository){
        this.candidateRepository = candidateRepository;
    }

    public ResponseEntity<?> getAllCandidate(GetAllCandidateCommand command){

        Sort sort = Sort.by(command.sortField());
        sort = command.sortDir().equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(command.pageNum() - 1,command.pageSize(),sort);

        Page<Candidate> pages = candidateRepository.findAll(pageable);

        if(pages.isEmpty()){
            return ResponseEntity.noContent().build();
        }else{
            List<Candidate> candidates = pages.getContent();
            return ResponseEntity.ok(candidates);
        }
    }
}
