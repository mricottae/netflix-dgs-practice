package com.example.dgspractice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AddShowInput(

        @NotBlank(message = "must not be blank")
        String title,

        @Min(value = 1888, message = "must be 1888 or later")
        Integer releaseYear,

        @Positive(message = "must be greater than zero")
        Integer seasons) {}
