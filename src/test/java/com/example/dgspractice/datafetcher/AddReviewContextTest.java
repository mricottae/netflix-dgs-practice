package com.example.dgspractice.datafetcher;

import com.example.dgspractice.repository.ReviewRepository;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import graphql.ExecutionResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AddReviewContextTest {

    private static final String ADD_REVIEW =
            "mutation { addReview(input: {showId: 1, starScore: %d}) { username starScore submittedDate } }";

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    ReviewRepository reviewRepository;

    private static HttpHeaders userHeader(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-User", username);
        return headers;
    }

    @Test
    void reviewIsAttributedToTheHeaderUser() {
        String username = dgsQueryExecutor.executeAndExtractJsonPath(
                ADD_REVIEW.formatted(5), "data.addReview.username", userHeader("matias"));

        assertThat(username).isEqualTo("matias");
        assertThat(reviewRepository.findByShowId(1))
                .extracting(r -> r.getUsername())
                .contains("matias");
    }

    @Test
    void submittedDateIsStampedByTheServer() {
        String submittedDate = dgsQueryExecutor.executeAndExtractJsonPath(
                ADD_REVIEW.formatted(4), "data.addReview.submittedDate", userHeader("ana"));

        assertThat(submittedDate).isNotNull();   // set server-side, never sent by the client
    }

    @Test
    void missingHeaderIsPermissionDenied() {
        long before = reviewRepository.count();

        ExecutionResult result = dgsQueryExecutor.execute(
                ADD_REVIEW.formatted(5), Map.of(), Map.of(), new HttpHeaders());

        assertThat(result.getErrors()).hasSize(1);
        assertThat(result.getErrors().getFirst().getExtensions())
                .containsEntry("errorType", "PERMISSION_DENIED");
        assertThat(reviewRepository.count()).isEqualTo(before);
    }

    @Test
    void starScoreOutOfRangeIsBadRequest() {
        ExecutionResult result = dgsQueryExecutor.execute(
                ADD_REVIEW.formatted(9), Map.of(), Map.of(), userHeader("matias"));

        assertThat(result.getErrors().getFirst().getExtensions())
                .containsEntry("errorType", "BAD_REQUEST");
        assertThat(result.getErrors().getFirst().getMessage())
                .contains("starScore: must be between 1 and 5");
    }

    @Test
    void unknownShowIsNotFound() {
        ExecutionResult result = dgsQueryExecutor.execute(
                "mutation { addReview(input: {showId: 99, starScore: 5}) { username } }",
                Map.of(), Map.of(), userHeader("matias"));

        assertThat(result.getErrors().getFirst().getExtensions())
                .containsEntry("errorType", "NOT_FOUND");
    }
}
