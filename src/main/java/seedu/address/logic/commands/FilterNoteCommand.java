package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX;
import static seedu.address.logic.Messages.MESSAGE_NOTE_NOT_FOUND;
import static seedu.address.logic.Messages.MESSAGE_NO_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE_TITLE;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.note.Note;
import seedu.address.model.patient.Patient;

/**
 * Filter out to display the note whose title matches the keyword under the
 * specific patient with the index
 * Keyword matching is case insensitive.
 */
public class FilterNoteCommand extends Command {

    public static final String COMMAND_WORD = "filternote";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filter the note with the matching title "
            + "belonging to the patient identified by the\n"
            + "index number used in the displayed patient list. "
            + "The specified keywords are case-insensitive.\n"
            + "Parameters: INDEX (must be a positive integer) " + PREFIX_NOTE_TITLE + "TITLE\n"
            + "Example: " + COMMAND_WORD + " 1 nt/alex";

    public static final String MESSAGE_SUCCESS = "Displaying notes for %1$s";

    private final Index index;

    private final String title;

    /**
     * Creates a NoteCommand to add the specified {@code Note}
     *
     * @param index index of the patient in the filtered patient list
     * @param title title of the note
     */
    public FilterNoteCommand(Index index, String title) {
        this.index = index;
        this.title = title;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Patient> lastShownList = model.getFilteredPatientList();

        if (index.getZeroBased() < 0 || index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
        }

        Patient patientToFilter = lastShownList.get(index.getZeroBased());
        TreeSet<Note> allNotes = patientToFilter.getNotes();

        if (allNotes.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_NO_NOTES, index.getOneBased()));
        }

        List<Note> matchingNotes = allNotes.stream()
                .filter(note -> note.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());

        if (matchingNotes.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_NOTE_NOT_FOUND, title));
        }

        String message = String.format(MESSAGE_SUCCESS, patientToFilter.getName().fullName);
        return new CommandResult(message,
                false, false, true,
                patientToFilter.getName().fullName,
                matchingNotes,
                null);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterNoteCommand)) {
            return false;
        }

        FilterNoteCommand otherFilterNoteCommand = (FilterNoteCommand) other;
        return index.equals(otherFilterNoteCommand.index) && title.equals(otherFilterNoteCommand.title);
    }

    public Index getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }
}
