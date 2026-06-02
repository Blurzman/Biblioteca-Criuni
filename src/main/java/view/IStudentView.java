package view;

import model.Student;

import java.util.Collection;

public interface IStudentView extends EditableView {
    void showError(String message);
    void showAll(Collection<Student> students);
    String askName();
    String askDocument();
    String askEmail();
    String askNumber();
    String askBirthDate();
    String askFaculty();
}
