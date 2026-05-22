package view;

import model.Student;

public interface IStudentView extends BaseView<Student>, EditableView { String askName();
    String askDocument();
    String askEmail();
    String askNumber();
    String askBirthDate();
    String askFaculty();
}
