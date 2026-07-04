package exception;

/**
 * InvalidInputException.java
 * Thrown when user-supplied data fails validation checks.
 * Carries the name of the failing field for UI feedback.
 */
public class InvalidInputException extends RuntimeException {
    private final String fieldName;

    public InvalidInputException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public InvalidInputException(String message) {
        super(message);
        this.fieldName = null;
    }

    /** @return The form field that caused the validation failure, or null. */
    public String getFieldName() { return fieldName; }
}
