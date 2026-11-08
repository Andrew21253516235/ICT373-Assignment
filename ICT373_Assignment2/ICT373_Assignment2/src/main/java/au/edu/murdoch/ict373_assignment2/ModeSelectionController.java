
/*
 * Title: Magazine Service Application
 * Author: Nguyen Tuan Anh
 * Date: 04 November 2026
 * File Name: ModeSelectionController.java
 *
 * Purpose:
 * This class manages the main mode selection interface.
 * It allows users to access Create Mode, Edit Mode
 * and View Mode.
 *
 * Assumptions and Input Conditions:
 * - An Assignment2 application instance must be provided.
 * - An existing Magazine is required before accessing Edit Mode.
 *
 * Expected Output:
 * - Displays the mode selection interface.
 * - Navigates users to their selected application mode.
 * - Displays a warning if Edit Mode is selected without
 *   an existing Magazine.
 */
package au.edu.murdoch.ict373_assignment2;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Controls the main mode selection interface and manages navigation between
 * Create Mode, Edit Mode and View Mode.
 *
 * @author Nguyen Tuan Anh
 */
public class ModeSelectionController {

    private final Assignment2 app;

    /**
     * Initializes the mode selection controller.
     *
     * @param app the main application instance used to manage application
     * navigation
     */
    public ModeSelectionController(Assignment2 app) {
        this.app = app;
    }

    /**
     * Constructs the main mode selection interface.
     *
     * Creates buttons for accessing Create Mode, Edit Mode and View Mode and
     * connects them to the corresponding application navigation methods.
     *
     * @return the root component of the mode selection interface
     */
    public Parent createView() {

        Label title = new Label("ICT373 Assignment 2");
        title.setStyle(
                "-fx-font-size: 24px;"
                + "-fx-font-weight: bold;"
        );

        Label instruction
                = new Label("Select a mode to continue");

        Button createButton = new Button("Create Mode");
        Button editButton = new Button("Edit Mode");
        Button viewButton = new Button("View Mode");

        createButton.setMaxWidth(Double.MAX_VALUE);
        editButton.setMaxWidth(Double.MAX_VALUE);
        viewButton.setMaxWidth(Double.MAX_VALUE);

        createButton.setPrefHeight(45);
        editButton.setPrefHeight(45);
        viewButton.setPrefHeight(45);

        createButton.setOnAction(event
                -> app.showCreateMode()
        );

        editButton.setOnAction(event -> {

            if (app.getMagazine() == null) {
                new Alert(
                        Alert.AlertType.WARNING,
                        "Please create or load a Magazine first."
                ).showAndWait();

                return;
            }

            app.showEditMode();
        });

        viewButton.setOnAction(event -> app.showViewMode());

        VBox root = new VBox(
                15,
                title,
                instruction,
                createButton,
                editButton,
                viewButton
        );

        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setPrefWidth(450);

        return root;
    }
}
