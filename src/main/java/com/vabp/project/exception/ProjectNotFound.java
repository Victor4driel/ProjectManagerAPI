package com.vabp.project.exception;

public class ProjectNotFound extends RuntimeException {
    public ProjectNotFound(String message) {
        super(message);
    }

    public ProjectNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public ProjectNotFound(Throwable cause) {
        super(cause);
    }

    public ProjectNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
