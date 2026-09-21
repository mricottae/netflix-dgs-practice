package com.example.dgspractice.datafetcher;

import com.example.dgspractice.repository.ShowRepository;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import graphql.ExecutionResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AddShowValidationTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    ShowRepository showRepository;

    @Test
    void blankTitleIsRejectedAsBadRequest() {
        long before = showRepository.count();

        ExecutionResult result = dgsQueryExecutor.execute(
                """
                mutation { addShow(input: {title: "", releaseYear: 2020, seasons: 2}) { id } }
                """);

        assertThat(result.getErrors()).hasSize(1);
        assertThat(result.getErrors().getFirst().getExtensions())
                .containsEntry("errorType", "BAD_REQUEST");
        assertThat(result.getErrors().getFirst().getMessage()).contains("title: must not be blank");
        assertThat(showRepository.count()).isEqualTo(before);
    }

    @Test
    void everyViolationIsReportedAtOnce() {
        ExecutionResult result = dgsQueryExecutor.execute(
                """
                mutation { addShow(input: {title: "Dark", releaseYear: 1500, seasons: 0}) { id } }
                """);

        assertThat(result.getErrors().getFirst().getMessage())
                .contains("releaseYear: must be 1888 or later")
                .contains("seasons: must be greater than zero");
    }

    @Test
    void validInputIsPersisted() {
        String title = dgsQueryExecutor.executeAndExtractJsonPath(
                """
                mutation { addShow(input: {title: "Dark", releaseYear: 2017, seasons: 3}) { title } }
                """,
                "data.addShow.title");

        assertThat(title).isEqualTo("Dark");
    }
}
