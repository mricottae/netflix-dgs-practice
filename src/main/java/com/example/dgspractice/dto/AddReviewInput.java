package com.example.dgspractice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Note: no username. The caller's identity comes from the request context, never the input. */
public record AddReviewInput(

        @NotNull(message = "is required")
        Integer showId,

        @NotNull(message = "is required")
        @Min(value = 1, message = "must be between 1 and 5")
        @Max(value = 5, message = "must be between 1 and 5")
        Integer starScore) {}
