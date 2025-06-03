package edu.npic.sps.features.signUpMethod.dto;

import jakarta.validation.constraints.NotNull;

public record SignupMethodRequest(
        @NotNull(message = "Name is required")
        String name
) {
}
