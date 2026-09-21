package com.example.dgspractice.datafetcher;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GraphQlMetricsTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    MeterRegistry meterRegistry;

    @Test
    void resolverTimerIsRecordedPerField() {
        dgsQueryExecutor.executeAndExtractJsonPath("{ shows { title } }", "data.shows[*].title");

        Timer timer = meterRegistry.find("gql.resolver")
                .tag("gql.field", "Query.shows")
                .timer();

        assertThat(timer).as("gql.resolver timer for Query.shows").isNotNull();
        assertThat(timer.count()).isPositive();
    }

    @Test
    void errorsAreCountedByTypedErrorCode() {
        dgsQueryExecutor.execute("{ showById(id: 99) { title } }");

        assertThat(meterRegistry.find("gql.error").tag("gql.errorCode", "NOT_FOUND").counter())
                .as("gql.error counter tagged NOT_FOUND")
                .isNotNull();
    }
}
