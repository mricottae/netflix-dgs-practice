package com.example.dgspractice.dto;

import java.time.LocalDateTime;

public record ReviewDto(Integer showId, String username, Integer starScore, LocalDateTime submittedDate) {}
