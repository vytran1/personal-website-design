package com.personalinformation.vta.features.certificate;

import com.personalinformation.vta.entities.Certificate;
import org.springframework.stereotype.Component;


@Component
public class CertificateMapper {



    public CertificateDTO mapToDTO(Certificate certificate){
        CertificateDTO certificateDTO = new CertificateDTO();
        certificateDTO.setName(certificate.getName());
        certificateDTO.setDescription(certificate.getDescription());
        return certificateDTO;
    }


}
