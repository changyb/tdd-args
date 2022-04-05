package org.cyb.tdd;

import java.util.List;
import java.util.function.Function;

class SingleValueOptionParser<T> implements OptionParser<T> {
    Function<String, T> valueParser;
    private T defaultValue;

    public SingleValueOptionParser(Function<String, T> valueParser, T defaultValue) {
        this.valueParser = valueParser;
        this.defaultValue = defaultValue;
    }

    @Override
    public T parse(List<String> arguments, Option option) {
        int index = arguments.indexOf("-" + option.value());

        if (index == -1) {
            return defaultValue;
        }

        if (index + 1 == arguments.size() ||
                arguments.get(index + 1).startsWith("-")) {
            throw new InSufficientArgumentsException(option.value());
        }
        if (index + 2 < arguments.size()
        && !arguments.get(index + 2).startsWith("-" + option.value())) {
            throw new TooManyArgumentsException(option.value());
        }

        return valueParser.apply(arguments.get(index + 1));
    }

}
