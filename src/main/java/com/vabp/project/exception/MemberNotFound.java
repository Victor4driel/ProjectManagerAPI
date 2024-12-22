package com.vabp.project.exception;

public class MemberNotFound extends RuntimeException {
    public MemberNotFound(String message) {
        super(message);
    }

    public MemberNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberNotFound(Throwable cause) {
        super(cause);
    }

    public MemberNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
