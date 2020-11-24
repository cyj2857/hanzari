package com.hancom.hanzari.exception;

@SuppressWarnings("serial")
public class SigninFailedException extends RuntimeException {
    public SigninFailedException(String msg, Throwable t) {
        super(msg, t);
    }

    public SigninFailedException(String msg) {
        super(msg);
    }

    public SigninFailedException() {
        super();
    }
}