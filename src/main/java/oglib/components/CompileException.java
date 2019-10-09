package oglib.components;

public class CompileException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 3697239313211740347L;

    public CompileException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompileException(String message) {
        super(message);
    }

    public CompileException() {
        super();
    }

    public CompileException(Throwable cause) {
        super(cause);
    }
}