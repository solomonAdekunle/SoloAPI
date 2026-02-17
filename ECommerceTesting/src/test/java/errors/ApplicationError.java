package errors;

/**
 * Application Error Represents a fatal error in the application
 *
 * @author C0953512 Nick Lee
 */
public class ApplicationError {

    private final String code;
    private final ApplicationErrorType type;

    /**
     * @param code the application error code
     * @param type the application error type i.e. SVC, WEC
     */
    public ApplicationError(String code, ApplicationErrorType type) {
        this.code = code;
        this.type = type;
    }

    /**
     * Get the error code from the data layer
     *
     * @return the error code which can be checked in SHAPE
     */
    public String getCode() {
        return code;
    }

    /**
     * Get the type of error which will be SVC, WEC or UNKNOWN if the page failed to load
     *
     * @return the error type
     */
    public ApplicationErrorType getType() {
        return type;
    }

    /**
     * Enum representing error types
     */
    public enum ApplicationErrorType {
        WEC, SVC, UNKNOWN
    }
}
