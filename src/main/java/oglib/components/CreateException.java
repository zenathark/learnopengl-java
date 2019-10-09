package oglib.components;

public class CreateException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1769392495945457875L;

    public CreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateException(String message) {
        super(message);
    }

    public CreateException() {
        super();
    }

    public CreateException(Throwable cause) {
        super(cause);
    }
}