package controller;

import data.StudentData;
import model.Faculty;
import model.Student;
import util.CancelException;
import util.Cancellable;
import view.IStudentView;
import view.StudentViewCLI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * controller for student operations.
 * Handles registration and editing of students,
 * coordinating between {@link StudentViewCLI} and {@link data.StudentData}.
 *
 * @author Samuel
 */
public class StudentController extends Cancellable implements BaseController, EditableController{
    public StudentController(IStudentView view, StudentData data) {
        this.view = view;
        this.data = data;
    }
    /**
     * Collects all student fields from the user and saves the student.
     * Cancellable via {@code "salir"}.
     *
     * @author Samuel
     */
    @Override
    public void register() {
        Student student = new Student();

        try {
            registerName(student);
            registerDocument(student);
            registerEmail(student);
            registerPhoneNumber(student);
            registerBirthDate(student);
            registerFaculty(student);
            data.add(student);
        } catch (IllegalArgumentException | CancelException e) {
            view.showError(e.getMessage());
        }
    }

    /**
     * Looks up a student by {@code document} and lets the user edit one field.
     * Cancellable via {@code "salir"}.
     *
     * @author Samuel
     */
    @Override
    public void edit() {
        try {
            String document = view.askDocument();
            checkCancel(document);
            Student student = data.get(document);
            String option = view.askEditOption();
            checkCancel(option);
            switch (Integer.parseInt(option)) {
                case (1):
                    registerName(student);
                    break;
                case (2):
                    registerDocument(student);
                    break;
                case (3):
                    registerEmail(student);
                    break;
                case (4):
                    registerPhoneNumber(student);
                    break;
                case (5):
                    registerBirthDate(student);
                    break;
                case (6):
                    registerFaculty(student);
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
     * Looks up a student from the storage and removes it.
     * Cancellable via {@code "salir"}.
     *
     * @author Samuel
     */
    @Override
    public void remove() {
        boolean valid = false;
        do{
            try {
                String document = view.askDocument();
                checkCancel(document);
                Student student = data.get(document);
                data.remove(student);
                valid = true;
            } catch (IllegalArgumentException | CancelException e){
                view.showError(e.getMessage());
            }
        } while (!valid);
    }

    /**
     * Shows all the students in the storage
     *
     * @author Samuel
     */
    @Override
    public void showAll(){
        view.showAll(data.getAll());
    }

    /**
     * Prompts until a valid name is entered and sets it on the student.
     *
     * @author Samuel
     */
    private void registerName(Student student){
        boolean valid = false;
        do{
            String name = view.askName();
            checkCancel(name);
            try {
                student.setName(name);
                valid = true;
            } catch (IllegalArgumentException e){
                view.showError(e.getMessage());
            }
        } while (!valid);
    }

    /**
     * Prompts until a valid document number is entered and sets it on the student.
     *
     * @author Samuel
     */
    private void registerDocument(Student student){
        boolean valid = false;
        do{
            String document = view.askDocument();
            checkCancel(document);
            try {
                student.setDocument(document);
                valid = true;
            } catch (IllegalArgumentException e){
                view.showError(e.getMessage());
            }
        } while (!valid);
    }

    /**
     * Prompts until a valid email is entered and sets it on the student.
     *
     * @author Samuel
     */
    private void registerEmail(Student student){
        boolean valid = false;
        do{
            String email = view.askEmail();
            checkCancel(email);
            try {
                student.setEmail(email);
                valid = true;
            } catch (IllegalArgumentException e){
                view.showError(e.getMessage());
            }
        } while (!valid);
    }

    /**
     * Prompts until a valid phone number is entered and sets it on the student.
     *
     * @author Samuel
     */
    private void registerPhoneNumber(Student student){
        boolean valid = false;
        do{
            String number = view.askNumber();
            checkCancel(number);
            try {
                student.setPhoneNumber(number);
                valid = true;
            } catch (IllegalArgumentException e){
                view.showError(e.getMessage());
            }
        } while (!valid);
    }

    /**
     * Prompts until a valid birth date ({@code dd/MM/yyyy}) is entered and sets it on the student.
     *
     * @author Samuel
     */
    private void registerBirthDate(Student student){
        boolean valid = false;
        do{
            try {
                String date = view.askBirthDate();
                checkCancel(date);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                student.setBirthDate(LocalDate.parse(date, formatter));
                valid = true;
            } catch (IllegalArgumentException e){
                view.showError(e.getMessage());
            } catch (DateTimeParseException e) {
                view.showError("Formato invalido utilice dd/MM/yyyy");
            }
        } while (!valid);
    }

    /**
     * Prompts until a valid faculty number is entered and sets it on the student.
     *
     * @author Samuel
     */
    private void registerFaculty(Student student){
        boolean valid = false;
        do{
            try {
                String faculty = view.askFaculty();
                checkCancel(faculty);
                int option = Integer.parseInt(faculty);
                student.setFaculty(Faculty.values()[option - 1]);
                valid = true;
            } catch (IndexOutOfBoundsException e){
                view.showError("Ingrese una de las opciones por favor");
            } catch (NumberFormatException e) {
                view.showError("Ingrese un numero valido");
            }
        } while (!valid);
    }

    private IStudentView view = null;
    private StudentData data = null;



}
