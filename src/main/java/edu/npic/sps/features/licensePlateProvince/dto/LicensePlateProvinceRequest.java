package edu.npic.sps.features.licensePlateProvince.dto;

import jakarta.validation.constraints.NotNull;

public record LicensePlateProvinceRequest(
        @NotNull(message = "Province name kh is required")
        String provinceNameKh,
        @NotNull(message = "Province name en is required")
        String provinceNameEn
) {
}
