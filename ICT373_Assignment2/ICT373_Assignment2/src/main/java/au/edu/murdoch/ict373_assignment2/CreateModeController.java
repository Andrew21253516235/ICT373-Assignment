
/*
 * Title: Magazine Service Application
 * Author: Nguyen Tuan Anh
 * Date: 04 November 2026
 * File Name: CreateModeController.java
 *
 * Purpose:
 * This class manages the Create Mode interface and provides
 * navigation to the magazine, supplement and customer
 * creation forms.
 *
 * Assumptions and Input Conditions:
 * - An Assignment2 application instance must be provided.
 * - The application manages navigation between creation forms.
 * - Magazine creation must occur before adding supplements
 *   or customers.
 *
 * Expected Output:
 * - Displays the Create Mode interface.
 * - Provides buttons for accessing the magazine, supplement
 *   and customer creation forms.
 * - Allows users to return to the main mode selection screen.
 */
package au.edu.murdoch.ict373_assignment2;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Controls the Create Mode interface and manages navigation to the magazine,
 * supplement and customer creation forms.
 *
 * @author Nguyen Tuan Anh
 */
public class CreateModeController {

    private final Assignment2 app;

    /**
     * Initializes the Create Mode controller with the main application
     * instance.
     *
     * @param app the main application instance used to manage application
     * navigation
     */
    public CreateModeController(Assignment2 app) {
        this.app = app;
    }

    /**
     * Constructs the Create Mode graphical interface.
     *
     * Creates buttons for magazine creation, supplement registration and
     * customer creation. Each button is connected to its corresponding
     * application method.
     *
     * @return the root component of the Create Mode interface
     */
    public Parent createView() {
        Label title = new Label("Create Mode");

        title.setStyle(
                "-fx-font-size: 22px; -fx-font-weight: bold;"
                + "-fx-cursor: hand"
        );

        Button magazineButton
                = new Button("1. Create Magazine");

        Button supplementButton
                = new Button("2. Add Supplements");

        Button customerButton
                = new Button("3. Create Customers");

        Button backButton
                = new Button("Back to Mode Selection");

        backButton.setMaxWidth(Double.MAX_VALUE);

        backButton.setOnAction(
                event -> app.showModeSelection()
        );

        magazineButton.setMaxWidth(Double.MAX_VALUE);
        supplementButton.setMaxWidth(Double.MAX_VALUE);
        customerButton.setMaxWidth(Double.MAX_VALUE);

        magazineButton.setOnAction(
                event -> app.showMagazineForm()
        );

        supplementButton.setOnAction(
                event -> app.showSupplementForm()
        );

        customerButton.setOnAction(
                event -> app.showCustomerForm()
        );

        VBox root = new VBox(
                15,
                title,
                magazineButton,
                supplementButton,
                customerButton,
                backButton
        );

        root.setPadding(new Insets(25));

        return root;
    }
}
