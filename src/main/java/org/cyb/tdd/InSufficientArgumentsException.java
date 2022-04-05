package org.cyb.tdd;

public class InSufficientArgumentsException extends RuntimeException {
    private String value;

    public InSufficientArgumentsException(String value) {
        super();
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
