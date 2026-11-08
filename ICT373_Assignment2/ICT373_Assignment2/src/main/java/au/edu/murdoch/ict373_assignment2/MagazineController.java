
/*
 * Title: Magazine Service Application
 * Author: Nguyen Tuan Anh
 * Date: 04 November 2026
 * File Name: MagazineController.java
 *
 * Purpose:
 * This class manages the magazine creation interface.
 * It collects the magazine name and weekly main-part cost
 * before creating a Magazine object.
 *
 * Assumptions and Input Conditions:
 * - The magazine name cannot be empty.
 * - The weekly cost must be a valid non-negative number.
 * - Only one Magazine can be created in the application.
 *
 * Expected Output:
 * - Creates a Magazine object.
 * - Stores the magazine information in the application.
 * - Saves the newly created magazine.
 * - Displays validation and confirmation messages.
 */
package au.edu.murdoch.ict373_assignment2;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import question2_assignment1.Magazine;

/**
 * Controls the magazine creation form and validates the information entered by
 * the user.
 *
 * @author Nguyen Tuan Anh
 */
public class MagazineController {

    private final Assignment2 app;

    private final TextField nameField = new TextField();

    private final TextField costField = new TextField();

    /**
     * Initializes the magazine controller with the main application instance.
     *
     * @param app the main application instance
     */
    public MagazineController(Assignment2 app) {

        this.app = app;
    }

    /**
     * Constructs the magazine creation interface containing fields for the
     * magazine name and weekly main-part cost.
     *
     * @return the root component of the magazine creation interface
     */
    public Parent createView() {
        Label title = new Label("Create Magazine");

        nameField.setPromptText("Enter magazine name");
        costField.setPromptText("Enter weekly magazine cost");

        Button createButton = new Button("Create Magazine");
        Button backButton = new Button("Back to Create Mode");

        createButton.setOnAction(event -> handleCreateMagazine());
        backButton.setOnAction(event -> app.showCreateMode());

        VBox root = new VBox(
                12,
                title,
                new Label("Magazine name:"),
                nameField,
                new Label("Weekly main-part cost: "),
                costField,
                createButton,
                backButton
        );

        root.setPadding(new Insets(25));

        return root;
    }

    /**
     * Validates the magazine information, creates a Magazine object and saves
     * it through the main application.
     *
     * Prevents the creation of another magazine when one already exists.
     */
    private void handleCreateMagazine() {

        if (app.getMagazine() != null) {
            new Alert(Alert.AlertType.WARNING,
                    "The Magazine has already been created.").showAndWait();
            return;
        }
        String name = nameField.getText().trim();
        String input = costField.getText().trim();

        if (name.isEmpty()) {
            showError("Enter a magazine name: ");
            return;
        }

        if (input.isEmpty()) {
            showError("Enter the weekly magazine cost.");
            return;
        }

        double weeklyCost;

        try {
            weeklyCost = Double.parseDouble(costField.getText().trim());
        } catch (NumberFormatException e) {
            showError("Enter a valid numeric cost.");
            return;
        }

        if (Double.isNaN(weeklyCost) || Double.isInfinite(weeklyCost) || weeklyCost < 0) {
            showError("Weekly cost must be non-negative");
            return;
        }

        Magazine magazine = new Magazine(weeklyCost, new question2_assignment1.Supplement[0], name);

        app.setMagazine(magazine);
        app.saveMagazine();

        new Alert(Alert.AlertType.INFORMATION, "Magazine created successfully").showAndWait();
        System.out.println(
                "Magazine after creation: " + app.getMagazine()
        );

        app.showCreateMode();

    }

    /**
     * Displays an error alert to the user.
     *
     * @param message the error message to display
     */
    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }

}
