package by.KirillBukato.dependency.exceptions;

public class ApplicationContextNotStartedException extends RuntimeException {
    public ApplicationContextNotStartedException() {
        super("Context is not started");
    }
}
