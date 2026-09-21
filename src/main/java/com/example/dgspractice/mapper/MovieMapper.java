package com.example.dgspractice.mapper;

import com.example.dgspractice.dto.MovieDto;
import com.example.dgspractice.entity.Movie;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieMapper {

    public MovieDto toMovieDto(Movie movie) {
        return new MovieDto(movie.getId(), movie.getTitle(), movie.getReleaseYear(), movie.getRuntimeMinutes());
    }

    public List<MovieDto> toMovieDtoList(List<Movie> movies) {
        return movies.stream().map(this::toMovieDto).toList();
    }
}
