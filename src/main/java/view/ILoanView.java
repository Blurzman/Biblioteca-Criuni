package view;

import model.Loan;

import java.util.Collection;

public interface ILoanView {
    void showError(String message);
    void showAll(Collection<Loan> loans);
    String askStudentDocument();
    String askBookIsbn();
    String askLoanId();
    void showFine(long fine);
}
