package controller;

import data.BookData;
import data.LoanData;
import data.StudentData;
import model.Book;
import model.Loan;
import model.Student;
import util.CancelException;
import util.Cancellable;
import view.ILoanView;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * controller for loan operations.
 * Handles registering new loans, processing returns, and cleaning up returned loans.
 * Automatically adjusts book stock when loans are created or returned.
 *
 * @author Samuel
 */
public class LoanController extends Cancellable implements BaseController{
    public LoanController(StudentData studentData, BookData bookData, LoanData data, ILoanView view){
        this.studentData = studentData;
        this.bookData = bookData;
        this.data = data;
        this.view = view;
    }

    /**
     * Registers a new loan: collects student via {@link #registerStudent(Loan)},
     * books via {@link #registerBooks(Loan)}, sets dates, and saves.
     * Cancellable via {@code "salir"}.
     *
     * @author Samuel
     */
    @Override
    public void register(){
        Loan loan = new Loan(data.getNextId());
        try {
            registerStudent(loan);
            registerBooks(loan);
            loan.setLoanDate();
            loan.setDueDate();
            data.add(loan);
        } catch (IllegalArgumentException | CancelException e){
            view.showError(e.getMessage());
        }

    }

    /**
     * Removes a returned loan from storage. Only loans where {@link model.Loan#isReturned()} is {@code true} can be removed.
     * Cancellable via {@code "salir"}.
     *
     * @author Samuel
     */
    @Override
    public void remove() {
        boolean valid = false;
        do{
            try {
                String id = view.askLoanId();
                checkCancel(id);
                Loan loan = data.get(id);
                if (loan.isReturned()) {
                    data.remove(loan);
                    valid = true;
                } else {
                    view.showError("No se puede eliminar un prestamo pendiente");
                }
            } catch (IllegalArgumentException | CancelException e){
                view.showError(e.getMessage());
            }
        } while (!valid);
    }

    /**
     * Shows all the loans in the storage
     *
     * @author Samuel
     */
    @Override
    public void showAll(){
        view.showAll(data.getAll());
    }

    /**
     *
     * @return All overdue loans
     * @author Samuel
     */
    public Collection<Loan> getOverdue() {
        return data.getAll().stream()
                .filter(Loan::isOverdue)
                .collect(java.util.stream.Collectors.toList());
    }


    /**
     * Prompts until a valid student document is entered and sets the student on the loan.
     *
     * @param loan Loan being registered.
     * @author Samuel
     */
    public void registerStudent(Loan loan){
        boolean valid = false;
        do {
            String document = view.askStudentDocument();
            checkCancel(document);
            try {
                Student student = studentData.get(document);
                loan.setStudent(student);
                valid = true;
            } catch (IllegalArgumentException e) {
                view.showError(e.getMessage());
            }
        } while (!valid);
    }

    /**
     * Prompts for book UUIDs until {@code "hecho"} is entered.
     * Decrements stock for each added book via {@link model.Book#setStock(int)}.
     * Cancellable via {@code "salir"}.
     *
     * @param loan Loan being registered.
     * @author Samuel
     */
    public void registerBooks(Loan loan){
        List<Book> books = new ArrayList<>();
        while (true) {
            boolean valid = false;
            do {
                String isbn = view.askBookIsbn();
                checkCancel(isbn);
                try {
                    if (isbn.equalsIgnoreCase("hecho")){
                        loan.setBooks(books);
                        return;
                    }
                    Book book = bookData.get(isbn);
                    if (!book.isAvailable()){
                        view.showError("El libro no esta disponible");
                        continue;
                    }
                    books.add(book);
                    book.setStock(book.getStock() - 1);
                    valid = true;
                } catch (IllegalArgumentException e) {
                    view.showError(e.getMessage());
                }
            } while (!valid);
        }
    }

    /**
     * Marks a loan as returned via {@link model.Loan#setReturnDate()}, calculates the fine
     * via {@link model.Loan#setFine()}, displays it, and restores book stock.
     * Cancellable via {@code "salir"}.
     *
     * @author Samuel
     */
    public void returnLoan() {
        boolean valid = false;
        do {
            try {
                String id = view.askLoanId();
                checkCancel(id);
                Loan loan = data.get(id);
                if (loan.isReturned()){
                    view.showError("Este prestamo ya fue devuelto");
                    return;
                }
                loan.setReturnDate();
                loan.setFine();
                view.showFine(loan.getFine());

                for (Book book : loan.getBooks()) {
                    book.setStock(book.getStock() + 1);
                }

                valid = true;
            } catch (IllegalArgumentException e){
                view.showError(e.getMessage());
            }
        } while (!valid);
    }

    /**
     * Removes all returned loans from storage.
     *
     * @author Samuel
     */
    public void removeReturned(){
        data.removeReturned();
    }

    private StudentData studentData;
    private BookData bookData;
    private ILoanView view;
    private LoanData data;

}
