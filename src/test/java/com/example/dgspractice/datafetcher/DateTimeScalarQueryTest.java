package com.example.dgspractice.datafetcher;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import graphql.ExecutionResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DateTimeScalarQueryTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    void serializesSubmittedDateAsIsoString() {
        List<String> dates = dgsQueryExecutor.executeAndExtractJsonPath(
                "{ shows { reviews { submittedDate } } }",
                "data.shows[*].reviews[*].submittedDate");

        assertThat(dates).contains("2024-01-15T09:00:00");
    }

    @Test
    void parsesInlineLiteral() {
        List<String> usernames = dgsQueryExecutor.executeAndExtractJsonPath(
                "{ reviewsSince(since: \"2025-01-01T00:00:00\") { username } }",
                "data.reviewsSince[*].username");

        assertThat(usernames).containsExactlyInAnyOrder("the critic", "dan");
    }

    @Test
    void parsesVariable() {
        List<String> usernames = dgsQueryExecutor.executeAndExtractJsonPath(
                "query R($since: DateTime!) { reviewsSince(since: $since) { username } }",
                "data.reviewsSince[*].username",
                Map.of("since", "2025-01-01T00:00:00"));

        assertThat(usernames).containsExactlyInAnyOrder("the critic", "dan");
    }

    @Test
    void rejectsMalformedDateBeforeAnyResolverRuns() {
        ExecutionResult result = dgsQueryExecutor.execute(
                "{ reviewsSince(since: \"not-a-date\") { username } }");

        assertThat(result.getErrors()).hasSize(1);
        assertThat(result.getErrors().getFirst().getMessage())
                .contains("Not a valid ISO-8601 date-time");
    }
}
