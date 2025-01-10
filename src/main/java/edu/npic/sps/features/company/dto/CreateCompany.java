package edu.npic.sps.features.company.dto;

import java.util.List;

public record CreateCompany(
        String companyName,
        String address,
        String image,
        String companyTypeUuid,
        List<String> siteNames
) {
}
