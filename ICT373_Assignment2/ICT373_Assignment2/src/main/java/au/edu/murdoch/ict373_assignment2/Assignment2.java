
/*
 * Title: Magazine Service Application
 * Author: Nguyen Tuan Anh
 * Date: 04 November 2026
 * File Name: Assignment2.java
 *
 * Purpose:
 * This class serves as the main entry point of the JavaFX
 * application. It initializes the application, manages
 * navigation between Create Mode, Edit Mode and View Mode,
 * and coordinates magazine data persistence.
 *
 * Assumptions and Input Conditions:
 * - The application uses JavaFX for its graphical interface.
 * - A Magazine may be loaded from an existing save file.
 * - A Magazine must exist before customers and supplements
 *   can be managed.
 *
 * Expected Output:
 * - Displays the main application interface.
 * - Allows users to navigate between application modes.
 * - Saves and loads magazine information.
 */
package au.edu.murdoch.ict373_assignment2;

import question2_assignment1.Magazine;

import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main application class responsible for initializing the JavaFX application,
 * managing navigation and coordinating magazine data between different
 * controllers.
 *
 * @author Nguyen Tuan Anh
 */
public class Assignment2 extends Application {

    private Stage stage;
    private Magazine magazine;
    private final MagazineFileManager fileManager
            = new MagazineFileManager();

    /**
     * Initializes the JavaFX application, loads previously saved magazine data
     * and displays the mode selection screen.
     *
     * @param stage the primary JavaFX application window
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;

        try {
            magazine = fileManager.load();
        } catch (IOException | ClassNotFoundException e) {
            new Alert(
                    Alert.AlertType.ERROR,
                    "Could not load saved magazine: "
                    + e.getMessage()
            ).showAndWait();
        }

        showModeSelection();

        stage.setTitle("ICT373 Assignment 2");
        stage.show();
    }

    /**
     * Changes the application screen to the supplied interface.
     *
     * @param root the root component of the new screen
     */
    private void setScreen(Parent root) {
        stage.setScene(new Scene(root, 600, 400));
    }

    /**
     * Opens the Create Mode interface.
     */
    public void showCreateMode() {
        CreateModeController controller
                = new CreateModeController(this);

        setScreen(controller.createView());
    }

    /**
     * Validates the availability of a Magazine and opens the supplement
     * creation form when a magazine exists.
     */
    public void showSupplementForm() {
        SupplementController supplementController
                = SupplementController.validateSupplement(
                        this, magazine
                );

        if (supplementController == null) {
            return;
        }

        setScreen(supplementController.createView());
    }

    /**
     * Opens the magazine creation form.
     */
    public void showMagazineForm() {
        MagazineController magazineController
                = new MagazineController(this);

        setScreen(magazineController.createView());
    }

    /**
     * Validates the availability of a Magazine and opens the customer creation
     * form when a magazine exists.
     */
    public void showCustomerForm() {
        CustomerController controller
                = CustomerController.validateCustomer(
                        this, magazine
                );

        if (controller == null) {
            return;
        }

        setScreen(controller.createView());
    }

    /**
     * Opens the Edit Mode interface if a Magazine exists. Displays a warning if
     * no magazine is available.
     */
    public void showEditMode() {
        if (magazine == null) {
            new Alert(
                    Alert.AlertType.WARNING,
                    "Please create or load a Magazine first."
            ).showAndWait();

            return;
        }

        EditModeController controller
                = new EditModeController(this, magazine);

        setScreen(controller.createView());
    }

    /**
     * Opens the View Mode interface to display magazine information, including
     * registered supplements and customers.
     */
    public void showViewMode() {
        ViewModeController controller
                = new ViewModeController(magazine, this);

        setScreen(controller.createView());
    }

    /**
     * Returns the application to the main mode selection screen.
     */
    public void showModeSelection() {
        ModeSelectionController controller
                = new ModeSelectionController(this);

        setScreen(controller.createView());
    }

    /**
     * Displays a message and provides a button for returning to the Create Mode
     * interface.
     *
     * @param message the message to display to the user
     */
    public void showMessage(String message) {
        Label label = new Label(message);

        Button backButton
                = new Button("Back to Create Mode");

        backButton.setOnAction(
                event -> showCreateMode()
        );

        VBox root = new VBox(15, label, backButton);
        root.setPadding(new Insets(25));

        stage.setScene(new Scene(root, 600, 400));
    }

    /**
     * Saves the current magazine information using the MagazineFileManager.
     * Displays an error alert if an input/output error occurs.
     */
    public void saveMagazine() {
        try {
            fileManager.save(magazine);
        } catch (IOException e) {
            new Alert(
                    Alert.AlertType.ERROR,
                    "Could not save magazine: " + e.getMessage()
            ).showAndWait();
        }
    }

    /**
     * Updates the Magazine object managed by the application.
     *
     * @param magazine the Magazine object to assign
     */
    public void setMagazine(Magazine magazine) {
        this.magazine = magazine;
    }

    /**
     * Returns the Magazine object currently managed by the application.
     *
     * @return the current Magazine object, or null if no magazine is available
     */
    public Magazine getMagazine() {
        return magazine;
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args command-line arguments supplied when launching the
     * application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
