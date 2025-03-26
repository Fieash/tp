package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE_CONTENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE_TITLE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditNoteCommand;

public class EditNoteCommandParserTest {
    private final EditNoteCommandParser parser = new EditNoteCommandParser();
    private final String nonEmptyTitle = "Some title.";
    private final String nonEmptyContent = "Some content.";

    @Test
    public void parse_indexSpecified_success() {
        // have remark
        Index targetIndex = INDEX_FIRST_PATIENT;
        String userInput = targetIndex.getOneBased() + " "
            + PREFIX_NOTE_TITLE + nonEmptyTitle + " "
            + PREFIX_NOTE_CONTENT + nonEmptyContent;
        EditNoteCommand expectedCommand =
            new EditNoteCommand(INDEX_FIRST_PATIENT, nonEmptyTitle, nonEmptyContent);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditNoteCommand.MESSAGE_USAGE);
        Index targetIndex = INDEX_FIRST_PATIENT;

        // no parameters
        assertParseFailure(parser, EditNoteCommand.COMMAND_WORD, expectedMessage);

        // no index
        assertParseFailure(parser, EditNoteCommand.COMMAND_WORD + " " + PREFIX_NOTE_TITLE
            + nonEmptyTitle, expectedMessage);

        // no title
        assertParseFailure(parser, EditNoteCommand.COMMAND_WORD + " "
            + targetIndex.getOneBased()
            + PREFIX_NOTE_TITLE, expectedMessage);

        // no title
        assertParseFailure(parser, EditNoteCommand.COMMAND_WORD + " "
            + targetIndex.getOneBased()
            + PREFIX_NOTE_TITLE + nonEmptyTitle
            + PREFIX_NOTE_CONTENT + nonEmptyContent, expectedMessage);
    }
}
