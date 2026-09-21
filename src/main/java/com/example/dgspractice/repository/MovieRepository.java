package com.example.dgspractice.repository;

import com.example.dgspractice.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    List<Movie> findByTitleContainingIgnoreCaseAndReleaseYear(String title, Integer year);

    List<Movie> findByTitleContainingIgnoreCase(String title);

    List<Movie> findByReleaseYear(Integer year);

    List<Movie> findByRuntimeMinutes(Integer runtimeMinutes);
}
