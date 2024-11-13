package by.KirillBukato.dependency.exceptions;

import java.util.ArrayList;

public class RecursiveInjectionException extends RuntimeException {

    private static String constructMessage(String name, ArrayList<String> injections) {
        injections.add(name);
        return "Can't inject " + name + " because of recursive injection: " + injections;
    }

    public RecursiveInjectionException(String name, ArrayList<String> injections) {
        super(constructMessage(name, injections));
    }
}
