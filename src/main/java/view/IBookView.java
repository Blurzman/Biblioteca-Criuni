package view;

import model.Book;

public interface IBookView extends BaseView<Book>, EditableView{
    String askTitle();
    String askAuthor();
    String askYearOfPublishing();
    String askPublisher();
    String askStock();
    String askDeltaStock();
    String askUuid();
}
