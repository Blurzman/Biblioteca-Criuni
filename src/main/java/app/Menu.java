package app;

import controller.BookController;
import controller.LoanController;
import controller.StudentController;
import data.*;
import util.CancelException;
import util.Cancellable;
import java.io.*;

import java.util.Scanner;

/**
 * Entry point for the cli.
 * Type "salir" at any menu prompt to exit
 *
 * @author Samuel
 */
public class Menu extends Cancellable {
    private Menu(){}


    /**
     * Starts the mainMenu.
     *
     * @author Samuel
     */
    public static void start(){
            mainMenu();
    }


    /**
     * Displays the main menu and redirects to {@link #bookMenu()}, {@link #studentMenu()}, or {@link #loanMenu()}.
     * Cancellable via {@code "salir"}.
     *
     * @author Samuel
     */
    private static void mainMenu(){
        while (true) {
            try {
                System.out.println("Bienvenido a Biblioteca Alejandria");
                System.out.println("(1). Gestion de Libros");
                System.out.println("(2). Gestion de Alumnos");
                System.out.println("(3). Gestion de Prestamos");
                String option = askOption();
                checkCancel(option);
                switch (Integer.parseInt(option)) {
                    case (1):
                        bookMenu();
                        break;
                    case (2):
                        studentMenu();
                        break;
                    case (3):
                        loanMenu();
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e){
                System.err.println("Ingrese una opcion valida");
            } catch (CancelException e){
                System.exit(0);
            }
        }
    }


    /**
     * Displays the book management submenu.
     * Calls {@link #save()} after every modifying operation.
     * Cancellable via {@code "salir"}.
     *
     * @author Samuel.
     */
    private static void bookMenu(){
        while (true) {
            try {
                System.out.println("(1). Crear un libro");
                System.out.println("(2). Editar un libro");
                System.out.println("(3). Eliminar un libro");
                System.out.println("(4). Listar los libros");
                String option = askOption();
                checkCancel(option);
                switch (Integer.parseInt(option)) {
                    case (1):
                        BOOK_CONTROLLER.register();
                        save();
                        break;
                    case (2):
                        BOOK_CONTROLLER.edit();
                        save();
                        break;
                    case (3):
                        BOOK_CONTROLLER.remove();
                        save();
                        break;
                    case (4):
                        BOOK_CONTROLLER.showAll();
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e){
                System.err.println("Ingrese una opcion valida");
            } catch (CancelException e){
                break;
            }
        }
    }

    /**
     * Displays the student management submenu.
     * Calls {@link #save()} after every modifying operation.
     * Cancellable via {@code "salir"}.
     *
     * @author Samuel
     */
    private static void studentMenu(){
        while (true) {
            try {
                System.out.println("(1). Crear un alumno");
                System.out.println("(2). Editar un alumno");
                System.out.println("(3). Eliminar un alumno");
                System.out.println("(4). Listar los alumnos");
                String option = askOption();
                checkCancel(option);
                switch (Integer.parseInt(option)) {
                    case (1):
                        STUDENT_CONTROLLER.register();
                        save();
                        break;
                    case (2):
                        STUDENT_CONTROLLER.edit();
                        save();
                        break;
                    case (3):
                        STUDENT_CONTROLLER.remove();
                        save();
                        break;
                    case (4):
                        STUDENT_CONTROLLER.showAll();
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Ingrese una opcion valida");
            } catch (CancelException e){
                break;
            }
        }
    }

    /**
     * Displays the loan management submenu.
     * Calls {@link #save()} after every modifying operation.
     * Cancellable via {@code "salir"}.
     *
     * @author Samuel
     */
    private static void loanMenu(){
        while (true) {
            try {
                System.out.println("(1). Crear un prestamo");
                System.out.println("(2). Devolver un prestamo");
                System.out.println("(3). Eliminar un prestamo");
                System.out.println("(4). Eliminar todos los prestamos devueltos");
                System.out.println("(5). Listar los prestamos");
                String option = askOption();
                checkCancel(option);
                switch (Integer.parseInt(option)) {
                    case (1):
                        LOAN_CONTROLLER.register();
                        save();
                        break;
                    case (2):
                        LOAN_CONTROLLER.returnLoan();
                        save();
                        break;
                    case (3):
                        LOAN_CONTROLLER.remove();
                        save();
                        break;
                    case (4):
                        LOAN_CONTROLLER.removeReturned();
                        save();
                        break;
                    case (5):
                        LOAN_CONTROLLER.showAll();
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e){
                System.err.println("Ingrese una opcion valida");
            } catch (CancelException e){
                break;
            }
        }
    }


    /**
     * Prompts the user to enter a menu option.
     *
     * @return the entered option as a String.
     * @author Samuel
     */
    private static String askOption(){
        System.out.println("Ingrese \"salir\" para salir");
        System.out.println("Ingrese una opcion:");
        return SCANNER.nextLine();
    }

    /**
     * Persists all data stores to their respective files using {@link java.io.ObjectOutputStream}.
     * Called after every modifying operation. Falls back silently on {@link java.io.IOException}.
     *
     * @author Samuel
     */
    private static void save(){
        try {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(STUDENTS_FILE))) {
                out.writeObject(STUDENT_DATA);
            }
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(BOOKS_FILE))) {
                out.writeObject(BOOK_DATA);
            }
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(LOANS_FILE))) {
                out.writeObject(LOAN_DATA);
            }

            System.out.println("Datos guardados correctamente");
        } catch (IOException e){
            System.err.println("Error al guardar los datos" + e.getMessage());
        }
    }


    /**
     * Loads a serialized object from a file.
     * Returns {@code defaultValue} if the file does not exist or cannot be read.
     *
     * @param <T> the type of the object to load.
     * @param fileName path to the file.
     * @param defaultValue value returned if loading fails.
     * @return the deserialized object, or {@code defaultValue} on failure.
     * @author Samuel
     */
    @SuppressWarnings("unchecked")
    private static <T> T load(String fileName, T defaultValue){
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            return (T) in.readObject();
        } catch (IOException | ClassNotFoundException e){
            return defaultValue;
        }
    }





    private static final Scanner SCANNER = new Scanner(System.in);

    private static final String STUDENTS_FILE = "storage/students.dat";
    private static final String BOOKS_FILE = "storage/books.dat";
    private static final String LOANS_FILE = "storage/loans.dat";

    private static final StudentData STUDENT_DATA = load(STUDENTS_FILE, new StudentData());
    private static final BookData BOOK_DATA = load(BOOKS_FILE, new BookData());
    private static final LoanData LOAN_DATA = load(LOANS_FILE, new LoanData());

    private static final StudentController STUDENT_CONTROLLER = new StudentController(STUDENT_DATA);
    private static final BookController BOOK_CONTROLLER = new BookController(BOOK_DATA);
    private static final LoanController LOAN_CONTROLLER = new LoanController(STUDENT_DATA, BOOK_DATA, LOAN_DATA);
}
