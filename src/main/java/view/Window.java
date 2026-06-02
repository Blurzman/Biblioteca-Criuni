package view;

import controller.BookGuiController;
import controller.LoanGuiController;
import controller.StudentGuiController;
import model.Book;
import model.Faculty;
import model.Loan;
import model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Swing GUI for the library. Uses the push-model GUI controllers
 * ({@link StudentGuiController}, {@link BookGuiController}, {@link LoanGuiController})
 * so validation lives in the model and the event dispatch thread never blocks.
 * Layout managers (req 13): {@link BorderLayout}, {@link BoxLayout}, {@link GridBagLayout}, {@link FlowLayout}.
 *
 * @author Samuel
 */
public class Window extends JFrame {

    public Window() {}

    /**
     * Wires the controllers and the persistence callback, builds the UI, and shows the window.
     *
     * @param onChange invoked after every operation that modifies data (persists to disk).
     */
    public void init(BookGuiController book, StudentGuiController student, LoanGuiController loan, Runnable onChange) {
        this.bookController = book;
        this.studentController = student;
        this.loanController = loan;
        this.onChange = onChange;

        buildUI();

        refreshStudents(studentController.list());
        refreshBooks(bookController.list());
        refreshLoans(loanController.list());

        setVisible(true);
    }

    // ---------------------------------------------------------------- UI layout

    private void buildUI() {
        setTitle("BIBLIOTECA CRIUNI");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("BIBLIOTECA CRIUNI", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Alumnos", createStudentTab());
        tabs.add("Libros", createBookTab());
        tabs.add("Prestamos", createLoanTab());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel createStudentTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        studentTable = buildTable(new String[]{"Cedula", "Alumno", "Correo", "Fecha Nac.", "Facultad"});
        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        JButton create = createButton("CREAR");
        JButton edit = createButton("EDITAR");
        JButton delete = createButton("BORRAR");
        JButton byName = createButton("ORDEN: NOMBRE");
        JButton byAge = createButton("ORDEN: EDAD");

        create.addActionListener(e -> openStudentForm(null));
        edit.addActionListener(e -> editStudent());
        delete.addActionListener(e -> deleteStudent());
        byName.addActionListener(e -> refreshStudents(studentController.listByName()));
        byAge.addActionListener(e -> refreshStudents(studentController.listByAge()));

        panel.add(buttonColumn(create, edit, delete, byName, byAge), BorderLayout.EAST);
        return panel;
    }

    private JPanel createBookTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        bookTable = buildTable(new String[]{"ISBN", "Titulo", "Autor", "Año", "Editorial", "Stock"});
        panel.add(new JScrollPane(bookTable), BorderLayout.CENTER);

        JButton create = createButton("CREAR");
        JButton edit = createButton("EDITAR");
        JButton delete = createButton("BORRAR");

        create.addActionListener(e -> openBookForm(null));
        edit.addActionListener(e -> editBook());
        delete.addActionListener(e -> deleteBook());

        panel.add(buttonColumn(create, edit, delete), BorderLayout.EAST);
        return panel;
    }

    private JPanel createLoanTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        loanTable = buildTable(new String[]{"ID", "Alumno", "Libros", "Fecha", "Vence", "Devolucion", "Estado", "Multa"});
        panel.add(new JScrollPane(loanTable), BorderLayout.CENTER);

        JButton create = createButton("CREAR");
        JButton give = createButton("DEVOLVER");
        JButton delete = createButton("BORRAR");
        JButton overdue = createButton("VENCIDOS");
        JButton returned = createButton("DEVUELTOS");
        JButton byUser = createButton("POR USUARIO");
        JButton byDate = createButton("POR FECHA");
        JButton all = createButton("TODOS");

        create.addActionListener(e -> openLoanForm());
        give.addActionListener(e -> returnLoan());
        delete.addActionListener(e -> deleteLoan());
        overdue.addActionListener(e -> refreshLoans(loanController.overdue()));
        returned.addActionListener(e -> refreshLoans(loanController.returned()));
        byUser.addActionListener(e -> filterLoansByUser());
        byDate.addActionListener(e -> filterLoansByDate());
        all.addActionListener(e -> refreshLoans(loanController.list()));

        panel.add(buttonColumn(create, give, delete, overdue, returned, byUser, byDate, all), BorderLayout.EAST);
        return panel;
    }

    // ---------------------------------------------------------------- Student actions

    private void editStudent() {
        String document = selectedValue(studentTable, 0);
        if (document == null) {
            showError("Seleccione un alumno de la tabla");
            return;
        }
        Student student = findStudent(document);
        if (student != null) openStudentForm(student);
    }

    private void deleteStudent() {
        String document = selectedValue(studentTable, 0);
        if (document == null) {
            showError("Seleccione un alumno de la tabla");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar al alumno " + document + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            studentController.delete(document);
            onChange.run();
            refreshStudents(studentController.list());
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void openStudentForm(Student existing) {
        JDialog dialog = new JDialog(this, existing == null ? "Registrar Alumno" : "Editar Alumno", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField txtName = new JTextField(18);
        JTextField txtDocument = new JTextField(18);
        JTextField txtEmail = new JTextField(18);
        JTextField txtNumber = new JTextField(18);
        JTextField txtBirthDate = new JTextField(18);
        JComboBox<Faculty> cmbFaculty = new JComboBox<>(Faculty.values());
        cmbFaculty.setRenderer(facultyRenderer());

        if (existing != null) {
            txtName.setText(existing.getName());
            txtDocument.setText(existing.getDocument());
            txtDocument.setEnabled(false);
            txtEmail.setText(existing.getEmail());
            txtNumber.setText(existing.getPhoneNumber());
            txtBirthDate.setText(existing.getBirthDate().format(DATE_FMT));
            cmbFaculty.setSelectedItem(existing.getFaculty());
        }

        int row = 0;
        addRow(dialog, gbc, "Nombre:", txtName, row++);
        addRow(dialog, gbc, "Cedula:", txtDocument, row++);
        addRow(dialog, gbc, "Correo:", txtEmail, row++);
        addRow(dialog, gbc, "Telefono:", txtNumber, row++);
        addRow(dialog, gbc, "Fecha Nac. (dd/MM/yyyy):", txtBirthDate, row++);
        addRow(dialog, gbc, "Facultad:", cmbFaculty, row++);

        JButton save = createButton("GUARDAR");
        save.addActionListener(e -> {
            try {
                Faculty faculty = (Faculty) cmbFaculty.getSelectedItem();
                if (existing == null) {
                    studentController.create(txtName.getText(), txtDocument.getText(), txtEmail.getText(),
                            txtNumber.getText(), txtBirthDate.getText(), faculty);
                } else {
                    studentController.edit(existing.getDocument(), txtName.getText(), txtEmail.getText(),
                            txtNumber.getText(), txtBirthDate.getText(), faculty);
                }
                onChange.run();
                refreshStudents(studentController.list());
                dialog.dispose();
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            }
        });

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(save, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // ---------------------------------------------------------------- Book actions

    private void editBook() {
        String isbn = selectedValue(bookTable, 0);
        if (isbn == null) {
            showError("Seleccione un libro de la tabla");
            return;
        }
        Book book = findBook(isbn);
        if (book != null) openBookForm(book);
    }

    private void deleteBook() {
        String isbn = selectedValue(bookTable, 0);
        if (isbn == null) {
            showError("Seleccione un libro de la tabla");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar el libro " + isbn + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            bookController.delete(isbn);
            onChange.run();
            refreshBooks(bookController.list());
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void openBookForm(Book existing) {
        JDialog dialog = new JDialog(this, existing == null ? "Registrar Libro" : "Editar Libro", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField txtIsbn = new JTextField(18);
        JTextField txtTitle = new JTextField(18);
        JTextField txtAuthor = new JTextField(18);
        JTextField txtYear = new JTextField(18);
        JTextField txtPublisher = new JTextField(18);
        JTextField txtStock = new JTextField(18);

        if (existing != null) {
            txtIsbn.setText(existing.getIsbn());
            txtIsbn.setEnabled(false);
            txtTitle.setText(existing.getTitle());
            txtAuthor.setText(existing.getAuthor());
            txtYear.setText(existing.getYearOfPublishing().toString());
            txtPublisher.setText(existing.getPublisher());
            txtStock.setText(String.valueOf(existing.getStock()));
        }

        int row = 0;
        addRow(dialog, gbc, "ISBN (13 digitos):", txtIsbn, row++);
        addRow(dialog, gbc, "Titulo:", txtTitle, row++);
        addRow(dialog, gbc, "Autor:", txtAuthor, row++);
        addRow(dialog, gbc, "Año:", txtYear, row++);
        addRow(dialog, gbc, "Editorial:", txtPublisher, row++);
        addRow(dialog, gbc, "Stock:", txtStock, row++);

        JButton save = createButton("GUARDAR");
        save.addActionListener(e -> {
            try {
                if (existing == null) {
                    bookController.create(txtIsbn.getText(), txtTitle.getText(), txtAuthor.getText(),
                            txtYear.getText(), txtPublisher.getText(), txtStock.getText());
                } else {
                    bookController.edit(existing.getIsbn(), txtTitle.getText(), txtAuthor.getText(),
                            txtYear.getText(), txtPublisher.getText(), txtStock.getText());
                }
                onChange.run();
                refreshBooks(bookController.list());
                dialog.dispose();
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            }
        });

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(save, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // ---------------------------------------------------------------- Loan actions

    private void openLoanForm() {
        List<Student> students = studentController.list().stream().collect(Collectors.toList());
        if (students.isEmpty()) {
            showError("No hay alumnos registrados");
            return;
        }
        List<Book> availableBooks = bookController.list().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
        if (availableBooks.isEmpty()) {
            showError("No hay libros disponibles");
            return;
        }

        JDialog dialog = new JDialog(this, "Registrar Prestamo", true);
        dialog.setLayout(new BorderLayout(10, 10));

        JComboBox<Student> cmbStudent = new JComboBox<>(students.toArray(new Student[0]));
        cmbStudent.setRenderer(studentRenderer());

        DefaultListModel<Book> listModel = new DefaultListModel<>();
        availableBooks.forEach(listModel::addElement);
        JList<Book> bookList = new JList<>(listModel);
        bookList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        bookList.setCellRenderer(bookRenderer());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Alumno:"));
        top.add(cmbStudent);
        dialog.add(top, BorderLayout.NORTH);
        dialog.add(new JScrollPane(bookList), BorderLayout.CENTER);

        JButton create = createButton("CREAR");
        create.addActionListener(e -> {
            try {
                Student student = (Student) cmbStudent.getSelectedItem();
                List<Book> selected = bookList.getSelectedValuesList();
                if (selected.isEmpty()) {
                    throw new IllegalArgumentException("Seleccione al menos un libro");
                }
                List<String> isbns = selected.stream().map(Book::getIsbn).collect(Collectors.toList());
                loanController.create(student.getDocument(), isbns);
                onChange.run();
                refreshLoans(loanController.list());
                refreshBooks(bookController.list());
                dialog.dispose();
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(new JLabel("Ctrl/Shift para elegir varios libros distintos"));
        bottom.add(create);
        dialog.add(bottom, BorderLayout.SOUTH);

        dialog.setSize(480, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void returnLoan() {
        String id = selectedValue(loanTable, 0);
        if (id == null) {
            showError("Seleccione un prestamo de la tabla");
            return;
        }
        try {
            long fine = loanController.returnLoan(id);
            onChange.run();
            refreshLoans(loanController.list());
            refreshBooks(bookController.list());
            JOptionPane.showMessageDialog(this,
                    fine > 0 ? "Multa a pagar: " + fine + " Gs." : "Devuelto sin multa.",
                    "Devolucion", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void deleteLoan() {
        String id = selectedValue(loanTable, 0);
        if (id == null) {
            showError("Seleccione un prestamo de la tabla");
            return;
        }
        try {
            loanController.delete(id);
            onChange.run();
            refreshLoans(loanController.list());
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void filterLoansByUser() {
        String document = JOptionPane.showInputDialog(this, "Cedula del alumno:");
        if (document == null) return;
        refreshLoans(loanController.byUser(document.trim()));
    }

    private void filterLoansByDate() {
        String fromText = JOptionPane.showInputDialog(this, "Desde (dd/MM/yyyy):");
        if (fromText == null) return;
        String toText = JOptionPane.showInputDialog(this, "Hasta (dd/MM/yyyy):");
        if (toText == null) return;
        try {
            LocalDate from = LocalDate.parse(fromText.trim(), DATE_FMT);
            LocalDate to = LocalDate.parse(toText.trim(), DATE_FMT);
            refreshLoans(loanController.byDate(from, to));
        } catch (DateTimeParseException e) {
            showError("Formato de fecha invalido, utilice dd/MM/yyyy");
        }
    }

    // ---------------------------------------------------------------- Table population

    private void refreshStudents(Collection<Student> students) {
        DefaultTableModel model = (DefaultTableModel) studentTable.getModel();
        model.setRowCount(0);
        for (Student s : students) {
            model.addRow(new Object[]{
                    s.getDocument(), s.getName(), s.getEmail(),
                    s.getBirthDate().format(DATE_FMT), s.getFaculty().getDisplayName()
            });
        }
    }

    private void refreshBooks(Collection<Book> books) {
        DefaultTableModel model = (DefaultTableModel) bookTable.getModel();
        model.setRowCount(0);
        for (Book b : books) {
            model.addRow(new Object[]{
                    b.getIsbn(), b.getTitle(), b.getAuthor(),
                    b.getYearOfPublishing().toString(), b.getPublisher(), b.getStock()
            });
        }
    }

    private void refreshLoans(Collection<Loan> loans) {
        DefaultTableModel model = (DefaultTableModel) loanTable.getModel();
        model.setRowCount(0);
        for (Loan l : loans) {
            String books = l.getBooks().stream().map(Book::getTitle).collect(Collectors.joining(", "));
            String estado = l.isReturned() ? "Devuelto" : (l.isOverdue() ? "Vencido" : "Pendiente");
            String returnDate = l.getReturnDate() == null ? "-" : l.getReturnDate().format(DATE_FMT);
            model.addRow(new Object[]{
                    l.getId(), l.getStudent().getDocument() + " - " + l.getStudent().getName(),
                    books, l.getLoanDate().format(DATE_FMT), l.getDueDate().format(DATE_FMT),
                    returnDate, estado, l.calculateFine()
            });
        }
    }

    // ---------------------------------------------------------------- Helpers

    private JTable buildTable(String[] columns) {
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        return table;
    }

    private JPanel buttonColumn(JButton... buttons) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        for (JButton button : buttons) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(button);
            panel.add(Box.createVerticalStrut(12));
        }
        return panel;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.RED);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 38));
        button.setMaximumSize(new Dimension(150, 38));
        return button;
    }

    private void addRow(JDialog dialog, GridBagConstraints gbc, String label, JComponent field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        dialog.add(field, gbc);
    }

    private String selectedValue(JTable table, int column) {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        Object value = table.getValueAt(row, column);
        return value == null ? null : value.toString();
    }

    private Student findStudent(String document) {
        return studentController.list().stream()
                .filter(s -> s.getDocument().equals(document))
                .findFirst().orElse(null);
    }

    private Book findBook(String isbn) {
        return bookController.list().stream()
                .filter(b -> b.getIsbn().equals(isbn))
                .findFirst().orElse(null);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private ListCellRenderer<Object> facultyRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean selected, boolean focused) {
                super.getListCellRendererComponent(list, value, index, selected, focused);
                if (value instanceof Faculty) setText(((Faculty) value).getDisplayName());
                return this;
            }
        };
    }

    private ListCellRenderer<Object> studentRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean selected, boolean focused) {
                super.getListCellRendererComponent(list, value, index, selected, focused);
                if (value instanceof Student) {
                    Student s = (Student) value;
                    setText(s.getName() + " (" + s.getDocument() + ")");
                }
                return this;
            }
        };
    }

    private ListCellRenderer<Object> bookRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean selected, boolean focused) {
                super.getListCellRendererComponent(list, value, index, selected, focused);
                if (value instanceof Book) {
                    Book b = (Book) value;
                    setText(b.getTitle() + "  [" + b.getIsbn() + "]  stock: " + b.getStock());
                }
                return this;
            }
        };
    }

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private BookGuiController bookController;
    private StudentGuiController studentController;
    private LoanGuiController loanController;
    private Runnable onChange;

    private JTable studentTable;
    private JTable bookTable;
    private JTable loanTable;
}
