package seedu.address.ui;

import java.util.List;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.FilterNoteCommand;
import seedu.address.logic.commands.ViewNotesCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.note.Note;
import seedu.address.model.patient.Patient;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private PatientListPanel patientListPanel;
    private NotesDisplayPanel notesDisplayPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;

    @FXML
    private VBox commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane patientListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane notesDisplayPanelPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private VBox commandBoxContainer;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        patientListPanel = new PatientListPanel(logic.getFilteredPatientList());
        patientListPanelPlaceholder.getChildren().add(patientListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        notesDisplayPanel = new NotesDisplayPanel();
        notesDisplayPanelPlaceholder.getChildren().add(notesDisplayPanel.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());

        // Ensure the command box can grow dynamically
        VBox.setVgrow(commandBoxPlaceholder, javafx.scene.layout.Priority.ALWAYS);
        VBox.setVgrow(commandBoxContainer, javafx.scene.layout.Priority.ALWAYS);
    }


    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    public PatientListPanel getPatientListPanel() {
        return patientListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());

            // Check if this is a ViewNotesCommand result
            if (commandText.trim().toLowerCase().startsWith(ViewNotesCommand.COMMAND_WORD)) {
                handleViewNotesCommand(commandResult, commandText);
            } else if (commandText.trim().toLowerCase().startsWith(FilterNoteCommand.COMMAND_WORD)) {
                handleFilterNoteCommand(commandResult, commandText);
            } else {
                // Reset notes panel for non-viewnotes commands
                notesDisplayPanel.reset();
                // Display regular feedback in the result display
                resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
            }

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("An error occurred while executing command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }

    /**
     * Handles the special case of a ViewNotesCommand by displaying the notes in the
     * dedicated NotesDisplayPanel.
     *
     * @param commandResult The result of the command execution
     * @param commandText The original command text
     */
    private void handleViewNotesCommand(CommandResult commandResult, String commandText) {
        try {
            String feedback = commandResult.getFeedbackToUser();

            // Display a simple confirmation message in the result display
            resultDisplay.setFeedbackToUser("Displaying notes. See notes panel below.");

            // Extract the index from the command
            String[] parts = commandText.trim().split("\\s+");
            if (parts.length < 2) {
                return; // Invalid command format, already handled elsewhere
            }

            int index = Integer.parseInt(parts[1]) - 1; // Convert to zero-based index
            if (index >= 0 && index < logic.getFilteredPatientList().size()) {
                Patient patient = logic.getFilteredPatientList().get(index);
                notesDisplayPanel.displayNotes(patient);
            }
        } catch (Exception e) {
            logger.warning("Error handling ViewNotesCommand: " + e.getMessage());
            // Fallback to showing the raw output in case of error
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
        }
    }

    /**
     * Handles the special case of a FilterNoteCommand by displaying the notes in the
     * dedicated NotesDisplayPanel.
     *
     * @param commandResult The result of the command execution
     * @param commandText The original command text
     */
    private void handleFilterNoteCommand(CommandResult commandResult, String commandText) {
        try {
            // Display a simple confirmation message in the result display
            resultDisplay.setFeedbackToUser("Displaying notes. See notes panel below.");

            // Extract the index from the command
            String[] parts = commandText.trim().split("\\s+");
            if (parts.length < 2) {
                return; // Invalid command format, already handled elsewhere
            }

            int index = Integer.parseInt(parts[1]) - 1; // Convert to zero-based index
            if (index >= 0 && index < logic.getFilteredPatientList().size()) {
                Patient patient = logic.getFilteredPatientList().get(index);
                List<Note> matchingNotes = commandResult.getNotesList();
                notesDisplayPanel.displayNotes(patient, matchingNotes);
            }
        } catch (Exception e) {
            logger.warning("Error handling FilterNoteCommand: " + e.getMessage());
            // Fallback to showing the raw output in case of error
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
        }
    }
}
