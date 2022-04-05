package org.cyb.tdd;

public class TooManyArgumentsException extends RuntimeException {
    String value;

    public TooManyArgumentsException(String option) {
        super();
        this.value = option;
    }

    public String getValue() {
        return value;
    }
}
