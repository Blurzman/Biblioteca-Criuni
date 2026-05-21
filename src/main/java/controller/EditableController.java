package controller;

/**
 * Interface for controllers that support edit operations.
 * Implemented by controllers whose corresponding entity can be edited.
 *
 * @author Samuel
 */
public interface EditableController {
    /**
     * Starts the edit flow for an existing entity.
     *
     * @author Samuel
     */
    void edit();
}
