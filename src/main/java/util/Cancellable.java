package util;

/**
 * Abstract base class for controllers that support cancellable input flows.
 * Extend this class to get access to {@link #checkCancel(String)}.
 *
 * @author Samuel
 */
public abstract class Cancellable {
    protected Cancellable() {}

    /**
     * Throws {@link CancelException} if the input equals {@code "salir"}.
     * @param input the user input to check.
     * @throws CancelException if the user wants to cancel.
     */
    protected static void checkCancel(String input) {
        if (input.equalsIgnoreCase("salir")) throw new CancelException();
    }
}
