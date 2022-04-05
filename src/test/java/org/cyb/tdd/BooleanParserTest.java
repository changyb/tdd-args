package org.cyb.tdd;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class BooleanParserTest {

    @Test
    public void should_not_accept_extra_argument_for_boolean_option() {
        TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class,
                () -> {
            new BooleanParser().parse(asList("-l", "t"), option("l"));
                });
        assertEquals("l", e.getValue());
    }

    @Test
    public void should_set_default_value_to_false_if_option_not_present() {
        assertFalse(new BooleanParser().parse(asList(), option("l")));
    }

    static Option option(String value) {
        return new Option() {

            public Class<? extends Annotation> annotationType() {
                return Option.class;
            }

            @Override
            public String value() {
                return value;
            }
        };
    }

}