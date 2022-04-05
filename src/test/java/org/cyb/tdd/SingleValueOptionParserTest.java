package org.cyb.tdd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.function.Function;

import static java.util.Arrays.asList;
import static org.cyb.tdd.BooleanParserTest.option;
import static org.junit.jupiter.api.Assertions.*;

class SingleValueOptionParserTest {

    @Test
    public void should_not_accept_extra_argument_for_single_valued_option() {
        TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
           new SingleValueOptionParser<>(Integer::parseInt, 0).parse(asList("-p", "8080", "8081"), option("p"));
        });

        assertEquals("p", e.getValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-p -l", "-p"})
    public void should_not_accept_insufficient_argument_for_single_valued_option(String arguments) {
        InSufficientArgumentsException e = assertThrows(InSufficientArgumentsException.class, () -> {
           new SingleValueOptionParser<>(Integer::parseInt, 0).parse(asList(arguments.split(" ")), option("p"));
        });

        assertEquals("p", e.getValue());
    }

    @Test
    public void should_set_default_value_to_0_for_int_option() {
        assertEquals(0, new SingleValueOptionParser<>(Integer::parseInt, 0).parse(asList(), option("p")));
    }

    @Test
    public void should_parse_int_value_if_flag_present() {
        assertEquals(8080, new SingleValueOptionParser<>(Integer::parseInt,0).parse(asList("-p", "8080"), option("p")));

                ;
    }

    @Test
    public void should_not_accept_extra_argument_for_string_single_valued_option() {
        TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
            new SingleValueOptionParser<>(String::valueOf, "").parse(asList("-d", "/usr/logs", "/usr/vars"), option("d"));
        });

        assertEquals("d", e.getValue());
    }

    @Test
    public void should_set_default_value_for_valued_option() {
        Function<String, Object> whatever = (it) -> null;
        Object defaultValue = new Object();
        assertSame(defaultValue, new SingleValueOptionParser<>(whatever, defaultValue).parse(asList(), option("p")));
    }

    @Test
    public void should_parse_value_if_flag_present() {
        Object parsed = new Object();
        Function<String, Object> parse = (it) -> parsed;
        Object whatever = new Object();
        assertSame(parsed, new SingleValueOptionParser<>(parse, whatever).parse(asList("-p", "8080"), option("p")));
    }


}