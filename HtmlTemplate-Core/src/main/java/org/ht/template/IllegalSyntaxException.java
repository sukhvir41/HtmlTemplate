package org.ht.template;

import java.util.Optional;

public class IllegalSyntaxException extends RuntimeException {

    private Exception exception;

    public IllegalSyntaxException(String message) {
        super(message);
    }

    public IllegalSyntaxException(String message, Exception e) {
        super(message);
        this.exception = e;
    }


    @Override
    public void printStackTrace() {
        super.printStackTrace();
        Optional.of(exception)
                .ifPresent(Exception::printStackTrace);
    }
}
