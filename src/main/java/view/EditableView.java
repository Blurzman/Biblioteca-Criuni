package View;

/**
 * Interface for views that support edit operations.
 * Implemented by views whose corresponding entity can be edited.
 *
 * @author Samuel
 */
public interface EditableView {
    /**
     * Displays the list of editable fields and prompts for a selection.
     *
     * @return the selected option number as a string.
     * @author Samuel
     */
    String askEditOption();
}
