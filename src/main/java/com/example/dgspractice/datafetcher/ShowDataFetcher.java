package com.example.dgspractice.datafetcher;

import com.example.dgspractice.dataloader.ReviewsDataLoader;
import com.example.dgspractice.dto.AddShowInput;
import com.example.dgspractice.dto.MovieDto;
import com.example.dgspractice.dto.ReviewDto;
import com.example.dgspractice.dto.ShowDto;
import com.example.dgspractice.entity.Movie;
import com.example.dgspractice.entity.Review;
import com.example.dgspractice.entity.Show;
import com.example.dgspractice.event.ShowEventPublisher;
import com.example.dgspractice.exception.ShowNotFoundException;
import com.example.dgspractice.mapper.MovieMapper;
import com.example.dgspractice.mapper.ReviewMapper;
import com.example.dgspractice.mapper.ShowMapper;
import com.example.dgspractice.service.MovieService;
import com.example.dgspractice.service.ReviewService;
import com.example.dgspractice.service.ShowService;
import com.example.dgspractice.generated.DgsConstants;
import com.netflix.graphql.dgs.*;
import org.dataloader.DataLoader;
import org.reactivestreams.Publisher;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
public class ShowDataFetcher {

    private final ShowService showService;

    private final ReviewService reviewService;

    private final MovieService movieService;

    private final ShowMapper showMapper;

    private final ReviewMapper reviewMapper;

    private final MovieMapper movieMapper;

    private final ShowEventPublisher showEventPublisher;

    public ShowDataFetcher(ShowService showService,  ShowMapper showMapper, ReviewMapper reviewMapper,  ReviewService reviewService, MovieService movieService, MovieMapper movieMapper, ShowEventPublisher showEventPublisher) {
        this.showService = showService;
        this.showMapper = showMapper;
        this.reviewMapper = reviewMapper;
        this.reviewService = reviewService;
        this.movieService = movieService;
        this.movieMapper = movieMapper;
        this.showEventPublisher = showEventPublisher;
    }

    @DgsQuery
    public List<ShowDto> shows(@InputArgument String titleFilter, @InputArgument Integer yearFilter) {
        return showMapper.toShowDtoList(showService.findByTitleAndYear(titleFilter, yearFilter));
    }

    @DgsQuery
    public ShowDto showById(@InputArgument int id) {
        return showService.findById(id)
                .map(showMapper::toShowDto)
                .orElseThrow(() -> new ShowNotFoundException(id));
    }

    @DgsMutation
    public ShowDto addShow(@InputArgument AddShowInput input) {
        return showMapper.toShowDto(showService.add(input));
    }

    @DgsData(parentType = DgsConstants.SHOW.TYPE_NAME, field = DgsConstants.SHOW.Reviews)
    public CompletableFuture<List<ReviewDto>> reviews(DgsDataFetchingEnvironment dfe) {
        DataLoader<Integer, List<Review>> loader = dfe.getDataLoader(ReviewsDataLoader.class);
        ShowDto show = dfe.getSource();
        return loader.load(show.id()).thenApply(reviewMapper::toReviewDtoList);
    }

    @DgsTypeResolver(name = "SearchResult")
    public String resolveSearchResult(Object result) {
        if (result instanceof ShowDto) return "Show";
        if (result instanceof MovieDto) return "Movie";
        if (result instanceof ReviewDto) return "Review";
        throw new IllegalStateException("Unknown SearchResult type: " + result.getClass());
    }

    @DgsSubscription
    public Publisher<ShowDto> showAdded() {
        return showEventPublisher.showAdded().map(showMapper::toShowDto);
    }

    @DgsQuery
    public List<ReviewDto> reviewsSince(@InputArgument LocalDateTime since) {
        return reviewMapper.toReviewDtoList(reviewService.reviewsSince(since));
    }

    @DgsQuery
    public List<Object> search(@InputArgument String term) {
        List<ShowDto> matchingShows = showMapper.toShowDtoList(showService.findByTitleAndYear(term, null));
        List<ReviewDto> matchingReviews = reviewMapper.toReviewDtoList(reviewService.findByUsernameContaining(term));
        List<MovieDto> matchingMovies = movieMapper.toMovieDtoList(movieService.findByTitleAndYear(term, null));
        List<Object> combinedResults = new ArrayList<>();
        combinedResults.addAll(matchingShows);
        combinedResults.addAll(matchingReviews);
        combinedResults.addAll(matchingMovies);
        return combinedResults;
    }
}
