package data;

import model.Book;


import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

/**
 * Storage class for book.
 * Uses the book's UUID as the unique key.
 *
 * @author Samuel
 */
public class BookData implements BaseData<Book>, Serializable {
    public BookData(){
        this.books = new HashMap<>();
    }

    /**
     * @param book Book to store.
     * @throws IllegalArgumentException if a book with the same UUID already exists.
     * @author Samuel
     */
    public void add(Book book){
        if (!books.containsKey(book.getUuid())){
            books.put(book.getUuid(),book);
        } else {
            throw new IllegalArgumentException("Error inesperado. Intentelo de nuevo"); // Shouldn't ever happen
        }
    }

    /**
     * @param book Book to remove.
     * @throws IllegalArgumentException if no book with that UUID exists.
     * @author Samuel
     */
    public void remove(Book book){
        if (books.containsKey(book.getUuid())){
            books.remove(book.getUuid());
        } else {
            throw new IllegalArgumentException("Ese libro no existe"); // Shouldn't happen either
        }
    }

    /**
     * @param uuid UUID of the book to retrieve.
     * @return The matching Book.
     * @throws IllegalArgumentException if no book with that UUID exists.
     * @author Samuel
     */
    public Book get(String uuid){
        if (books.containsKey(uuid)){
            return books.get(uuid);
        } else {
            throw new IllegalArgumentException("Ese libro no existe");
        }
    }

    /**
     * @return All stored books.
     * @author Samuel
     */
    public Collection<Book> getAll(){
        return books.values();
    }

    private HashMap<String, Book> books = null;
}
