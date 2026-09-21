package com.example.dgspractice.scalar;

import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.IntValue;
import graphql.language.StringValue;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateTimeScalarTest {

    private final DateTimeScalar scalar = new DateTimeScalar();
    private final GraphQLContext context = GraphQLContext.newContext().build();
    private final Locale locale = Locale.getDefault();

    @Test
    void serializesToIsoString() {
        LocalDateTime value = LocalDateTime.of(2024, 5, 1, 10, 30, 0);

        assertThat(scalar.serialize(value, context, locale)).isEqualTo("2024-05-01T10:30:00");
    }

    @Test
    void serializeRejectsNonDateTime() {
        assertThatThrownBy(() -> scalar.serialize("2024-05-01", context, locale))
                .isInstanceOf(CoercingSerializeException.class);
    }

    @Test
    void parseValueReadsIsoString() {
        assertThat(scalar.parseValue("2024-05-01T10:30:00", context, locale))
                .isEqualTo(LocalDateTime.of(2024, 5, 1, 10, 30, 0));
    }

    @Test
    void parseValueRejectsOtherFormats() {
        assertThatThrownBy(() -> scalar.parseValue("01/05/2024", context, locale))
                .isInstanceOf(CoercingParseValueException.class);
    }

    @Test
    void parseLiteralReadsStringValue() {
        StringValue literal = StringValue.newStringValue("2024-05-01T10:30:00").build();

        assertThat(scalar.parseLiteral(literal, CoercedVariables.emptyVariables(), context, locale))
                .isEqualTo(LocalDateTime.of(2024, 5, 1, 10, 30, 0));
    }

    @Test
    void parseLiteralRejectsNonStringLiteral() {
        IntValue literal = IntValue.newIntValue(BigInteger.valueOf(2024)).build();

        assertThatThrownBy(() ->
                scalar.parseLiteral(literal, CoercedVariables.emptyVariables(), context, locale))
                .isInstanceOf(CoercingParseLiteralException.class);
    }
}
