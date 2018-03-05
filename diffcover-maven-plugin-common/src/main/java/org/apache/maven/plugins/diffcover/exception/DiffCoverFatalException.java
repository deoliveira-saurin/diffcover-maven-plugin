package org.apache.maven.plugins.diffcover.exception;

public class DiffCoverFatalException extends RuntimeException {

    public DiffCoverFatalException(Throwable throwable) {
        super(throwable);
    }

    public DiffCoverFatalException(String message) {
        super(message);
    }

    public DiffCoverFatalException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
