package com.example.dgspractice.service;

import com.example.dgspractice.entity.Movie;
import com.example.dgspractice.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Optional<Movie> findById(int id) {
        return movieRepository.findById(id);
    }

    public List<Movie> findByTitleAndYear(String title, Integer year) {
        if (title != null && year != null) {
            return movieRepository.findByTitleContainingIgnoreCaseAndReleaseYear(title, year);
        } else if (title != null) {
            return movieRepository.findByTitleContainingIgnoreCase(title);
        } else if (year != null) {
            return movieRepository.findByReleaseYear(year);
        }
        return movieRepository.findAll();
    }
}
