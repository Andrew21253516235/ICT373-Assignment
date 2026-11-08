
/*
 * Title: Magazine Service Application
 * Author: Nguyen Tuan Anh
 * Date: 04 November 2026
 * File Name: CustomerController.java
 *
 * Purpose:
 * This class manages the customer creation interface.
 * It handles customer details, residential addresses,
 * supplement selection, payment methods and associate
 * customer assignments.
 *
 * Assumptions and Input Conditions:
 * - A Magazine must exist before creating customers.
 * - Customer name, email, address and customer type are required.
 * - Paying customers must provide payment information.
 * - Associate customers must be assigned to an existing
 *   paying customer.
 * - Supplement selection is optional.
 *
 * Expected Output:
 * - Creates and registers customer objects.
 * - Updates customer subscription information.
 * - Maintains relationships between paying and associate customers.
 * - Saves customer information to persistent storage.
 * - Displays validation and confirmation messages.
 */
package au.edu.murdoch.ict373_assignment2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import question2_assignment1.Magazine;
import question2_assignment1.Customer;
import question2_assignment1.PayingCustomer;
import question2_assignment1.Supplement;
import question2_assignment1.CreditCard;
import question2_assignment1.BankAccount;
import question2_assignment1.Address;
import question2_assignment1.PaymentMethod;
import javafx.scene.control.Label;

/**
 * Controls customer creation and manages customer information, subscription
 * selection, payment details and associate customer relationships.
 *
 * @author Nguyen Tuan Anh
 */
public class CustomerController {

    private final Assignment2 app;
    private final Magazine magazine;

    // Customer information
    private final TextField nameField = new TextField();
    private final TextField emailField = new TextField();
    private final TextField streetNumberField = new TextField();
    private final TextField streetNameField = new TextField();
    private final TextField suburbField = new TextField();
    private final TextField postCodeField = new TextField();
    private final Label saveStatusLabel = new Label();
    private final MagazineFileManager fileManager = new MagazineFileManager();

    private final ComboBox<String> customerTypeBox
            = new ComboBox<>();

    // Supplement selection: zero or multiple
    private final ObservableList<Supplement> availableSupplements
            = FXCollections.observableArrayList();

    private final ListView<Supplement> supplementList
            = new ListView<>(availableSupplements);

    // Payment information for paying customers
    private final ComboBox<String> paymentMethodBox
            = new ComboBox<>();

    private final TextField paymentNumberField
            = new TextField();

    private final VBox paymentSection = new VBox(8);

    // Associate information
    private final ObservableList<PayingCustomer> payingCustomers
            = FXCollections.observableArrayList();

    private final ComboBox<PayingCustomer> payingCustomerBox
            = new ComboBox<>(payingCustomers);

    private final ObservableList<String> associateItems
            = FXCollections.observableArrayList();

    private final ListView<String> associateList
            = new ListView<>(associateItems);

    private final VBox associateSection = new VBox(8);

    // Display registered customers
    private final ObservableList<String> customerItems
            = FXCollections.observableArrayList();

    private final ListView<String> customerList
            = new ListView<>(customerItems);

    /**
     * Initializes the customer controller with the main application instance
     * and the magazine being managed.
     *
     * @param app the main application instance
     * @param magazine the magazine associated with the controller
     */
    public CustomerController(
            Assignment2 app,
            Magazine magazine) {

        this.app = app;
        this.magazine = magazine;
    }

    /**
     * Constructs the customer creation interface.
     *
     * Provides input fields for customer details and addresses, supplement
     * selection controls, payment information and associate customer
     * assignments.
     *
     * @return the root component of the customer creation interface
     */
    public Parent createView() {

        Label title = new Label("Create Customer");

        title.setStyle(
                "-fx-font-size: 22px; -fx-font-weight: bold;"
        );

        nameField.setPromptText("Customer name");
        emailField.setPromptText("Email address");
        streetNumberField.setPromptText("Street number");
        streetNameField.setPromptText("Street name");
        suburbField.setPromptText("Suburb");
        postCodeField.setPromptText("Postcode");

        customerTypeBox.getItems().setAll(
                "Paying Customer",
                "Associate Customer"
        );

        customerTypeBox.setPromptText(
                "Select customer type"
        );

        // Display available supplements.
        supplementList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        supplementList.setPrefHeight(120);

        supplementList.setCellFactory(list
                -> new ListCell<Supplement>() {

            /**
             * Updates the supplement list cell to display the supplement name
             * and its weekly subscription cost.
             *
             * @param supplement the supplement to display
             * @param empty indicates whether the cell is empty
             */
            @Override
            protected void updateItem(
                    Supplement supplement,
                    boolean empty) {

                super.updateItem(supplement, empty);

                if (empty || supplement == null) {
                    setText(null);
                } else {
                    setText(
                            supplement.getSupplementName()
                            + " - $"
                            + String.format(
                                    "%.2f",
                                    supplement.getSupplementWeeklyCost()
                            )
                    );
                }
            }
        }
        );

        // Payment controls
        paymentMethodBox.getItems().setAll(
                "Credit Card",
                "Bank Account"
        );

        paymentMethodBox.setPromptText(
                "Select payment method"
        );

        paymentNumberField.setPromptText(
                "Enter card/account number"
        );

        paymentSection.getChildren().setAll(
                new Label("Payment method:"),
                paymentMethodBox,
                paymentNumberField
        );

        paymentSection.setVisible(false);
        paymentSection.setManaged(false);

        // Associate controls
        payingCustomerBox.setPromptText(
                "Select paying customer"
        );

        associateList.setPrefHeight(100);

        associateSection.getChildren().setAll(
                new Label("Assign to paying customer:"),
                payingCustomerBox,
                new Label("Current associate customers:"),
                associateList
        );

        associateSection.setVisible(false);
        associateSection.setManaged(false);

        // Update the form according to customer type.
        customerTypeBox.setOnAction(
                event -> updateTypeControls()
        );

        paymentMethodBox.setOnAction(event -> {
            String method = paymentMethodBox.getValue();

            if ("Credit Card".equals(method)) {
                paymentNumberField.setPromptText(
                        "Enter credit card number"
                );
            } else if ("Bank Account".equals(method)) {
                paymentNumberField.setPromptText(
                        "Enter bank account number"
                );
            }
        });

        payingCustomerBox.setOnAction(
                event -> refreshAssociateList()
        );

        Button createButton
                = new Button("Create Customer");

        Button backButton
                = new Button("Back to Create Mode");

        createButton.setOnAction(
                event -> handleCreateCustomer()
        );

        backButton.setOnAction(
                event -> app.showCreateMode()
        );

        // Load the data belonging to the existing Magazine.
        refreshAvailableSupplements();
        refreshPayingCustomers();
        refreshCustomerList();
        saveMagazineData();

        VBox content = new VBox(
                12,
                title,
                new Label("Customer name:"),
                nameField,
                new Label("Email address:"),
                emailField,
                new Label("Street number:"),
                streetNumberField,
                new Label("Street name:"),
                streetNameField,
                new Label("Suburb:"),
                suburbField,
                new Label("Postcode:"),
                postCodeField,
                new Label("Customer type:"),
                customerTypeBox,
                new Label(
                        "Select supplements (optional; use Ctrl "
                        + "to select multiple):"
                ),
                supplementList,
                paymentSection,
                associateSection,
                createButton,
                saveStatusLabel,
                new Label("Registered customers:"),
                customerList,
                backButton
        );

        content.setPadding(new Insets(25));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);

        return scrollPane;
    }

    /**
     * Updates the visibility and management of payment and associate customer
     * sections according to the selected customer type.
     */
    private void updateTypeControls() {

        String type = customerTypeBox.getValue();

        boolean isPaying
                = "Paying Customer".equals(type);

        boolean isAssociate
                = "Associate Customer".equals(type);

        paymentSection.setVisible(isPaying);
        paymentSection.setManaged(isPaying);

        associateSection.setVisible(isAssociate);
        associateSection.setManaged(isAssociate);
    }

    /**
     * Validates the customer information and creates either a PayingCustomer or
     * a regular Customer.
     *
     * Assigns selected supplements, configures payment information or links an
     * associate customer to an existing paying customer. Saves the updated
     * magazine information after registration.
     */
    private void handleCreateCustomer() {

        if (magazine == null) {
            showError("Please create the Magazine first.");
            return;
        }

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String type = customerTypeBox.getValue();

        String streetNumber = streetNumberField.getText().trim();
        String streetName = streetNameField.getText().trim();
        String suburb = suburbField.getText().trim();
        String postcode = postCodeField.getText().trim();

        PayingCustomer assignedPayingCustomer = null;
        String paymentNumber = null;
        String paymentMethod = null;

        if (name.isEmpty()
                || email.isEmpty()
                || streetNumber.isEmpty()
                || streetName.isEmpty()
                || suburb.isEmpty()
                || postcode.isEmpty()
                || type == null) {

            showError("Please complete all required fields.");
            return;

        } else if ("Paying Customer".equals(type)) {

            paymentMethod = paymentMethodBox.getValue();
            paymentNumber = paymentNumberField.getText().trim();

            if (name.isEmpty()
                    || email.isEmpty()
                    || streetNumber.isEmpty()
                    || streetName.isEmpty()
                    || suburb.isEmpty()
                    || postcode.isEmpty()
                    || paymentMethod == null
                    || paymentNumber.isEmpty()) {

                showError("Please complete all required fields.");
                return;
            }

            if (!paymentNumber.matches("[0-9]+")) {
                showError(
                        "The card/account number must contain digits only."
                );
                return;
            }

        } // Validate Associate Customer information.
        else if ("Associate Customer".equals(type)) {

            assignedPayingCustomer = payingCustomerBox.getValue();

            if (name.isEmpty()
                    || email.isEmpty()
                    || streetNumber.isEmpty()
                    || streetName.isEmpty()
                    || suburb.isEmpty()
                    || postcode.isEmpty()
                    || assignedPayingCustomer == null) {

                showError("Please complete all required fields.");
                return;
            }
        }

        if (!streetNumber.matches("[0-9]+")) {
            showError("Street number must contain digits only.");
            return;
        }

        if (!postcode.matches("[A-Za-z0-9][A-Za-z0-9 -]*")) {
            showError("Postcode can contain letters, numbers, spaces and hyphens.");
            return;
        }

        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            showError("Enter a valid email address.");
            return;
        }

        Address address = new Address(streetNumber, streetName, suburb, postcode);

        // An empty selection is allowed.
        ArrayList<Supplement> selectedSupplements
                = new ArrayList<>();

        for (Supplement supplement
                : supplementList.getSelectionModel().getSelectedItems()) {

            if (supplement != null) {
                selectedSupplements.add(supplement);
            }
        }

        Customer customer;

        // Create a paying customer.
        if ("Paying Customer".equals(type)) {

            PayingCustomer payingCustomer
                    = new PayingCustomer();

            payingCustomer.setCustomerName(name);
            payingCustomer.setEmailAddress(email);
            payingCustomer.setAddress(address);

            payingCustomer.setSupplements(
                    selectedSupplements
            );

            if ("Credit Card".equals(paymentMethod)) {

                payingCustomer.setPaymentMethod((PaymentMethod) new CreditCard(paymentNumber));

            } else {

                payingCustomer.setPaymentMethod((PaymentMethod) new BankAccount(paymentNumber));
            }

            customer = payingCustomer;

        } else {

            Customer newCustomer = new Customer(
                    name,
                    email,
                    new ArrayList<Supplement>(),
                    magazine.getCustomers().toArray(
                            new Customer[0]
                    )
            );

            newCustomer.setAddress(address);
            newCustomer.setSupplements(selectedSupplements);

            customer = newCustomer;
        }

        magazine.addCustomer(customer);

// Link the associate before saving.
        if ("Associate Customer".equals(type)) {
            assignedPayingCustomer.addAssociate(customer);
        }

        app.setMagazine(magazine);
        app.saveMagazine();

        refreshPayingCustomers();
        refreshCustomerList();
        refreshAssociateList();

        showInformation(
                "Customer created successfully."
        );

        // Clear entry fields for the next customer.
        nameField.clear();
        emailField.clear();
        paymentNumberField.clear();
        streetNumberField.clear();
        streetNameField.clear();
        suburbField.clear();
        postCodeField.clear();

        paymentNumberField.clear();
        supplementList.getSelectionModel().clearSelection();
        customerTypeBox.getSelectionModel().clearSelection();
        paymentMethodBox.getSelectionModel().clearSelection();
        payingCustomerBox.getSelectionModel().clearSelection();

        updateTypeControls();
        nameField.requestFocus();
    }

    /**
     * Refreshes the available supplement list using the supplements registered
     * in the current Magazine.
     */
    private void refreshAvailableSupplements() {
        availableSupplements.clear();

        if (magazine == null
                || magazine.getSupplements() == null) {
            return;
        }

        for (Supplement supplement : magazine.getSupplements()) {

            if (supplement != null) {
                availableSupplements.add(supplement);
            }
        }
    }

    /**
     * Retrieves registered paying customers from the Magazine and updates the
     * paying customer selection list.
     */
    private void refreshPayingCustomers() {

        PayingCustomer selected = payingCustomerBox.getValue();

        payingCustomers.clear();

        for (Customer customer : magazine.getCustomers()) {

            if (customer instanceof PayingCustomer) {
                payingCustomers.add(
                        (PayingCustomer) customer
                );
            }
        }

        if (selected != null && payingCustomers.contains(selected)) {
            payingCustomerBox.setValue(selected);
        }
    }

    /**
     * Refreshes the associate customer list according to the currently selected
     * paying customer.
     */
    private void refreshAssociateList() {

        associateItems.clear();

        PayingCustomer selected
                = payingCustomerBox.getValue();

        if (selected == null) {
            return;
        }

        for (Customer associate : selected.getAssociate()) {

            associateItems.add(
                    associate.getCustomerName()
                    + " - "
                    + associate.getEmailAddress()
            );
        }
    }

    /**
     * Refreshes the registered customer list and displays customer roles,
     * contact details, addresses, subscriptions and payment information where
     * applicable.
     */
    private void refreshCustomerList() {

        customerItems.clear();

        for (Customer customer : magazine.getCustomers()) {

            String role;

            if (customer instanceof PayingCustomer) {
                role = "Paying Customer";
            } else if (isAssociate(customer)) {
                role = "Associate Customer";
            } else {
                role = "Regular Customer";
            }

            customerItems.add(
                    role
                    + ": "
                    + customer.getCustomerName()
                    + " | "
                    + customer.getEmailAddress()
                    + "| Address: " + customer.getAddress()
                    + " | Supplements: "
                    + customer.getSupplements().size()
                    + customer.getSupplements()
                    + (customer instanceof PayingCustomer
                            ? " | "
                            + ((PayingCustomer) customer).getPaymentMethod()
                                    .getPaymentDetails()
                            : "")
            );
        }
    }

    /**
     * Checks whether the specified customer is registered as an associate of an
     * existing paying customer.
     *
     * @param customer the customer to check
     * @return true if the customer is an associate; otherwise false
     */
    private boolean isAssociate(Customer customer) {

        for (Customer registered : magazine.getCustomers()) {

            if (registered instanceof PayingCustomer) {

                PayingCustomer payingCustomer
                        = (PayingCustomer) registered;

                if (payingCustomer.getAssociate()
                        .contains(customer)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Displays an error alert to the user.
     *
     * @param message the error message to display
     */
    private void showError(String message) {

        new Alert(
                Alert.AlertType.ERROR,
                message
        ).showAndWait();
    }

    /**
     * Displays an informational alert to the user.
     *
     * @param message the information message to display
     */
    private void showInformation(String message) {

        new Alert(
                Alert.AlertType.INFORMATION,
                message
        ).showAndWait();
    }

    /**
     * Checks whether a Magazine exists before allowing the customer creation
     * controller to be initialized.
     *
     * @param app the main application instance
     * @param magazine the magazine to manage
     * @return a CustomerController if the magazine exists; otherwise null
     */
    public static CustomerController validateCustomer(Assignment2 app, Magazine magazine) {

        if (magazine == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setTitle("Magazine Required");
            alert.setHeaderText("Cannot Create Customer");
            alert.setContentText(
                    "Please create the Magazine first."
            );

            alert.showAndWait();
            return null;
        }

        return new CustomerController(app, magazine);
    }

    /**
     * Saves the current Magazine using the file manager. Updates the save
     * status label according to the result of the saving operation.
     */
    private void saveMagazineData() {
        try {
            fileManager.save(magazine);

            saveStatusLabel.setText("✓ Saved");
            saveStatusLabel.setStyle("-fx-text-fill: green;");

        } catch (IOException e) {

            saveStatusLabel.setText("✗ Save failed");
            saveStatusLabel.setStyle("-fx-text-fill: red;");

            e.printStackTrace();
        }

    }
}
