package model;


import java.io.Serializable;
import java.time.Year;

/**
 * Represents a book in the library.
 * Each book is assigned a unique UUID on creation.
 *
 * @author Samuel
 */
public class Book implements Serializable {

    public Book(){}

    public String getTitle() {
        return title;
    }

    /**
     * @param title Book title. Letters and spaces only.
     * @throws IllegalArgumentException if it contains non-letter characters.
     * @author Samuel
     */
    public void setTitle(String title) {
        if (title.matches(ALPHASPACES)){
            this.title = title;
        } else {
            throw new IllegalArgumentException("Titulo invalido. Solo letras y espacios");
        }
    }

    public String getAuthor() {
        return author;
    }

    /**
     * @param author Author name. Letters and spaces only.
     * @throws IllegalArgumentException if it contains non-letter characters.
     * @author Samuel
     */
    public void setAuthor(String author) {
        if (author.matches(ALPHASPACES)){
            this.author = author;
        } else {
            throw new IllegalArgumentException("Autor invalido. Solo letras y espacios");
        }
    }

    public Year getYearOfPublishing() {
        return yearOfPublishing;
    }

    /**
     * @param yearOfPublishing Year the book was published. Must be before the current year.
     * @throws IllegalArgumentException if {@code yearOfPublishing} is in the future.
     * @author Samuel
     */
    public void setYearOfPublishing(Year yearOfPublishing) {
        if (yearOfPublishing.isBefore(Year.now())){
            this.yearOfPublishing = yearOfPublishing;
        } else {
            throw new IllegalArgumentException("El año de publicacion no puede ser en el futuro");
        }
    }

    public String getPublisher() {
        return publisher;
    }

    /**
     * @param publisher Publisher name. Letters and spaces only.
     * @throws IllegalArgumentException if it contains non-letter characters.
     * @author Samuel
     */
    public void setPublisher(String publisher) {
        if (publisher.matches(ALPHASPACES)){
            this.publisher = publisher;
        } else {
            throw new IllegalArgumentException("Editorial invalida. Solo letras y espacios");
        }
    }

    public String getIsbn() {
        return isbn;
    }

    /**
     * @param isbn ISBN-13 of the book. Must be exactly 13 digits.
     * @throws IllegalArgumentException if the format is invalid.
     * @author Samuel
     */
    public void setIsbn(String isbn) {
        if (isbn.matches("^[0-9]{13}$")) {
            this.isbn = isbn;
        } else {
            throw new IllegalArgumentException("ISBN invalido. Debe tener 13 digitos");
        }
    }

    public int getStock(){
        return this.stock;
    }

    /**
     * @param stock New stock value. Must be zero or greater.
     * @throws IllegalArgumentException if stock is negative.
     * @author Samuel
     */
    public void setStock(int stock){
        if (stock >= 0){
            this.stock = stock;
        } else {
            throw new IllegalArgumentException("El stock no puede ser menor a cero");
        }
    }

    /**
     * @return {@code true} if {@link #getStock()} is greater than {@code 0}.
     * @author Samuel
     */
    public boolean isAvailable(){
        return this.stock > 0;
    }

    @Override
    public String toString(){
        return String.format("[%s] Titulo: %-20s | Autor: %-20s | %s | Editorial: %-10s | (%d)",
                isbn, title, author, yearOfPublishing.toString(), publisher, stock);
    }

    public static final String ALPHASPACES = "^[a-zA-Z ]+$";

    private String title = null;
    private String author = null;
    private Year yearOfPublishing = null;
    private String publisher = null;
    private String isbn = null;
    private int stock = 0;
}
