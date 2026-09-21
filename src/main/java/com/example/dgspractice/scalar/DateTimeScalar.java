package com.example.dgspractice.scalar;

import com.netflix.graphql.dgs.DgsScalar;
import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * ISO-8601 date-time scalar, e.g. "2024-05-01T10:30:00".
 *
 * <p>Note: LocalDateTime carries no timezone. Real "submitted at" timestamps should use
 * Instant or OffsetDateTime so clients in different zones agree on the instant.
 */
@DgsScalar(name = "DateTime")
public class DateTimeScalar implements Coercing<LocalDateTime, String> {

    /** Java -> JSON: what a datafetcher returned, on its way out. */
    @Override
    public String serialize(Object dataFetcherResult, GraphQLContext context, Locale locale)
            throws CoercingSerializeException {
        if (dataFetcherResult instanceof LocalDateTime dateTime) {
            return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        throw new CoercingSerializeException(
                "Expected a LocalDateTime but got: " + dataFetcherResult);
    }

    /** JSON -> Java: a value supplied through query variables. */
    @Override
    public LocalDateTime parseValue(Object input, GraphQLContext context, Locale locale)
            throws CoercingParseValueException {
        try {
            return LocalDateTime.parse(input.toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw new CoercingParseValueException(
                    "Not a valid ISO-8601 date-time: " + input, e);
        }
    }

    /** AST -> Java: a literal written inline in the query document. */
    @Override
    public LocalDateTime parseLiteral(Value<?> input, CoercedVariables variables,
                                      GraphQLContext context, Locale locale)
            throws CoercingParseLiteralException {
        if (input instanceof StringValue stringValue) {
            try {
                return LocalDateTime.parse(stringValue.getValue(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (DateTimeParseException e) {
                throw new CoercingParseLiteralException(
                        "Not a valid ISO-8601 date-time: " + stringValue.getValue(), e);
            }
        }
        throw new CoercingParseLiteralException(
                "Expected a String literal but got: " + input);
    }
}
