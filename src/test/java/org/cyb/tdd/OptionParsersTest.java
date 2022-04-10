package org.cyb.tdd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.annotation.Annotation;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static org.cyb.tdd.OptionParsersTest.BooleanParserTest.option;
import static org.junit.jupiter.api.Assertions.*;

class OptionParsersTest {

    @Nested
    class UnaryOptionParser {
        @Test
        public void should_not_accept_extra_argument_for_single_valued_option() {
            TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
                OptionParsers.unary(Integer::parseInt, 0).parse(asList("-p", "8080", "8081"), option("p"));
            });

            assertEquals("p", e.getValue());
        }

        @ParameterizedTest
        @ValueSource(strings = {"-p -l", "-p"})
        public void should_not_accept_insufficient_argument_for_single_valued_option(String arguments) {
            InSufficientArgumentsException e = assertThrows(InSufficientArgumentsException.class, () -> {
                OptionParsers.unary(Integer::parseInt, 0).parse(asList(arguments.split(" ")), option("p"));
            });

            assertEquals("p", e.getValue());
        }

        @Test
        public void should_set_default_value_to_0_for_int_option() {
            assertEquals(0, OptionParsers.unary(Integer::parseInt, 0).parse(asList(), option("p")));
        }

        @Test
        public void should_parse_int_value_if_flag_present() {
            assertEquals(8080, OptionParsers.unary(Integer::parseInt, 0).parse(asList("-p", "8080"), option("p")));

            ;
        }

        @Test
        public void should_not_accept_extra_argument_for_string_single_valued_option() {
            TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
                OptionParsers.unary(String::valueOf, "").parse(asList("-d", "/usr/logs", "/usr/vars"), option("d"));
            });

            assertEquals("d", e.getValue());
        }

        @Test
        public void should_set_default_value_for_valued_option() {
            Function<String, Object> whatever = (it) -> null;
            Object defaultValue = new Object();
            assertSame(defaultValue, OptionParsers.unary(whatever, defaultValue).parse(asList(), option("p")));
        }

        @Test
        public void should_parse_value_if_flag_present() {
            Object parsed = new Object();
            Function<String, Object> parse = (it) -> parsed;
            Object whatever = new Object();
            assertSame(parsed, OptionParsers.unary(parse, whatever).parse(asList("-p", "8080"), option("p")));
        }
    }

    @Nested
    class BooleanParserTest {

        @DisplayName("sad path")
        @Test
        public void should_not_accept_extra_argument_for_boolean_option() {
            TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class,
                    () -> {
                OptionParsers.bool().parse(asList("-l", "t"), option("l"));
                    });
            assertEquals("l", e.getValue());
        }

        @DisplayName("default value")
        @Test
        public void should_set_default_value_to_false_if_option_not_present() {
            assertFalse(OptionParsers.bool().parse(asList(), option("l")));
        }

        @DisplayName("happy path")
        @Test
        public void should_set_value_to_true_if_option_present() {
            assertTrue(OptionParsers.bool().parse(asList("-l"), option("l")));
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

    @Nested
    class ListOptionParser {

        @Test
        public void should_not_treat_negative_int_as_flag() {
            Integer[] value = OptionParsers.list(Integer[]::new, Integer::parseInt).parse(asList("-g","-1", "-2"), option("g"));
            assertArrayEquals(new Integer[]{-1, -2}, value);
        }

        @Test
        public void should_parse_list_value() {
            String[] value = OptionParsers.list(String[]::new, String::valueOf).parse(asList("-g", "this", "is"), option("g"));
            assertArrayEquals(new String[] {"this", "is"}, value);
        }

        @Test
        public void should_use_empty_array_as_default_value() {
            String[] value =
                    OptionParsers.list(String[]::new, String::valueOf).parse(asList(), option("g"));
            assertEquals(0, value.length);
        }

        @Test
        public void should_throw_exception_if_value_parser_cant_parse_value() {
            Function<String, String> parser = (it) -> {throw new RuntimeException();};
            assertThrows(RuntimeException.class, () ->
                    OptionParsers.list(String[]::new, parser).parse(asList("-g", "this", "is"), option("g")));
        }
    }
}