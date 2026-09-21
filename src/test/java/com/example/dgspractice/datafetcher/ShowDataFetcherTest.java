package com.example.dgspractice.datafetcher;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ShowDataFetcherTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    void showsByTitleReturnsMatches() {
        List<String> titles = dgsQueryExecutor.executeAndExtractJsonPath(
                "{ shows(titleFilter: \"the\") { title } }",
                "data.shows[*].title");

        assertThat(titles).containsExactly("The Crown");
    }
}
