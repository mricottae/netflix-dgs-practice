package com.example.dgspractice.repository;

import com.example.dgspractice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByShowId(int showId);
    List<Review> findByShowIdIn(Set<Integer> showIds);
    List<Review> findByUsernameContainingIgnoreCase(String search);
    List<Review> findBySubmittedDateAfter(LocalDateTime since);
}
