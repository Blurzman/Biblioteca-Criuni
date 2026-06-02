package view;

import model.Book;

import java.util.Collection;

public interface IBookView extends EditableView {
    void showError(String message);
    void showAll(Collection<Book> books);
    String askTitle();
    String askAuthor();
    String askYearOfPublishing();
    String askPublisher();
    String askStock();
    String askDeltaStock();
    String askIsbn();
}
