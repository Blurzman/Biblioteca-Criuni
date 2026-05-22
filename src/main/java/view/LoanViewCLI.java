package view;

import model.Loan;

import java.util.Collection;
import java.util.Scanner;

/**
 * Console view for loan-related interactions.
 * Handles all input prompts and output for loan operations.
 * Type "salir" at any prompt to cancel, "hecho" to finish adding books.
 *
 * @author Samuel
 */
public class LoanViewCLI implements ILoanView{
    public LoanViewCLI(){
        sc = new Scanner(System.in);
    }

    /**
     * @return The student's document number as a String.
     * @author Samuel
     */
    public String askStudentDocument(){
        System.out.println("Ingrese su numero de documento por favor: ");
        return sc.nextLine();
    }

    /**
     * @return A book UUID, or "hecho" to finish adding books, or "salir" to cancel.
     * @author Samuel
     */
    public String askBookUuid(){
        System.out.println("Ingrese el uuid del libro que desea (Escriba \"hecho\" para terminar): ");
        return sc.nextLine();
    }

    /**
     * @return The loan ID as a String.
     * @author Samuel
     */
    public String askLoanId(){
        System.out.println("Ingrese el id del prestamo");
        return sc.nextLine();
    }

    /**
     * Prints the fine amount, or "Sin multa." if there is none.
     * @param fine Fine in Guaraníes.
     * @author Samuel
     */
    public void showFine(long fine){
        if (fine > 0) {
            System.out.println("Multa a pagar: " + fine + " Gs.");
        } else {
            System.out.println("Sin multa.");
        }
    }

    /**
     * @param message Error message to print.
     * @author Samuel
     */
    @Override
    public void showError(String message){
        System.err.println(message);
    }

    /**
     * @param loans Collection of loans to print, one per line.
     * @author Samuel
     */
    @Override
    public void showAll(Collection<Loan> loans){
        loans.forEach(System.out::println);
    }

    private Scanner sc = null;
}
