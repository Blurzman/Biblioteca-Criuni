package View;

import java.util.Collection;

/**
 * Generic interface for console views.
 *
 * @param <T> the type of entity this view displays.
 * @author Samuel
 */
public interface BaseView<T> {
    /**
     * Displays an error message to the user.
     *
     * @param message the error message.
     * @author Samuel
     */
    void showError(String message);

    /**
     * Displays all items in the collection to the user.
     *
     * @param item the collection of items to display.
     * @author Samuel
     */
    void showAll(Collection<T> item);
}
