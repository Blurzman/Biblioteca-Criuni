package view;

import model.Loan;

public interface ILoanView extends BaseView<Loan> {
    String askStudentDocument();
    String askBookUuid();
    String askLoanId();
    void showFine(long fine);
}
