package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.commands.EditCommand.MESSAGE_DUPLICATE_PATIENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE_CONTENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE_TITLE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PATIENTS;
import static seedu.address.model.note.Note.isValidNote;

import java.util.List;
import java.util.TreeSet;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.note.Note;
import seedu.address.model.patient.Patient;

/**
 * Edit a specified note of a specified patient.
 */
public class EditNoteCommand extends Command {
    public static final String COMMAND_WORD = "editnote";
    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Edits the notes of the person identified "
        + "by the index number used in the last person listing. "
        + "Existing note will be overwritten by the input.\n"
        + "Parameters: USER_INDEX (must be a positive integer) "
        + PREFIX_NOTE_TITLE + "[NOTE TITLE] "
        + PREFIX_NOTE_CONTENT + "[NOTE CONTENT]\n"
        + "Example: " + COMMAND_WORD + " 1 " + PREFIX_NOTE_TITLE + "Patient has allergies! "
        + PREFIX_NOTE_CONTENT + "Allergies include:\n - Shellfish\n - Mushrooms";

    public static final String MESSAGE_NOT_IMPLEMENTED_YET =
        "editnote command not implemented yet";
    public static final String MESSAGE_ARGUMENTS = "UserIndex: %1$d, ListIndex: %1$d, Content: %3$s";
    public static final String MESSAGE_NOTE_NOT_FOUND = "Note Title cannot be found: %1$s";
    public static final String MESSAGE_NO_NOTES = "Patient %1$s has no notes";
    public static final String MESSAGE_BLANK_OR_EMPTY_NOTE = "Note Title and Contents cannot be empty or whitespaces";
    public static final String MESSAGE_EDIT_NOTE_SUCCESS = "Edited note of Person: %1$s";

    private final Index userIndex;
    private final String noteTitle;
    private final String content;

    /**
     * Creates an EditNoteCommand object
     * @param userIndex
     * @param noteTitle
     * @param content
     */
    public EditNoteCommand(Index userIndex, String noteTitle, String content) {
        requireAllNonNull(userIndex, noteTitle, content);

        this.userIndex = userIndex;
        this.noteTitle = noteTitle;
        this.content = content;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Patient> lastShownList = model.getFilteredPatientList();

        if (userIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
        }
        if (isValidNote(noteTitle, content) == false) {
            throw new CommandException(MESSAGE_BLANK_OR_EMPTY_NOTE + noteTitle + content);
        }

        Patient patientToEdit = lastShownList.get(userIndex.getZeroBased());
        // Copy existing notes, delete note, add note, send it back
        TreeSet<Note> updatedNotes = patientToEdit.getNotes();
        if (updatedNotes.isEmpty()) {
            throw new CommandException(MESSAGE_NO_NOTES);
        }

        Note deleteNote = null;
        for (Note i: updatedNotes) {
            if (i.getTitle().equalsIgnoreCase(noteTitle)) {
                deleteNote = i;
                break;
            }
        }
        if (deleteNote == null) {
            throw new CommandException(String.format(MESSAGE_NOTE_NOT_FOUND, noteTitle));
        }

        patientToEdit.getNotes().remove(deleteNote);
        Note editedNote = new Note(noteTitle, content);
        updatedNotes.add(editedNote);

        // Create updated patient
        Patient editedPatient = new Patient(
                patientToEdit.getName(),
                patientToEdit.getPhone(),
                patientToEdit.getEmail(),
                patientToEdit.getAddress(),
                patientToEdit.getTags(),
                updatedNotes);

        if (!patientToEdit.isSamePatient(editedPatient) && model.hasPatient(editedPatient)) {
            throw new CommandException(MESSAGE_DUPLICATE_PATIENT);
        }

        model.setPatient(patientToEdit, editedPatient);
        model.updateFilteredPatientList(PREDICATE_SHOW_ALL_PATIENTS);

        return new CommandResult(generateSuccessMessage(editedPatient));
    }

    /**
     * Generates a command execution success message when a note is edited.
     */
    private String generateSuccessMessage(Patient patientToEdit) {
        return String.format(MESSAGE_EDIT_NOTE_SUCCESS, Messages.format(patientToEdit));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditNoteCommand)) {
            return false;
        }

        EditNoteCommand e = (EditNoteCommand) other;
        return userIndex.equals(e.userIndex)
                && noteTitle.equals(e.noteTitle)
                && content.equals(e.content);
    }
}
