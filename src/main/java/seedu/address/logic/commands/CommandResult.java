package seedu.address.logic.commands;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.note.Note;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {
    private final String feedbackToUser;
    private final boolean showHelp;
    private final boolean exit;
    private final boolean showNotes;
    private final String patientName;
    private final List<Note> notesList;

    // Regular constructor for normal commands
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false, false, null, null);
    }

    // Constructor for help and exit commands
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this(feedbackToUser, showHelp, exit, false, null, null);
    }

    /**
     * Constructs a {@code CommandResult} object with the given parameters.
     * This constructor is used for initializing the command result with feedback,
     * help status, exit status, notes display, patient information, and a list of notes.
     *
     * @param feedbackToUser The feedback message to be displayed to the user.
     * @param showHelp A boolean flag indicating whether the help screen should be shown.
     * @param exit A boolean flag indicating whether the application should exit.
     * @param showNotes A boolean flag indicating whether the notes should be displayed.
     * @param patientName The name of the patient related to the notes.
     * @param notesList A list of {@code Note} objects associated with the patient.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit,
                         boolean showNotes, String patientName, List<Note> notesList) {
        this.feedbackToUser = feedbackToUser;
        this.showHelp = showHelp;
        this.exit = exit;
        this.showNotes = showNotes;
        this.patientName = patientName;
        this.notesList = notesList;
    }


    // Add getters for all fields
    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    public boolean isShowNotes() {
        return showNotes;
    }

    public String getPatientName() {
        return patientName;
    }

    public List<Note> getNotesList() {
        return notesList;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("exit", exit)
                .toString();
    }

}
