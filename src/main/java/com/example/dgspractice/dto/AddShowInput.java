package com.example.dgspractice.dto;

import jakarta.validation.constraints.NotBlank;

public record AddShowInput(@NotBlank String title, Integer releaseYear, Integer seasons) {}