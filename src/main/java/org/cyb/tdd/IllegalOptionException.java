package org.cyb.tdd;

public class IllegalOptionException extends RuntimeException {
    private String value;

    public IllegalOptionException(String value) {
        super();
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
