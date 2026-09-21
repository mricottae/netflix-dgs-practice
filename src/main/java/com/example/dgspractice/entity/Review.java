package com.example.dgspractice.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reviews")
public class Review {

    @Id // Required: Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer showId;
    private String username;
    private Integer starScore;
    private LocalDateTime submittedDate;
}
