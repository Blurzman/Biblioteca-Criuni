package view;

import model.Book;

import java.util.Collection;
import java.util.Scanner;

/**
 * CLI for book-related interactions.
 * Handles all input prompts and output for book operations.
 * Type "salir" at any prompt to cancel the current operation.
 *
 * @author Samuel
 */
public class BookViewCLI implements IBookView {
    public BookViewCLI(){
        this.sc = new Scanner(System.in);
    }

    /**
     * @param books Collection of books to print, one per line.
     * @author Samuel
     */
    @Override
    public void showAll(Collection<Book> books){
        books.forEach(System.out::println);

    }

    /**
     * @return Raw input for the book title.
     * @author Samuel
     */
    public String askTitle(){
        System.out.println("Ingrese el titulo del libro por favor: ");
        return sc.nextLine();
    }

    /**
     * @return Raw input for the author name.
     * @author Samuel
     */
    public String askAuthor(){
        System.out.println("Ingrese el nombre del autor por favor: ");
        return sc.nextLine();
    }

    /**
     * @return Raw input for the year of publishing (parsed by the controller).
     * @author Samuel
     */
    public String askYearOfPublishing(){
        System.out.println("Ingrese el año de publicacion del libro por favor: ");
        return sc.nextLine();
    }

    /**
     * @return Raw input for the publisher name.
     * @author Samuel
     */
    public String askPublisher(){
        System.out.println("Ingrese el nombre de la editorial por favor: ");
        return sc.nextLine();
    }

    /**
     * @return Raw input for the initial stock.
     * @author Samuel
     */
    public String askStock(){
        System.out.println("Ingrese la cantidad de libros en stock por favor: ");
        return sc.nextLine();
    }

    /**
     * @return Raw input for a stock delta (positive to add, negative to remove).
     * @author Samuel
     */
    public String askDeltaStock(){
        System.out.println("Ingrese la cantidad de libros que quiere añadir o quitar por favor: ");
        return sc.nextLine();
    }

    /**
     * @return Raw input for the book UUID.
     * @author Samuel
     */
    public String askUuid() {
        System.out.println("Ingrese el uuid del libro por favor: ");
        return sc.nextLine();
    }

    /**
     * @return The selected edit option as a String.
     * @author Samuel
     */
    @Override
    public String askEditOption(){
        System.out.println("Que quiere editar?");
        System.out.println("1. Titulo");
        System.out.println("2. Autor");
        System.out.println("3. Año de salida");
        System.out.println("4. Editorial");
        System.out.println("5. Stock");
        System.out.println("Ingresar opcion: ");
        return sc.nextLine();
    }
    /**
     * @param message Error message to print.
     * @author Samuel
     */
    @Override
    public void showError(String message) {
        System.err.println(message);
    }

    private Scanner sc = null;
}
