package org.cyb.tdd;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

class OptionParsers {

    public static OptionParser<Boolean> bool() {
        return ((arguments, option) -> values(arguments, option, 0)
                .map(it -> true).orElse(false));
    }

    public static <T> OptionParser<T> unary(Function<String, T> valueParser, T defaultValue) {
        return ((arguments, option) -> values(arguments, option, 1).map(it -> parseValue(it.get(0), valueParser)).orElse(defaultValue));
    }

    public static <T> OptionParser<T[]> list(IntFunction<T[]> generator, Function<String, T> valueParser) {
        return ((arguments, option) -> values(arguments, option)
                .map(it -> it.stream().map(value -> parseValue(value, valueParser))
                        .toArray(generator)).orElse(generator.apply(0)));
    }

    private static Optional<List<String>> values(List<String> arguments, Option option) {
        int index = arguments.indexOf("-" + option.value());

        if (index == -1) {
            return Optional.empty();
        }

        return Optional.of(values(arguments, index));

    }

    private static Optional<List<String>> values(List<String> arguments, Option option, int expectedSize) {
        int index = arguments.indexOf("-" + option.value());

        if (index == -1) {
            return Optional.empty();
        }

        List<String> values = values(arguments, index);

        if (values.size() < expectedSize) {
            throw new InSufficientArgumentsException(option.value());
        }
        if (values.size() > expectedSize) {
            throw new TooManyArgumentsException(option.value());
        }
        return Optional.of(values);

    }

    private static <T> T parseValue(String value, Function<String, T> valueParser) {
        return valueParser.apply(value);
    }

    private static List<String> values(List<String> arguments, int index) {
        int followingFlag = IntStream.range(index + 1, arguments.size())
                .filter(it -> arguments.get(it).matches("^-[a-zA-Z-]+$"))
                .findFirst().orElse(arguments.size());

        return arguments.subList(index + 1, followingFlag);
    }

}
