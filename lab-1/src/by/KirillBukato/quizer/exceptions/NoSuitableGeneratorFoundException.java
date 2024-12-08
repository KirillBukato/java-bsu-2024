package by.KirillBukato.quizer.exceptions;

public class NoSuitableGeneratorFoundException extends RuntimeException {
    public NoSuitableGeneratorFoundException() {
        super("Each generator in the group generator throws an exception");
    }
}
