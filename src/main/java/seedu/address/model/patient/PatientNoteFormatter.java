package seedu.address.model.patient;

import java.util.TreeSet;

import seedu.address.model.note.Note;

/**
 * Utility class for formatting patient notes.
 */
public final class PatientNoteFormatter {

    /**
     * Private constructor to prevent instantiation.
     */
    private PatientNoteFormatter() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * Formats notes dynamically with numbering and proper wrapping.
     *
     * @param notesSet A TreeSet of notes to be formatted. The TreeSet ensures that the notes are sorted by
     *                 their natural ordering (which is expected to be by creation date).
     * @return A formatted string representing the notes with numbering, content, and timestamps.
     */
    public static String formatNotes(TreeSet<Note> notesSet) {
        StringBuilder sb = new StringBuilder();
        int noteNumber = 1;

        for (Note note : notesSet) {
            sb.append(noteNumber).append(". ").append(note.getTitle()).append("\n")
                    .append("   ").append(note.getContent()).append("\n") // The UI will handle wrapping
                    .append("   ").append(note.getDateTimeCreated().toString()).append("\n\n");
            noteNumber++;
        }

        return sb.toString().trim();
    }
}
