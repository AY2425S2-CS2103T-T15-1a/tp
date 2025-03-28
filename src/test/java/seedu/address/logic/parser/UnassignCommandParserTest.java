package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DUTY_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DUTY_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DUTY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DUTY_AMY_STRING;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.UnassignCommand;
import seedu.address.model.person.Duty;

public class UnassignCommandParserTest {
    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnassignCommand.MESSAGE_USAGE);
    private final UnassignCommandParser parser = new UnassignCommandParser();

    @Test
    public void parse_null_failure() {
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, DUTY_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + DUTY_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + DUTY_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        //invalid duty date format
        assertParseFailure(parser, "1" + INVALID_DUTY_DESC, Duty.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + DUTY_DESC_AMY;
        UnassignCommand expectedCommand = new UnassignCommand(targetIndex, VALID_DUTY_AMY_STRING);

        assertParseSuccess(parser, userInput, expectedCommand);

        Index secondTargetIndex = INDEX_SECOND_PERSON;
        String secondUserInput = secondTargetIndex.getOneBased() + DUTY_DESC_AMY;
        UnassignCommand secondExpectedCommand = new UnassignCommand(secondTargetIndex, VALID_DUTY_AMY_STRING);
        assertParseSuccess(parser, secondUserInput, secondExpectedCommand);
    }

    @Test
    public void parse_multiple_duty() {
        String userInput = INDEX_FIRST_PERSON.getOneBased() + DUTY_DESC_AMY + DUTY_DESC_BOB;

        //only record the last appearance of duty date
        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_DUTY));

        String secondUserInput = INDEX_SECOND_PERSON.getOneBased() + DUTY_DESC_BOB + DUTY_DESC_AMY;

        //only record the last appearance of duty date
        assertParseFailure(parser, secondUserInput,
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_DUTY));
    }
}
