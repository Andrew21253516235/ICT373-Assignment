
/*
 * Title: Magazine Service Application
 * Author: Nguyen Tuan Anh
 * Date: 04 November 2026
 * File Name: SupplementController.java
 *
 * Purpose:
 * This class manages the supplement creation interface.
 * It collects supplement names and weekly costs and
 * registers valid supplements in the Magazine.
 *
 * Assumptions and Input Conditions:
 * - A Magazine must exist before adding supplements.
 * - Supplement names cannot be empty.
 * - Weekly costs must be valid non-negative numbers.
 *
 * Expected Output:
 * - Creates and registers supplement objects.
 * - Displays registered supplements in the interface.
 * - Saves updated magazine information.
 * - Displays validation and confirmation messages.
 */
package au.edu.murdoch.ict373_assignment2;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import question2_assignment1.Magazine;
import question2_assignment1.Supplement;

/**
 * Controls the supplement creation interface and manages supplement
 * registration within a Magazine.
 *
 * @author Nguyen Tuan Anh
 */
public class SupplementController {

    private final Assignment2 app;
    private final Magazine magazine;

    private final TextField nameField = new TextField();
    private final TextField costField = new TextField();

    private final ObservableList<String> supplementItems = FXCollections.observableArrayList();
    private final ListView<String> supplementList = new ListView<>(supplementItems);

    /**
     * Initializes the supplement controller with the application instance and
     * the magazine that will receive supplements.
     *
     * @param app the main application instance
     * @param magazine the magazine used to register supplements
     */
    public SupplementController(Assignment2 app, Magazine magazine) {

        this.app = app;
        this.magazine = magazine;
    }

    /**
     * Constructs the supplement creation interface.
     *
     * Provides input fields for the supplement name and weekly cost, together
     * with a list of registered supplements.
     *
     * @return the root component of the supplement interface
     */
    public Parent createView() {
        Label title = new Label("Add Supplements");

        nameField.setPromptText("Supplement name");
        costField.setPromptText("Weekly cost");

        Button addButton = new Button("Add supplement");
        Button backButton = new Button("Back to Create Mode");

        addButton.setOnAction(event -> handleAddSupplement());
        backButton.setOnAction(event -> app.showCreateMode());

        VBox root = new VBox(
                12,
                title,
                new Label("Name:"),
                nameField,
                new Label("Weekly cost:"),
                costField,
                addButton,
                new Label("Added supplements:"),
                supplementList,
                backButton
        );

        root.setPadding(new Insets(25));

        return root;
    }

    /**
     * Validates the supplement name and weekly cost.
     *
     * Creates a Supplement object, registers it in the Magazine and saves the
     * updated magazine information.
     *
     * Clears the input fields after successful registration.
     */
    private void handleAddSupplement() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            showError("Enter a supplement name.");
            return;
        }

        double weeklyCost;

        try {
            weeklyCost = Double.parseDouble(costField.getText().trim());
        } catch (NumberFormatException e) {
            showError("Enter a valid weekly cost.");
            return;
        }

        if (Double.isNaN(weeklyCost) || Double.isInfinite(weeklyCost) || weeklyCost < 0) {
            showError("Weekly cost must be non-negative");
            return;
        }

        Supplement supplement = new Supplement(name, weeklyCost);

        magazine.addSupplement(supplement);
        app.saveMagazine();

        supplementItems.add(name + " - $" + String.format("%.2f", weeklyCost));
        new Alert(Alert.AlertType.INFORMATION, "Supplement added successfully").showAndWait();

        nameField.clear();
        costField.clear();
        nameField.requestFocus();

    }

    /**
     * Displays an error alert to the user.
     *
     * @param message the error message to display
     */
    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }

    /**
     * Checks whether a Magazine exists before allowing the supplement
     * controller to be initialized.
     *
     * Displays a warning when no Magazine is available.
     *
     * @param app the main application instance
     * @param magazine the magazine to manage
     * @return a SupplementController if the magazine exists; otherwise null
     */
    public static SupplementController validateSupplement(
            Assignment2 app,
            Magazine magazine) {

        if (magazine == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setTitle("Magazine Required");
            alert.setHeaderText("Cannot Add Supplements");
            alert.setContentText(
                    "Please create the Magazine first."
            );

            alert.showAndWait();
            return null;
        }

        return new SupplementController(app, magazine);
    }

}
