package org.ht.template;

public class IllegalSyntaxException extends RuntimeException {

    public IllegalSyntaxException(String message) {
        super(message);
    }

    public IllegalSyntaxException(String message, Exception e) {
        super(message);
        e.printStackTrace();
    }
}
