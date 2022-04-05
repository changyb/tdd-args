package org.cyb.tdd;

public class TooManyArgumentsException extends RuntimeException {
    private String value;

    public TooManyArgumentsException(String option) {
        super();
        this.value = option;
    }

    public String getValue() {
        return value;
    }
}
