package fr.vrecomsys.app.exceptions;

public class UnvalidInputException extends RuntimeException {
    public UnvalidInputException(String message) {
        super(message);
    }
}
