package com.example.dgspractice.dataloader;

import com.example.dgspractice.entity.Review;
import com.example.dgspractice.service.ReviewService;
import com.netflix.graphql.dgs.DgsDataLoader;
import org.dataloader.MappedBatchLoader;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "reviews")
public class ReviewsDataLoader implements MappedBatchLoader<Integer, List<Review>> {

    private final ReviewService reviewService;

    public ReviewsDataLoader(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Override
    public CompletionStage<Map<Integer, List<Review>>> load(Set<Integer> showIds) {
        return CompletableFuture.supplyAsync(() -> reviewService.reviewsForShows(showIds));
    }
}
