package seedu.address.model.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import seedu.address.model.note.Note;

/**
 * Tests for {@code PatientNoteFormatter}.
 */
public class PatientNoteFormatterTest {

    /**
     * Tests if {@code PatientNoteFormatter} correctly formats notes.
     */
    @Test
    public void formatNotes_validNotes_correctFormat() {
        TreeSet<Note> notes = new TreeSet<>();

        // Create notes with specific timestamps
        Note firstNote = new Note("Session 1", "Content", LocalDateTime.of(2024, 3, 10, 10, 0));
        Note secondNote = new Note("Session 2", "Progress update", LocalDateTime.of(2024, 3, 11, 10, 0));

        notes.add(secondNote);
        notes.add(firstNote); // This should be ordered first

        // Get the actual formatted output
        String actualOutput = PatientNoteFormatter.formatNotes(notes);

        // Since TreeSet orders by dateTimeCreated, Session 1 should come first
        String expectedOutput = "1. Session 1\n   Content\n   2024-03-10T10:00\n\n"
                + "2. Session 2\n   Progress update\n   2024-03-11T10:00";

        // Assert that the formatted output matches the expected order and format
        assertEquals(expectedOutput, actualOutput);
    }
}
