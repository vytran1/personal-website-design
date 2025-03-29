package com.personalinformation.vta.features.certificate.getallcertificatfollowingcandidate;


import com.personalinformation.vta.common.IQuery;
import com.personalinformation.vta.entities.Certificate;
import com.personalinformation.vta.features.certificate.CertificateDTO;
import com.personalinformation.vta.features.certificate.CertificateMapper;
import com.personalinformation.vta.features.certificate.CertificateRepository;
import io.membrane_api.jmediator.Handler;

import java.util.List;

record GetAllCertificateBelongToOneCandidateCommand(Integer candidateId) implements IQuery<List<CertificateDTO>>{};


@Handler
public class GetAllCertificateBelongToOneCandidateHandler {

    private final CertificateRepository certificateRepository;
    private final CertificateMapper mapper;

    public GetAllCertificateBelongToOneCandidateHandler(CertificateRepository certificateRepository,CertificateMapper mapper) {
        this.certificateRepository = certificateRepository;
        this.mapper = mapper;
    }



    public List<CertificateDTO> handler(GetAllCertificateBelongToOneCandidateCommand command){

        List<Certificate> certificates = certificateRepository.findCertificateByCandidateId(command.candidateId());

        List<CertificateDTO> certificateDTOList = certificates.stream().map(mapper::mapToDTO).toList();

        return certificateDTOList;

    }
}
