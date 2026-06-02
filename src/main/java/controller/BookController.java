package controller;

import data.BookData;
import model.Book;
import util.CancelException;
import util.Cancellable;
import view.BookViewCLI;
import view.IBookView;

import java.time.DateTimeException;
import java.time.Year;

/**
 * controller for book operations.
 * Handles registration, editing, and stock management of books,
 * coordinating between {@link BookViewCLI} and {@link data.BookData}.
 *
 * @author Samuel
 */
public class BookController extends Cancellable implements BaseController, EditableController {
    public BookController(IBookView view, BookData data){
        this.view = view;
        this.data = data;
    }

    /**
     * Collects all book fields from the user and saves the book.
     * Cancellable via {@code "salir"}.
     *
     * @author Samuel
     */
    @Override
    public void register(){
        Book book = new Book();
        try {
            registerIsbn(book);
            registerTitle(book);
            registerAuthor(book);
            registerYearOfPublishing(book);
            registerPublisher(book);
            registerStock(book);
            data.add(book);
        } catch (IllegalArgumentException | CancelException e){
            view.showError(e.getMessage());
        }
    }

    /**
     * Looks up a book by {@code UUID} and lets the user edit one field.
     * Cancellable via {@code "salir"}.
     *
     * @author Samuel
     */
    @Override
    public void edit() {
        String isbn = view.askIsbn();
        try {
            checkCancel(isbn);
            Book book = data.get(isbn);
            String option = view.askEditOption();
            checkCancel(option);
            switch (Integer.parseInt(option)) {
                case (1):
                    registerTitle(book);
                    break;
                case (2):
                    registerAuthor(book);
                    break;
                case (3):
                    registerYearOfPublishing(book);
                    break;
                case (4):
                    registerPublisher(book);
                    break;
                case (5):
                    changeStock(book);
                    break;
                default:
                    view.showError("Opcion Invalida");
                    break;
            }
        } catch (NumberFormatException e) {
            view.showError("Por favor ingrese un numero valido");
        } catch (IllegalArgumentException | CancelException e){
            view.showError(e.getMessage());
        }
    }

    /**
     * Looks up a book from the storage and removes it.
     * Cancellable via {@code "salir"}.
     *
     * @author Samuel
     */
    @Override
    public void remove() {
        boolean valid = false;
        do{
            try {
                String isbn = view.askIsbn();
                checkCancel(isbn);
                Book book = data.get(isbn);
                data.remove(book);
                valid = true;
            } catch (IllegalArgumentException | CancelException e){
                view.showError(e.getMessage());
            }
        } while (!valid);
    }

    /**
     * Shows all the Books in the storage
     *
     * @author Samuel
     */
    @Override
    public void showAll(){
        view.showAll(data.getAll());
    }


    /**
     * Prompts for a delta and adjusts the book's stock.
     * Accepts negative values to reduce stock.
     *
     * @param book Book whose stock will be updated.
     * @author Samuel
     */
    public void changeStock(Book book){
        boolean valid = false;
        do {
            String deltaStock = view.askDeltaStock();
            checkCancel(deltaStock);
            try {
                book.setStock(book.getStock() + Integer.parseInt(deltaStock));
                valid = true;
            } catch (NumberFormatException e) {
                view.showError("Ingrese un numero valido por favor");
            } catch (IllegalArgumentException e) {
                view.showError(e.getMessage());
            }
        } while (!valid);
    }

    /**
     * Prompts until a valid ISBN-13 is entered and sets it on the book.
     *
     * @author Samuel
     */
    private void registerIsbn(Book book){
        boolean valid = false;
        do{
            String isbn = view.askIsbn();
            checkCancel(isbn);
            try {
                book.setIsbn(isbn);
                valid = true;
            } catch (IllegalArgumentException e) {
                view.showError(e.getMessage());
            }
        } while (!valid);
    }

    /**
     * Prompts until a valid title is entered and sets it on the book.
     *
     * @author Samuel
     */
    private void registerTitle(Book book){
        boolean valid = false;
        do{
            String title = view.askTitle();
            checkCancel(title);
            try {
                book.setTitle(title);
                valid = true;
            } catch (IllegalArgumentException e) {
                view.showError(e.getMessage());
            }
        } while (!valid);
    }

    /**
     * Prompts until a valid author name is entered and sets it on the book.
     *
     * @author Samuel
     */
    private void registerAuthor(Book book){
        boolean valid = false;
        do{
            String author = view.askAuthor();
            checkCancel(author);
            try {
                book.setAuthor(author);
                valid = true;
            } catch (IllegalArgumentException e) {
                view.showError(e.getMessage());
            }
        } while (!valid);
    }

    /**
     * Prompts until a valid year is entered and sets it on the book.
     *
     * @author Samuel
     */
    private void registerYearOfPublishing(Book book){
        boolean valid = false;
        do{
            String yearOfPublishing = view.askYearOfPublishing();
            checkCancel(yearOfPublishing);
            try {
                book.setYearOfPublishing(Year.parse(yearOfPublishing));
                valid = true;
            } catch (DateTimeException e){
                 view.showError("Ingrese un año valido");
            } catch (IllegalArgumentException e) {
                view.showError(e.getMessage());
            }
        } while (!valid);
    }

    /**
     * Prompts until a valid publisher name is entered and sets it on the book.
     *
     * @author Samuel
     */
    private void registerPublisher(Book book){
        boolean valid = false;
        do{
            String publisher = view.askPublisher();
            checkCancel(publisher);
            try {
                book.setPublisher(publisher);
                valid = true;
            } catch (IllegalArgumentException e) {
                view.showError(e.getMessage());
            }
        } while (!valid);
    }

    /**
     * Prompts until a valid stock value is entered and sets it on the book.
     *
     * @author Samuel
     */
    private void registerStock(Book book){
        boolean valid = false;
        do{
            String stock = view.askStock();
            checkCancel(stock);
            try {
                book.setStock(Integer.parseInt(stock));
                valid = true;
            } catch (NumberFormatException e){
                view.showError("Ingrese un numero valido");
            } catch (IllegalArgumentException e) {
                view.showError(e.getMessage());
            }
        } while (!valid);
    }


    private IBookView view = null;
    private BookData data = null;

}
