package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.patient.Patient;

/**
 * Unmodifiable view of an app
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the patients list.
     * This list will not contain any duplicate patients.
     */
    ObservableList<Patient> getPatientList();

}
