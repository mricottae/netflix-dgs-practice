package com.example.dgspractice.mapper;

import com.example.dgspractice.dto.ReviewDto;
import com.example.dgspractice.entity.Review;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewMapper {

    public ReviewDto toReviewDto(Review review) {
        return new ReviewDto(null, review.getUsername(), review.getStarScore(), review.getSubmittedDate());
    }

    public List<ReviewDto> toReviewDtoList(List<Review> reviews) {
        return reviews.stream().map(this::toReviewDto).toList();
    }

    public Review toReviewEntity(ReviewDto review) {
        return new Review(null, review.showId(), review.username(), review.starScore(), review.submittedDate());
    }
}
