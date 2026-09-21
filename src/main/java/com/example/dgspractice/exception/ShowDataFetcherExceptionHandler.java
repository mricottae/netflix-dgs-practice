package com.example.dgspractice.exception;

import com.netflix.graphql.dgs.exceptions.DefaultDataFetcherExceptionHandler;
import com.netflix.graphql.types.errors.TypedGraphQLError;
import graphql.execution.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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

        if (params.getException() instanceof ConstraintViolationException ex) {
            var error = TypedGraphQLError.newBadRequestBuilder()
                    .message(describe(ex))
                    .path(params.getPath())
                    .build();
            return CompletableFuture.completedFuture(
                    DataFetcherExceptionHandlerResult.newResult(error).build());
        }

        return defaultHandler.handleException(params);
    }

    /** Turns "add.addShowInput.title: must not be blank" into "title: must not be blank". */
    private String describe(ConstraintViolationException ex) {
        return ex.getConstraintViolations().stream()
                .map(violation -> leafProperty(violation) + ": " + violation.getMessage())
                .sorted()
                .collect(Collectors.joining(", "));
    }

    private String leafProperty(ConstraintViolation<?> violation) {
        String property = "";
        for (Path.Node node : violation.getPropertyPath()) {
            property = node.getName();
        }
        return property;
    }
}
