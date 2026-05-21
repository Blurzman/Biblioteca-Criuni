package data;

import model.Student;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

/**
 *  Storage class for student.
 *  Uses the document as a key.
 *  @author Samuel
 */
public class StudentData implements BaseData<Student>, Serializable {
    public StudentData(){
        this.students = new HashMap<>();
    }

    /**
     * Adds a student to the storage.
     * @param student the student to add.
     * @throws IllegalArgumentException if the student's document already exists in the storage.
     * @author Samuel
     */
    @Override
    public void add(Student student){
        if (!students.containsKey(student.getDocument())){
            students.put(student.getDocument(),student);
        } else {
            throw new IllegalArgumentException("Estudiante con el documento " + student.getDocument() + " ya existe. Intente de nuevo.");
        }
    }

    /**
     * Removes a student from the storage.
     * @param student the student to remove.
     * @throws IllegalArgumentException if the student's document does not exist (should not happen).
     * @author Samuel
     */
    @Override
    public void remove(Student student){
        if (students.containsKey(student.getDocument())) {
            students.remove(student.getDocument());
        } else {
            throw new IllegalArgumentException("Ese alumno no existe");
        }
    }

    /**
     * Returns a student in the storage.
     * @param document the document of the student.
     * @return the student.
     * @throws IllegalArgumentException if the student's document does not exist.
     */
    @Override
    public Student get(String document){
        if (students.containsKey(document)){
            return students.get(document);
        } else {
            throw new IllegalArgumentException("Estudiante con el documento " + document + " no existe. Intente de nuevo.");
        }
    }

    /**
     *
     * @return the students in a collection.
     * @author Samuel
     */
    @Override
    public Collection<Student> getAll() {
        return students.values();
    }

    private HashMap<String, Student> students;

}





















