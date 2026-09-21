package com.example.dgspractice.service;

import com.example.dgspractice.dto.AddReviewInput;
import com.example.dgspractice.entity.Review;
import com.example.dgspractice.exception.ShowNotFoundException;
import com.example.dgspractice.repository.ShowRepository;
import com.example.dgspractice.repository.ReviewRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final ShowRepository showRepository;

    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    public ReviewService(ReviewRepository reviewRepository, ShowRepository showRepository) {
        this.reviewRepository = reviewRepository;
        this.showRepository = showRepository;
    }

    /** The username comes from the request context, never from client input. */
    public Review add(@Valid AddReviewInput input, String username) {
        if (!showRepository.existsById(input.showId())) {
            throw new ShowNotFoundException(input.showId());
        }
        Review review = new Review(null, input.showId(), username, input.starScore(), LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public List<Review> findByUsernameContaining(String search) {
        if (search != null) {
            return reviewRepository.findByUsernameContainingIgnoreCase(search);
        }
        return reviewRepository.findAll();
    }

    public List<Review> reviewsSince(LocalDateTime since) {
        return reviewRepository.findBySubmittedDateAfter(since);
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
