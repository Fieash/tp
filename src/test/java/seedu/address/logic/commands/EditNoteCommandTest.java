package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NOTE_CONTENT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NOTE_CONTENT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NOTE_TITLE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NOTE_TITLE_BOB;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PATIENT;

import org.junit.jupiter.api.Test;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * EditNoteCommand.
 */
public class EditNoteCommandTest {
    /**
     * Test equals function
     */
    @Test
    public void equals() {
        final EditNoteCommand standardCommand =
            new EditNoteCommand(INDEX_FIRST_PATIENT, VALID_NOTE_TITLE_BOB, VALID_NOTE_CONTENT_BOB);

        // same values -> returns true
        EditNoteCommand commandWithSameValues =
            new EditNoteCommand(INDEX_FIRST_PATIENT, VALID_NOTE_TITLE_BOB, VALID_NOTE_CONTENT_BOB);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(
            new EditNoteCommand(INDEX_SECOND_PATIENT, VALID_NOTE_TITLE_BOB, VALID_NOTE_CONTENT_BOB)));

        // different note title and content -> returns false
        assertFalse(standardCommand.equals(
            new EditNoteCommand(INDEX_FIRST_PATIENT, VALID_NOTE_TITLE_AMY, VALID_NOTE_CONTENT_AMY)));
    }


}
