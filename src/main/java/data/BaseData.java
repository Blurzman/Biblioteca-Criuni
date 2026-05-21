package data;

import java.util.Collection;

/**
 * Generic interface for in-memory data storage.
 *
 * @param <T> the type of entity being stored.
 * @author Samuel
 */
public interface BaseData<T> {
    /**
     * Adds an item to the store.
     *
     * @param item the item to add.
     * @throws IllegalArgumentException if the item already exists.
     * @author Samuel
     */
    void add(T item);

    /**
     * Removes an item from the store.
     *
     * @param item the item to remove.
     * @throws IllegalArgumentException if the item does not exist.
     * @author Samuel
     */
    void remove(T item);

    /**
     * Retrieves an item by its unique identifier.
     *
     * @param id the unique identifier.
     * @return the matching item.
     * @throws IllegalArgumentException if no item with that id exists.
     * @author Samuel
     */
    T get(String id);

    /**
     * @return all stored items.
     * @author Samuel
     */
    Collection<T> getAll();
}
