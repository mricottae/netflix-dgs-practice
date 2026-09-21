package com.example.dgspractice.datafetcher;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import graphql.ExecutionResult;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ShowAddedSubscriptionTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    void subscribersReceiveShowsCreatedByTheMutation() {
        ExecutionResult subscription = dgsQueryExecutor.execute("subscription { showAdded { title seasons } }");

        assertThat(subscription.getErrors()).isEmpty();
        Publisher<ExecutionResult> publisher = subscription.getData();

        StepVerifier.create(Flux.from(publisher).map(this::titleOf))
                .then(() -> addShow("Dark", 2017, 3))
                .expectNext("Dark")
                .then(() -> addShow("Severance", 2022, 2))
                .expectNext("Severance")
                .thenCancel()
                .verify(Duration.ofSeconds(5));
    }

    @Test
    void eventsEmittedWithNoSubscriberAreSimplyDropped() {
        addShow("Unwatched", 2001, 1);   // nobody is listening; the mutation still succeeds

        String title = dgsQueryExecutor.executeAndExtractJsonPath(
                "{ shows(titleFilter: \"Unwatched\") { title } }", "data.shows[0].title");

        assertThat(title).isEqualTo("Unwatched");
    }

    /** Each emission is a full GraphQL response: the selection set, already serialized. */
    @SuppressWarnings("unchecked")
    private String titleOf(ExecutionResult result) {
        Map<String, Object> data = result.getData();
        Map<String, Object> showAdded = (Map<String, Object>) data.get("showAdded");
        return (String) showAdded.get("title");
    }

    private void addShow(String title, int releaseYear, int seasons) {
        ExecutionResult result = dgsQueryExecutor.execute(
                "mutation { addShow(input: {title: \"%s\", releaseYear: %d, seasons: %d}) { id } }"
                        .formatted(title, releaseYear, seasons));
        assertThat(result.getErrors()).isEmpty();
    }
}
