package util;

import java.util.concurrent.CancellationException;

/**
 * Thrown when the user types {@code "salir"} at any input prompt to cancel the current operation.
 *
 * @author Samuel
 */
public class CancelException extends CancellationException {
    public CancelException(){
        super("Operacion Cancelada");
    }
}
