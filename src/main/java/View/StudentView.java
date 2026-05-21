package View;

import model.Faculty;
import model.Student;

import java.util.Collection;
import java.util.Scanner;

/**
 *  CLI for student-related interaction.
 *  Handles all input and output for student operations.
 *  Type "salir" at any prompt to cancel the current operation
 * @author Samuel
 */
public class StudentView implements BaseView<Student>, EditableView {

    public StudentView(){
        this.sc = new Scanner(System.in);
    }

    /**
     *
     * @return The student's full name as entered.
     * @author Samuel
     */
    public String askName(){
        System.out.println("Ingrese su nombre completo por favor:");
        return sc.nextLine();
    }

    /**
     *
     * @return the student's document as entered.
     * @author Samuel
     */
    public String askDocument(){
        System.out.println("Ingrese su numero de documento por favor:");
        return sc.nextLine();
    }

    /**
     *
     * @return the student's email as entered.
     * @author Samuel
     */
    public String askEmail(){
        System.out.println("Ingrese su correo institucional por favor:");
        return sc.nextLine();
    }

    /**
     *
     * @return the student's phone number as entered.
     * @author Samuel
     */
    public String askNumber(){
        System.out.println("Ingrese su numero de telefono sin el cero por favor:");
        return sc.nextLine();
    }

    /**
     *
     * @return the student's birthdate in dd/MM/yyyy format as entered.
     * @author Samuel
     */
    public String askBirthDate(){
        System.out.println("Ingrese su fecha de nacimiento dd/MM/yyyy por favor:");
        return sc.nextLine();
    }

    /**
     * Displays a list of available faculties.
     * @return the selected option number as a string.
     */
    public String askFaculty() {
        System.out.println("Ingrese su facultad:");
        for (Faculty f : Faculty.values()){
            System.out.printf("%d. %s%n", f.ordinal() + 1, f.getDisplayName());
        }
        System.out.println("Ingresar opcion: ");
        return sc.nextLine();
    }

    /**
     * Displays the list of editable fields.
     * @return the selected option number as a string.
     * @author Samuel
     */
    @Override
    public String askEditOption(){
        System.out.println("Que quiere editar?");
        System.out.println("1. Nombre");
        System.out.println("2. Documento");
        System.out.println("3. Email");
        System.out.println("4. Numero de telefono");
        System.out.println("5. Fecha de nacimiento");
        System.out.println("6. Facultad");
        System.out.println("Ingresar opcion: ");
        return sc.nextLine();
    }

    /**
     *
     * @param message Error message to print.
     * @author Samuel
     */
    @Override
    public void showError(String message) {
        System.err.println(message);
    }

    /**
     * Prints all students on the console.
     * @param students the collection of students to display.
     * @author Samuel
     */
    @Override
    public void showAll(Collection<Student> students){
        students.forEach(System.out::println);

    }

    private Scanner sc = null;
}
