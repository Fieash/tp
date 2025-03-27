package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.note.Note;
import seedu.address.model.patient.Patient;

/**
 * Panel that displays notes for a selected patient.
 */
public class NotesDisplayPanel extends UiPart<Region> {

    private static final String FXML = "NotesDisplayPanel.fxml";

    @FXML
    private Label patientNameLabel;

    @FXML
    private VBox notesContainer;

    /**
     * Creates a {@code NotesDisplayPanel} with empty content.
     */
    public NotesDisplayPanel() {
        super(FXML);
        reset();
    }

    /**
     * Resets the panel to its default empty state.
     */
    public void reset() {
        patientNameLabel.setText("No patient selected");
        notesContainer.getChildren().clear();
    }

    /**
     * Updates the panel with notes from the specified patient.
     *
     * @param patient The patient whose notes should be displayed.
     */
    public void displayNotes(Patient patient) {
        requireNonNull(patient);
        patientNameLabel.setText("Notes for " + patient.getName().fullName);

        notesContainer.getChildren().clear();
        List<Note> notesList = patient.getNotes().stream().toList();

        if (notesList.isEmpty()) {
            Label noNotesLabel = new Label("This patient has no notes.");
            noNotesLabel.getStyleClass().add("viewnotes-content");
            notesContainer.getChildren().add(noNotesLabel);
            return;
        }

        int noteNumber = 1;
        for (Note note : notesList) {
            VBox noteBox = createNoteBox(note, noteNumber++);
            notesContainer.getChildren().add(noteBox);
        }
    }

    /**
     * Updates the panel with the specific filtered note from the specified patient.
     *
     * @param patient The patient whose notes should be displayed.
     * @param matchingNote the notes that match the keyword
     */
    public void displayNotes(Patient patient, List<Note> matchingNote) {
        requireNonNull(patient);
        patientNameLabel.setText("Notes for " + patient.getName().fullName);

        notesContainer.getChildren().clear();

        if (matchingNote.isEmpty()) {
            Label noNotesLabel = new Label("This patient has no notes.");
            noNotesLabel.getStyleClass().add("filternote-content");
            notesContainer.getChildren().add(noNotesLabel);
            return;
        }

        int noteNumber = 1;
        for (Note note : matchingNote) {
            VBox noteBox = createNoteBox(note, noteNumber++);
            notesContainer.getChildren().add(noteBox);
        }
    }
    /**
     * Creates a formatted box for displaying a single note.
     *
     * @param note The note to display.
     * @param noteNumber The sequence number of this note.
     * @return A VBox containing the formatted note.
     */
    private VBox createNoteBox(Note note, int noteNumber) {
        VBox noteBox = new VBox();
        noteBox.getStyleClass().add("viewnotes-entry");

        // Number and title
        Label titleLabel = new Label(noteNumber + ". " + note.getTitle());
        titleLabel.getStyleClass().add("viewnotes-title");
        titleLabel.setWrapText(true);

        // Content
        Label contentLabel = new Label(note.getContent());
        contentLabel.getStyleClass().add("viewnotes-content");
        contentLabel.setWrapText(true);

        // Date information
        Label dateLabel = new Label("Created: " + note.getDateTimeCreated().toString());
        dateLabel.getStyleClass().add("viewnotes-date");

        noteBox.getChildren().addAll(titleLabel, contentLabel, dateLabel);
        return noteBox;
    }
}
