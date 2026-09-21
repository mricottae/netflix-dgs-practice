package com.example.dgspractice.repository;

import com.example.dgspractice.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Integer> {

    List<Show> findByTitleContainingIgnoreCaseAndReleaseYear(String title, Integer year);

    List<Show> findByTitleContainingIgnoreCase(String title);

    List<Show> findByReleaseYear(Integer year);
}
