package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Student's model class.
 * Contains all of a student's data, including some validation rules.
 * @author Samuel
 */
public class Student implements Serializable {

    /**
     * Empty constructor, used in the class controller.
     * @author Samuel
     */
    public Student(){}

    public String getName() {
        return name;
    }

    /**
     *
     * @param name Full name of the student.
     * @throws IllegalArgumentException if it contains non-letter characters.
     * @author Samuel
     */
    public void setName(String name) {
        if (name.matches("^[a-zA-Z ]+$")) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Nombre invalido. Solo letras y espacios");
        }
    }

    public String getDocument() {
        return document;
    }

    /**
     *
     * @param document The document of the student. Must match {@code 1234567} (7 digits).
     * @throws IllegalArgumentException if it contains non-numeric characters or its length is not {@code 7}.
     * @author Samuel
     */
    public void setDocument(String document) {
        if (document.matches("^[0-9]{7}$")){
            this.document = document;
        } else{
            throw new IllegalArgumentException("Numero de cedula invalido. Debe tener formato 1234567");
        }
    }

    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email Institutional email of the student.
     * @throws IllegalArgumentException if it does not match {@code nombre@dominio.edu.py}.
     * @author Samuel
     */
    public void setEmail(String email) {
        if (email.matches("^[a-zA-Z.]+@[a-zA-Z]+\\.edu\\.py$")){
            this.email = email;
        } else {
            throw new IllegalArgumentException("Email invalido. Debe tener formato nombre@dominio.edu.py");
        }

    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     *
     * @param phoneNumber Paraguayan phone number. Must match {@code 972121212} (9 digits, no leading zero).
     * @throws IllegalArgumentException if it contains non-numeric characters or its length is not {@code 9}.
     * @author Samuel
     */
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber.matches("^[0-9]{9}$")){
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Numero de telefono invalido. Debe ser numero paraguayo sin el 0, 972121212");
        }
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     *
     * @param birthDate The birthdate of the student.
     * @throws IllegalArgumentException if {@code birthDate} is in the future.
     * @author Samuel
     */
    public void setBirthDate(LocalDate birthDate) {
        if(birthDate.isBefore(LocalDate.now())){
            this.birthDate = birthDate;
        } else {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser en el futuro");
        }
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    @Override
    public String toString(){
        return String.format("[%s] Nombre: %-30s| (%-30s) | Celular: %-10s | Nacimiento: %s | Facultad: %s",
                document, name, email, phoneNumber, birthDate.toString(), faculty.getDisplayName());
    }

    private String name = null;
    private String document = null;
    private String email = null;
    private String phoneNumber = null;
    private LocalDate birthDate = null;
    private Faculty faculty = null;

}
