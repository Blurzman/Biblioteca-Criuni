package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a book loan assigned to a student.
 * Tracks loan date, due date, return date, and calculates fines for late returns.
 * Fine is calculated as: BASE_FINE * FINE_RATE ^ daysLate (compound interest).
 *
 * @author Samuel
 */
public class Loan implements Serializable {
    public Loan(int id){
        this.id = Integer.toString(id);
    }

    public String getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    /**
     * @param student The student borrowing the books.
     * @author Samuel
     */
    public void setStudent(Student student) {
        this.student = student;
    }

    public List<Book> getBooks() {
        return books;
    }

    /**
     * @param books Non-empty list of books included in this loan.
     * @throws IllegalArgumentException if the list is empty.
     * @author Samuel
     */
    public void setBooks(List<Book> books) {
        if (!books.isEmpty()){
            this.books = books;
        } else {
            throw new IllegalArgumentException("Por favor elige algun libro");
        }
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    /**
     * Sets the loan date to today.
     * Must be called before {@link #setDueDate()}.
     *
     * @author Samuel
     */
    public void setLoanDate() {
        this.loanDate = LocalDate.now();
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    /** Sets the due date to {@code loanDate + LOAN_DAYS}.
     * Requires {@link #setLoanDate()} first.
     *
     * @author Samuel
     */
    public void setDueDate() {
        this.dueDate = loanDate.plusDays(LOAN_DAYS);
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    /**
     * Records today as the return date, marking the loan as returned.
     *
     * @author Samuel
     */
    public void setReturnDate() {
        this.returnDate = LocalDate.now();
    }

    public long getFine() {
        return fine;
    }

    /**
     * Calculates and stores the fine via {@link #calculateFine()}. Call after {@link #setReturnDate()}.
     *
     * @author Samuel
     */
    public void setFine() {
        this.fine = calculateFine();
    }

    /**
     * @return {@code true} if {@link #getReturnDate()} is not {@code null}.
     * @author Samuel
     */
    public boolean isReturned(){
        return returnDate != null;
    }

    /**
     * @return {@code true} if the return date (or today if {@link #isReturned()} is {@code false}) is after {@link #getDueDate()}.
     * @author Samuel
     */
    public boolean isOverdue(){
        LocalDate date;
        if (isReturned()){
            date = returnDate;
        } else {
            date = LocalDate.now();
        }
        return date.isAfter(dueDate);
    }

    /**
     * Computes the fine using compound interest: {@code BASE_FINE * FINE_RATE ^ daysLate}.
     * Uses {@link #getReturnDate()} if returned, otherwise today.
     * @return fine in Guaraníes, or {@code 0} if {@link #isOverdue()} is {@code false}.
     * @author Samuel
     */
    public long calculateFine(){
        if (isOverdue()) {
            LocalDate date;
            if (isReturned()){
                date = returnDate;
            } else {
                date = LocalDate.now();
            }
            long daysLate = ChronoUnit.DAYS.between(dueDate, date);
            return  (long) (BASE_FINE * Math.pow(FINE_RATE, daysLate));
        }
        return 0;
    }

    @Override
    public String toString(){
        String bookTitles = books.stream()
                .map(Book::getTitle)
                .collect(Collectors.joining(","));

        return String.format("[%s] | Alumno: %-8s | Libros: %-20s | Fecha de Prestamo: %s | Fecha de Devolucion: %s | Fue Retornada: %s | Paso el Tiempo: %s | Multa: %d",
                id, student.getDocument(), bookTitles, loanDate.toString(), dueDate.toString(), isReturned(), isOverdue(), calculateFine());
    }


    private String id = null;
    private Student student = null;
    private List<Book> books = null;
    private LocalDate loanDate = null;
    private LocalDate dueDate = null;
    private LocalDate returnDate = null;
    private long fine = 0;
    private static final int LOAN_DAYS = 7;
    private static final double FINE_RATE = 1.10;
    private static final double BASE_FINE = 20000;
}
