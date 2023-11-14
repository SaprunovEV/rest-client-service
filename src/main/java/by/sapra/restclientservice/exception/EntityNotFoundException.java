package by.sapra.restclientservice.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String format) {
        super(format);
    }
}
