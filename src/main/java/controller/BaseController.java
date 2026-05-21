package controller;



/**
 * Base interface for all controllers.
 * Every controller must support registering a new entity.
 *
 * @author Samuel
 */
public interface BaseController {
    /**
     * Starts the registration flow for a new entity.
     * @author Samuel
     */
    public void register();

    /**
     * Starts the remove flow from the storage
     * @author Samuel
     */
    public void remove();

    /**
     * Asks the view to show all items in storage
     * @author Samuel
     */
    public void showAll();

}
