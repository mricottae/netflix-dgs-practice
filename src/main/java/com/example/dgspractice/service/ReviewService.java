package com.example.dgspractice.service;

import com.example.dgspractice.entity.Review;
import com.example.dgspractice.repository.ReviewRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> findByUsernameContaining(String search) {
        if (search != null) {
            return reviewRepository.findByUsernameContainingIgnoreCase(search);
        }
        return reviewRepository.findAll();
    }

    public List<Review> reviewsForShow(int showId) {
        log.info("LOAD reviews for show {}", showId);
        return reviewRepository.findByShowId(showId);
    }

    public Map<Integer, List<Review>> reviewsForShows(Set<Integer> showIds) {
        log.info("BATCH LOAD reviews for shows {}", showIds);
        return reviewRepository.findByShowIdIn(showIds).stream()
                .collect(Collectors.groupingBy(Review::getShowId));
    }
}
