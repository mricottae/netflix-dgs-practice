package com.example.dgspractice.exception;

import com.netflix.graphql.dgs.exceptions.DefaultDataFetcherExceptionHandler;
import com.netflix.graphql.types.errors.TypedGraphQLError;
import graphql.execution.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class ShowDataFetcherExceptionHandler implements DataFetcherExceptionHandler {

    private final DataFetcherExceptionHandler defaultHandler = new DefaultDataFetcherExceptionHandler();

    @Override
    public CompletableFuture<DataFetcherExceptionHandlerResult> handleException(
            DataFetcherExceptionHandlerParameters params) {

        if (params.getException() instanceof ShowNotFoundException ex) {
            var error = TypedGraphQLError.newNotFoundBuilder()
                    .message(ex.getMessage())
                    .path(params.getPath())
                    .build();
            return CompletableFuture.completedFuture(
                    DataFetcherExceptionHandlerResult.newResult(error).build());
        }
        return defaultHandler.handleException(params);
    }
}
