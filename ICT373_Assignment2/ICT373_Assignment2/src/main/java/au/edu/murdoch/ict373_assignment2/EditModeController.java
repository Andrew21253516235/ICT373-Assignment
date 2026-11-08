
/*
 * Title: Magazine Service Application
 * Author: Nguyen Tuan Anh
 * Date: 04 November 2026
 * File Name: EditModeController.java
 *
 * Purpose:
 * This class manages the Edit Mode interface. It allows
 * users to add, edit and delete supplements and customers.
 * It also manages customer addresses, subscription schedules,
 * payment methods and associate customer relationships.
 *
 * Assumptions and Input Conditions:
 * - An existing Magazine must be provided.
 * - Users must select the relevant item before editing
 *   or deleting it.
 * - Supplement names and weekly costs must be valid.
 * - Customer information must satisfy validation rules.
 * - Subscription selections can be configured for four weeks.
 *
 * Expected Output:
 * - Updates magazine, supplement and customer information.
 * - Updates customer subscription schedules.
 * - Maintains payment and associate customer relationships.
 * - Saves changes to persistent storage.
 * - Displays status and validation messages.
 */
package au.edu.murdoch.ict373_assignment2;

import question2_assignment1.Address;
import question2_assignment1.BankAccount;
import question2_assignment1.CreditCard;
import question2_assignment1.Customer;
import question2_assignment1.Magazine;
import question2_assignment1.PayingCustomer;
import question2_assignment1.Supplement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Controls the Edit Mode interface and provides operations for managing
 * magazine supplements and customers.
 *
 * Supports adding, editing and deleting records, modifying customer
 * information, managing subscription schedules and updating payment methods.
 *
 * @author Nguyen Tuan Anh
 */
public class EditModeController {

    private final Assignment2 app;
    private final Magazine magazine;

    private final ObservableList<Supplement> supplementItems
            = FXCollections.observableArrayList();

    private final ObservableList<Customer> customerItems
            = FXCollections.observableArrayList();

    private final ListView<Supplement> supplementList
            = new ListView<>(supplementItems);

    private final ListView<Customer> customerList
            = new ListView<>(customerItems);

    private final Label statusLabel = new Label();

    /**
     * Initializes the Edit Mode controller with the application instance and
     * existing magazine.
     *
     * @param app the main application instance
     * @param magazine the magazine whose information will be edited
     */
    public EditModeController(Assignment2 app, Magazine magazine) {
        this.app = app;
        this.magazine = magazine;
    }

    /**
     * Constructs the Edit Mode interface.
     *
     * Displays registered supplements and customers, provides editing controls
     * and connects buttons to their respective event handlers.
     *
     * @return the root component of the Edit Mode interface
     */
    public Parent createView() {

        Label title = new Label("Edit Mode");

        title.setStyle(
                "-fx-font-size: 22px;"
                + "-fx-font-weight: bold;"
        );

        Label magazineLabel = new Label(
                "Magazine: " + magazine.getMagazineName()
        );

        // Display supplements with their weekly costs.
        supplementList.setCellFactory(list
                -> new ListCell<Supplement>() {

            /**
             * Updates a supplement cell to display its name and weekly
             * subscription cost.
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
        });

        // Display customer names and email addresses.
        customerList.setCellFactory(list
                -> new ListCell<Customer>() {

            /**
             * Updates a customer cell to display the customer's name, email,
             * customer type, address, payment details and supplement
             * subscriptions.
             *
             * @param customer the customer to display
             * @param empty indicates whether the cell is empty
             */
            @Override
            protected void updateItem(Customer customer, boolean empty) {

                super.updateItem(customer, empty);

                if (empty || customer == null) {
                    setText(null);
                } else {
                    setText(
                            customer.getCustomerName()
                            + " | "
                            + customer.getEmailAddress()
                            + getCustomerType(customer)
                            + " | Adress: "
                            + customer.getAddress()
                            + (customer instanceof PayingCustomer
                                    ? " | "
                                    + ((PayingCustomer) customer).getPaymentMethod()
                                            .getPaymentDetails()
                                    : "")
                            + " | Supplements: "
                            + customer.getSupplements().size()
                            + customer.getSupplements()
                    );
                }
            }
        });

        supplementList.setPrefHeight(150);
        customerList.setPrefHeight(180);

        // Supplement operations.
        Button addSupplementButton
                = new Button("Add Supplement");

        Button editSupplementButton
                = new Button("Edit Selected Supplement");

        Button deleteSupplementButton
                = new Button("Delete Selected Supplement");

        // Customer operations.
        Button addCustomerButton
                = new Button("Add Customer");

        Button editCustomerButton
                = new Button("Edit Name and Email");

        Button editAddressButton
                = new Button("Edit Address");

        Button editSubscriptionsButton
                = new Button("Edit Subscriptions");

        Button editPaymentButton
                = new Button("Edit Payment Method");

        Button deleteCustomerButton
                = new Button("Delete Selected Customer");

        Button backButton
                = new Button("Back to Mode Selection");

        // Edit Mode toggle button belongs to this controller.
        Button editButton = new Button("Edit Mode");

        // Connect buttons to their event handlers.
        addSupplementButton.setOnAction(
                event -> addSupplement()
        );

        editSupplementButton.setOnAction(
                event -> editSupplement()
        );

        deleteSupplementButton.setOnAction(
                event -> deleteSupplement()
        );

        addCustomerButton.setOnAction(event -> showCustomerListForm());

        editCustomerButton.setOnAction(
                event -> editCustomerDetails()
        );

        editAddressButton.setOnAction(
                event -> editCustomerAddress()
        );

        editSubscriptionsButton.setOnAction(
                event -> editCustomerSubscriptions()
        );

        editPaymentButton.setOnAction(
                event -> editPaymentMethod()
        );

        backButton.setOnAction(
                event -> app.showModeSelection()
        );
        deleteCustomerButton.setOnAction(
                event -> deleteCustomer()
        );

        supplementList.getSelectionModel()
                .setSelectionMode(SelectionMode.MULTIPLE);

        customerList.getSelectionModel()
                .setSelectionMode(SelectionMode.MULTIPLE);

        // Group all editing operations in one container.
        VBox editOptions = new VBox(
                10,
                addSupplementButton,
                editSupplementButton,
                deleteSupplementButton,
                addCustomerButton,
                editCustomerButton,
                editAddressButton,
                editSubscriptionsButton,
                editPaymentButton,
                deleteCustomerButton
        );

        // Hide the editing options at first.
        editOptions.setVisible(false);
        editOptions.setManaged(false);

        // Toggle the editing options on and off.
        editButton.setOnAction(event -> {

            boolean showOptions = !editOptions.isVisible();

            editOptions.setVisible(showOptions);
            editOptions.setManaged(showOptions);

            if (showOptions) {
                editButton.setText("Hide Edit Options");
            } else {
                editButton.setText("Edit Mode");
            }
        });

        VBox content = new VBox(
                10,
                title,
                magazineLabel,
                statusLabel,
                new Label("SUPPLEMENTS"),
                supplementList,
                new Label("CUSTOMERS"),
                customerList,
                editButton,
                editOptions,
                backButton
        );

        content.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);

        refreshLists();

        return scrollPane;
    }

    /**
     * Collects and validates supplement information before registering a new
     * Supplement in the current Magazine.
     */
    private void addSupplement() {

        Optional<String> nameResult = askText(
                "Add Supplement",
                "Enter supplement name:",
                ""
        );

        if (!nameResult.isPresent()) {
            return;
        }

        String name = nameResult.get().trim();

        if (name.isEmpty()) {
            showError("Supplement name cannot be empty.");
            return;
        }

        Optional<String> costResult = askText(
                "Add Supplement",
                "Enter weekly cost:",
                ""
        );

        if (!costResult.isPresent()) {
            return;
        }

        double cost;

        try {
            cost = Double.parseDouble(costResult.get().trim());
        } catch (NumberFormatException e) {
            showError("Enter a valid numeric cost.");
            return;
        }

        if (Double.isNaN(cost)
                || Double.isInfinite(cost)
                || cost < 0) {

            showError("Weekly cost must be non-negative.");
            return;
        }

        Supplement supplement = new Supplement(name, cost);

        try {
            magazine.addSupplement(supplement);
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
            return;
        }

        saveChanges("Supplement added successfully.");
    }

    /**
     * Allows the user to modify the name and weekly cost of the selected
     * supplement.
     *
     * Checks for empty names, duplicate names and invalid weekly costs before
     * updating the supplement.
     */
    private void editSupplement() {

        Supplement selected
                = supplementList.getSelectionModel()
                        .getSelectedItem();

        if (selected == null) {
            showError("Select a supplement first.");
            return;
        }

        Optional<String> nameResult = askText(
                "Edit Supplement",
                "Supplement name:",
                selected.getSupplementName()
        );

        if (!nameResult.isPresent()) {
            return;
        }

        String newName = nameResult.get().trim();

        if (newName.isEmpty()) {
            showError("Supplement name cannot be empty.");
            return;
        }

        // Prevent duplicate supplement names.
        for (Supplement item : magazine.getSupplements()) {

            if (item != selected
                    && item.getSupplementName()
                            .equalsIgnoreCase(newName)) {

                showError(
                        "A supplement with that name already exists."
                );

                return;
            }
        }

        Optional<String> costResult = askText(
                "Edit Supplement",
                "Weekly cost:",
                String.valueOf(
                        selected.getSupplementWeeklyCost()
                )
        );

        if (!costResult.isPresent()) {
            return;
        }

        double newCost;

        try {
            newCost = Double.parseDouble(costResult.get().trim());
        } catch (NumberFormatException e) {
            showError("Enter a valid numeric cost.");
            return;
        }

        if (Double.isNaN(newCost)
                || Double.isInfinite(newCost)
                || newCost < 0) {

            showError("Weekly cost must be non-negative.");
            return;
        }

        selected.setSupplementName(newName);
        selected.setSupplementWeeklyCost(newCost);

        saveChanges("Supplement updated successfully.");
    }

    /**
     * Deletes the selected supplements from the Magazine after obtaining
     * confirmation from the user.
     */
    private void deleteSupplement() {

        ObservableList<Supplement> selected
                = FXCollections.observableArrayList(
                        supplementList.getSelectionModel()
                                .getSelectedItems()
                );

        if (selected.isEmpty()) {
            showError("Select at least one supplement.");
            return;
        }

        if (!confirm(
                "Delete Supplements",
                "Delete " + selected.size()
                + " selected supplement(s)?")) {
            return;
        }

        for (Supplement supplement : selected) {
            magazine.removeSupplement(supplement);
        }

        saveChanges("Supplement deleted successfully.");
    }

    /**
     * Allows the user to update the selected customer's name and email address
     * after validating the input.
     */
    private void editCustomerDetails() {

        Customer customer = getSelectedCustomer();

        if (customer == null) {
            return;
        }

        Optional<String> nameResult = askText(
                "Edit Customer",
                "Customer name:",
                customer.getCustomerName()
        );

        if (!nameResult.isPresent()) {
            return;
        }

        String name = nameResult.get().trim();

        if (name.isEmpty()) {
            showError("Customer name cannot be empty.");
            return;
        }

        Optional<String> emailResult = askText(
                "Edit Customer",
                "Email address:",
                customer.getEmailAddress()
        );

        if (!emailResult.isPresent()) {
            return;
        }

        String email = emailResult.get().trim();

        if (!email.matches(
                "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {

            showError("Enter a valid email address.");
            return;
        }

        customer.setCustomerName(name);
        customer.setEmailAddress(email);

        saveChanges("Customer information updated.");
    }

    /**
     * Allows the user to modify the selected customer's residential address.
     *
     * Validates the street number and postcode before updating the customer's
     * address information.
     */
    private void editCustomerAddress() {

        Customer customer = getSelectedCustomer();

        if (customer == null) {
            return;
        }

        Optional<String> numberResult = askText(
                "Edit Address",
                "Street number:",
                ""
        );

        if (!numberResult.isPresent()) {
            return;
        }

        Optional<String> streetResult = askText(
                "Edit Address",
                "Street name:",
                ""
        );

        if (!streetResult.isPresent()) {
            return;
        }

        Optional<String> suburbResult = askText(
                "Edit Address",
                "Suburb:",
                ""
        );

        if (!suburbResult.isPresent()) {
            return;
        }

        Optional<String> postcodeResult = askText(
                "Edit Address",
                "Postcode:",
                ""
        );

        if (!postcodeResult.isPresent()) {
            return;
        }

        String number = numberResult.get().trim();
        String street = streetResult.get().trim();
        String suburb = suburbResult.get().trim();
        String postcode = postcodeResult.get().trim();

        if (number.isEmpty()
                || street.isEmpty()
                || suburb.isEmpty()
                || postcode.isEmpty()) {

            showError("Complete all address fields.");
            return;
        }

        if (!number.matches("[0-9]+")) {
            showError("Street number must contain digits only.");
            return;
        }

        if (!postcode.matches("[A-Za-z0-9][A-Za-z0-9 -]*")) {
            showError("Enter a valid postcode.");
            return;
        }

        Address address = new Address(
                number,
                street,
                suburb,
                postcode
        );

        customer.setAddress(address);

        saveChanges("Customer address updated.");
    }

    // ==================================================
    // CUSTOMER SUBSCRIPTIONS
    // ==================================================
    /**
     * Allows the user to modify the selected customer's supplement subscription
     * schedule.
     *
     * Provides separate selection controls for each of the four billing weeks
     * and updates the customer's subscription information based on the selected
     * supplements and weeks.
     */
    private void editCustomerSubscriptions() {

        Customer customer = getSelectedCustomer();

        if (customer == null) {
            return;
        }

        Dialog<Map<String, ArrayList<Integer>>> dialog
                = new Dialog<>();

        dialog.setTitle("Edit Subscriptions");

        dialog.setHeaderText(
                "Select supplements for "
                + customer.getCustomerName()
        );

        dialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK,
                ButtonType.CANCEL
        );

        GridPane choices = new GridPane();

        choices.setHgap(12);
        choices.setVgap(12);
        choices.setPadding(new Insets(10));

        // Column headings
        choices.add(new Label("Supplement"), 0, 0);
        choices.add(new Label("Week 1"), 1, 0);
        choices.add(new Label("Week 2"), 2, 0);
        choices.add(new Label("Week 3"), 3, 0);
        choices.add(new Label("Week 4"), 4, 0);

        // One array of four checkboxes for each supplement.
        Map<String, CheckBox[]> checkBoxes
                = new LinkedHashMap<>();

        Map<String, ArrayList<Integer>> existingSchedule
                = customer.getSupplementSubscriptionWeeks();

        int row = 1;

        for (Supplement supplement : magazine.getSupplements()) {

            String name = supplement.getSupplementName();

            Label supplementLabel = new Label(
                    name + " - $"
                    + String.format(
                            "%.2f",
                            supplement.getSupplementWeeklyCost()
                    )
            );

            choices.add(supplementLabel, 0, row);

            CheckBox[] weekBoxes = new CheckBox[4];

            ArrayList<Integer> savedWeeks
                    = existingSchedule.get(name);

            boolean previouslySubscribed
                    = customer.getSupplements() != null
                    && customer.getSupplements().contains(supplement);

            for (int i = 0; i < 4; i++) {

                CheckBox box = new CheckBox();

                if (savedWeeks != null) {
                    // Restore the saved week selections.
                    box.setSelected(savedWeeks.contains(i + 1));
                } else {
                    // Existing subscriptions without a saved schedule
                    // continue to apply to all four weeks.
                    box.setSelected(previouslySubscribed);
                }

                weekBoxes[i] = box;

                choices.add(box, i + 1, row);
            }

            checkBoxes.put(name, weekBoxes);

            row++;
        }

        if (checkBoxes.isEmpty()) {
            choices.add(
                    new Label("No supplements available."),
                    0,
                    1
            );
        }

        dialog.getDialogPane().setContent(choices);

        dialog.setResultConverter(button -> {

            if (button != ButtonType.OK) {
                return null;
            }

            Map<String, ArrayList<Integer>> selectedSchedule
                    = new LinkedHashMap<>();

            for (Map.Entry<String, CheckBox[]> entry
                    : checkBoxes.entrySet()) {

                ArrayList<Integer> selectedWeeks
                        = new ArrayList<>();

                CheckBox[] boxes = entry.getValue();

                for (int i = 0; i < boxes.length; i++) {

                    if (boxes[i].isSelected()) {
                        selectedWeeks.add(i + 1);
                    }
                }

                // Save only supplements with at least one selected week.
                if (!selectedWeeks.isEmpty()) {
                    selectedSchedule.put(
                            entry.getKey(),
                            selectedWeeks
                    );
                }
            }

            return selectedSchedule;
        });

        Optional<Map<String, ArrayList<Integer>>> result
                = dialog.showAndWait();

        if (!result.isPresent()) {
            return;
        }

        // Save the week schedule.
        customer.setSupplementSubscriptionWeeks(result.get());

        // Keep your existing supplement list synchronized.
        ArrayList<Supplement> selectedSupplements
                = new ArrayList<>();

        for (Supplement supplement : magazine.getSupplements()) {

            if (result.get().containsKey(
                    supplement.getSupplementName())) {

                selectedSupplements.add(supplement);
            }
        }

        customer.setSupplementSubscriptionWeeks(result.get());
        customer.setSupplements(selectedSupplements);

        // Identify the paying customer responsible for the bill.
        PayingCustomer billingOwner = null;

        if (customer instanceof PayingCustomer) {

            billingOwner = (PayingCustomer) customer;

        } else {

            for (Customer registered : magazine.getCustomers()) {

                if (registered instanceof PayingCustomer) {

                    PayingCustomer payer = (PayingCustomer) registered;

                    if (payer.getAssociate().contains(customer)) {
                        billingOwner = payer;
                        break;
                    }
                }
            }
        }

        if (billingOwner != null) {

            billingOwner.generateMonthlyBillingHistory(
                    billingOwner,
                    magazine
            );
        }
        app.saveMagazine();

        saveChanges("Customer subscriptions updated.");
    }

    /**
     * Allows the user to modify the payment method of a selected
     * PayingCustomer.
     *
     * Supports credit card and bank account payment methods and validates the
     * entered payment information.
     */
    private void editPaymentMethod() {

        Customer customer = getSelectedCustomer();

        if (customer == null) {
            return;
        }

        if (!(customer instanceof PayingCustomer)) {

            showError(
                    "Only a Paying Customer has a payment method."
            );

            return;
        }

        PayingCustomer payer = (PayingCustomer) customer;

        ChoiceDialog<String> methodDialog
                = new ChoiceDialog<>(
                        "Credit Card",
                        "Credit Card",
                        "Bank Account"
                );

        methodDialog.setTitle("Edit Payment Method");

        methodDialog.setHeaderText(
                "Choose a payment method for "
                + payer.getCustomerName()
        );

        Optional<String> methodResult
                = methodDialog.showAndWait();

        if (!methodResult.isPresent()) {
            return;
        }

        Optional<String> numberResult = askText(
                "Edit Payment Method",
                "Enter card/account number:",
                ""
        );

        if (!numberResult.isPresent()) {
            return;
        }

        String number = numberResult.get().trim();

        if (!number.matches("[0-9]+")) {
            showError(
                    "The card/account number must contain digits only."
            );
            return;
        }

        if ("Credit Card".equals(methodResult.get())) {

            payer.setPaymentMethod(
                    new CreditCard(number)
            );

        } else {

            payer.setPaymentMethod(
                    new BankAccount(number)
            );
        }

        saveChanges("Payment method updated.");
    }

    /**
     * Deletes selected customers after obtaining confirmation from the user.
     *
     * Maintains associate customer relationships by removing deleted associates
     * from their paying customers. When a paying customer is deleted, their
     * associated customers are also removed.
     *
     * Updates affected billing histories and saves the changed Magazine
     * information.
     */
    private void deleteCustomer() {

        ObservableList<Customer> selected
                = FXCollections.observableArrayList(
                        customerList.getSelectionModel()
                                .getSelectedItems()
                );

        if (selected.isEmpty()) {
            showError("Select at least one customer.");
            return;
        }

        if (!confirm(
                "Delete Customers",
                "Delete " + selected.size()
                + " selected customer(s)?")) {
            return;
        }

        ArrayList<PayingCustomer> affectedPayers
                = new ArrayList<>();

        for (Customer customer : selected) {

            for (Customer registered : magazine.getCustomers()) {

                if (registered instanceof PayingCustomer) {

                    PayingCustomer payer
                            = (PayingCustomer) registered;

                    if (payer != customer
                            && payer.getAssociate().contains(customer)) {

                        payer.removeAssociate(customer);

                        // Do not regenerate a payer that is also being deleted.
                        if (!selected.contains(payer)
                                && !affectedPayers.contains(payer)) {

                            affectedPayers.add(payer);
                        }
                    }
                }
            }
        }

        for (Customer customer : selected) {

            if (customer instanceof PayingCustomer) {

                PayingCustomer payer = (PayingCustomer) customer;

                // Remove all associates belonging to this payer.
                ArrayList<Customer> associates
                        = new ArrayList<>(payer.getAssociate());

                for (Customer associate : associates) {
                    magazine.removeCustomer(associate);
                }

                payer.getAssociate().clear();
            }

            magazine.removeCustomer(customer);
        }

        for (PayingCustomer payer : affectedPayers) {

            if (magazine.getCustomers().contains(payer)) {

                payer.generateMonthlyBillingHistory(
                        payer,
                        magazine
                );
            }
        }

        // STEP 4: Save and refresh the GUI.
        saveChanges("Selected customers deleted successfully.");
    }

    /**
     * Retrieves the customer currently selected in the customer list. Displays
     * an error message when no customer is selected.
     *
     * @return the selected Customer object, or null if none is selected
     */
    private Customer getSelectedCustomer() {

        Customer customer
                = customerList.getSelectionModel()
                        .getSelectedItem();

        if (customer == null) {
            showError("Select a customer first.");
        }

        return customer;
    }

    /**
     * Determines the displayed customer type based on the customer's
     * relationship with registered paying customers.
     *
     * @param customer the customer whose type is being checked
     * @return the customer type description
     */
    private String getCustomerType(Customer customer) {

        if (customer instanceof PayingCustomer) {
            return " | Paying Customer";
        }

        for (Customer registered : magazine.getCustomers()) {

            if (registered instanceof PayingCustomer) {

                PayingCustomer payer = (PayingCustomer) registered;

                if (payer.getAssociate().contains(customer)) {
                    return " | Associate Customer";
                }
            }
        }
        return "";
    }

    /**
     * Refreshes the supplement and customer lists using the latest information
     * stored in the Magazine.
     */
    private void refreshLists() {

        supplementItems.setAll(
                Arrays.asList(magazine.getSupplements())
        );

        customerItems.setAll(
                magazine.getCustomers()
        );

        supplementList.refresh();
        customerList.refresh();
    }

    /**
     * Saves the current Magazine information, refreshes the interface lists and
     * updates the status message.
     *
     * @param message the status message to display after saving
     */
    private void saveChanges(String message) {

        app.setMagazine(magazine);

        app.saveMagazine();

        refreshLists();

        statusLabel.setText(message);
    }

    /**
     * Displays a customer creation form within Edit Mode.
     *
     * Collects customer details, address information, customer type,
     * subscription selections and payment information where applicable.
     * Validates the entered information before registering the new customer in
     * the Magazine.
     */
    private void showCustomerListForm() {

        if (magazine == null) {
            showError("Please create or load a Magazine first.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle("Add Customer");
        dialog.setHeaderText(
                "Enter the new customer's information."
        );

        ButtonType addButtonType = new ButtonType(
                "Add Customer",
                ButtonBar.ButtonData.OK_DONE
        );

        dialog.getDialogPane().getButtonTypes().addAll(
                addButtonType,
                ButtonType.CANCEL
        );

        // Customer details.
        TextField nameField = new TextField();
        nameField.setPromptText("Customer name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email address");

        TextField streetNumberField = new TextField();
        streetNumberField.setPromptText("Street number");

        TextField streetNameField = new TextField();
        streetNameField.setPromptText("Street name");

        TextField suburbField = new TextField();
        suburbField.setPromptText("Suburb");

        TextField postcodeField = new TextField();
        postcodeField.setPromptText("Postcode");

        ComboBox<String> customerTypeBox = new ComboBox<>();

        customerTypeBox.getItems().setAll(
                "Paying Customer",
                "Associate Customer"
        );

        customerTypeBox.setPromptText("Select customer type");
        customerTypeBox.setMaxWidth(Double.MAX_VALUE);

        ListView<Supplement> availableSupplements
                = new ListView<>();

        availableSupplements.getItems().setAll(
                Arrays.asList(magazine.getSupplements())
        );

        availableSupplements.getSelectionModel()
                .setSelectionMode(SelectionMode.MULTIPLE);

        availableSupplements.setPrefHeight(120);

        availableSupplements.setCellFactory(list
                -> new ListCell<Supplement>() {

            /**
             * Updates a supplement cell in the customer creation form to
             * display its name and weekly cost.
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
        });

        ComboBox<String> paymentMethodBox = new ComboBox<>();

        paymentMethodBox.getItems().setAll(
                "Credit Card",
                "Bank Account"
        );

        paymentMethodBox.setPromptText("Payment method");
        paymentMethodBox.setMaxWidth(Double.MAX_VALUE);

        TextField paymentNumberField = new TextField();
        paymentNumberField.setPromptText(
                "Card/account number"
        );

        VBox paymentSection = new VBox(
                8,
                new Label("Payment method:"),
                paymentMethodBox,
                paymentNumberField
        );

        paymentSection.setVisible(false);
        paymentSection.setManaged(false);

        ComboBox<PayingCustomer> payingCustomerBox
                = new ComboBox<>();

        // Display existing paying customers.
        for (Customer customer : magazine.getCustomers()) {

            if (customer instanceof PayingCustomer) {
                payingCustomerBox.getItems().add(
                        (PayingCustomer) customer
                );
            }
        }

        payingCustomerBox.setPromptText(
                "Select paying customer"
        );

        payingCustomerBox.setMaxWidth(Double.MAX_VALUE);

        payingCustomerBox.setCellFactory(list
                -> new ListCell<PayingCustomer>() {

            /**
             * Updates a paying customer cell to display the customer's name and
             * email address in the selection list.
             *
             * @param payer the paying customer to display
             * @param empty indicates whether the cell is empty
             */
            @Override
            protected void updateItem(
                    PayingCustomer payer,
                    boolean empty) {

                super.updateItem(payer, empty);

                if (empty || payer == null) {
                    setText(null);
                } else {
                    setText(
                            payer.getCustomerName()
                            + " | "
                            + payer.getEmailAddress()
                    );
                }
            }
        });

        payingCustomerBox.setButtonCell(
                new ListCell<PayingCustomer>() {

            /**
             * Updates the selected paying customer button cell with the
             * customer's name and email address.
             *
             * @param payer the paying customer to display
             * @param empty indicates whether the cell is empty
             */
            @Override
            protected void updateItem(
                    PayingCustomer payer,
                    boolean empty) {

                super.updateItem(payer, empty);

                if (empty || payer == null) {
                    setText(null);
                } else {
                    setText(
                            payer.getCustomerName()
                            + " | "
                            + payer.getEmailAddress()
                    );
                }
            }
        });

        VBox associateSection = new VBox(
                8,
                new Label("Paid for by:"),
                payingCustomerBox
        );

        associateSection.setVisible(false);
        associateSection.setManaged(false);

        // Show the correct controls for the selected type.
        customerTypeBox.setOnAction(event -> {

            String type = customerTypeBox.getValue();

            boolean isPaying
                    = "Paying Customer".equals(type);

            boolean isAssociate
                    = "Associate Customer".equals(type);

            paymentSection.setVisible(isPaying);
            paymentSection.setManaged(isPaying);

            associateSection.setVisible(isAssociate);
            associateSection.setManaged(isAssociate);
        });

        VBox form = new VBox(
                8,
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
                postcodeField,
                new Label("Customer type:"),
                customerTypeBox,
                new Label(
                        "Supplements (optional; use Ctrl to select multiple):"
                ),
                availableSupplements,
                paymentSection,
                associateSection
        );

        ScrollPane formScrollPane = new ScrollPane(form);
        formScrollPane.setFitToWidth(true);
        formScrollPane.setPrefViewportHeight(450);

        dialog.getDialogPane().setContent(formScrollPane);
        dialog.getDialogPane().setPrefWidth(450);
        Button addButton = (Button) dialog.getDialogPane()
                .lookupButton(addButtonType);

        addButton.addEventFilter(
                ActionEvent.ACTION,
                event -> {

                    String name = nameField.getText().trim();
                    String email = emailField.getText().trim();
                    String number = streetNumberField.getText().trim();
                    String street = streetNameField.getText().trim();
                    String suburb = suburbField.getText().trim();
                    String postcode = postcodeField.getText().trim();
                    String type = customerTypeBox.getValue();

                    PayingCustomer assignedPayingCustomer = null;
                    String paymentNumber = null;
                    String paymentMethod = null;

                    if (name.isEmpty()
                    || email.isEmpty()
                    || number.isEmpty()
                    || street.isEmpty()
                    || suburb.isEmpty()
                    || postcode.isEmpty()
                    || type == null) {
                        event.consume();
                        showError("Please complete all required fields.");
                        return;
                    }

                    if ("Paying Customer".equals(type)) {

                        paymentMethod = paymentMethodBox.getValue();
                        paymentNumber = paymentNumberField.getText().trim();

                        if (name.isEmpty()
                        || email.isEmpty()
                        || number.isEmpty()
                        || street.isEmpty()
                        || suburb.isEmpty()
                        || postcode.isEmpty()
                        || paymentMethod == null
                        || paymentNumber.isEmpty()) {
                            event.consume();
                            showError("Please complete all required fields.");

                            return;
                        }

                        if (!paymentNumber.matches("[0-9]+")) {
                            event.consume();
                            showError("The card/account number must contain digits only.");
                            return;
                        }

                    } // Validate Associate Customer information.
                    else if ("Associate Customer".equals(type)) {

                        assignedPayingCustomer = payingCustomerBox.getValue();

                        if (name.isEmpty()
                        || email.isEmpty()
                        || number.isEmpty()
                        || street.isEmpty()
                        || suburb.isEmpty()
                        || postcode.isEmpty()
                        || assignedPayingCustomer == null) {
                            event.consume();
                            showError("Please complete all required fields.");

                            return;
                        }
                    }
                    if (!email.matches(
                            "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                        event.consume();
                        showError("Enter a valid email address.");
                        return;
                    }

                    if (!number.matches("[0-9]+")) {
                        event.consume();
                        showError(
                                "Street number must contain digits only."
                        );

                        return;
                    }

                    if (!postcode.matches(
                            "[A-Za-z0-9][A-Za-z0-9 -]*")) {
                        event.consume();
                        showError("Enter a valid postcode.");
                        return;
                    }

                });

        dialog.setResultConverter(button -> button);

        Optional<ButtonType> result = dialog.showAndWait();

        if (!result.isPresent()
                || !result.get().equals(addButtonType)) {

            return;
        }

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();

        Address address = new Address(
                streetNumberField.getText().trim(),
                streetNameField.getText().trim(),
                suburbField.getText().trim(),
                postcodeField.getText().trim()
        );

        ArrayList<Supplement> selectedSupplements
                = new ArrayList<>(
                        availableSupplements
                                .getSelectionModel()
                                .getSelectedItems()
                );

        String type = customerTypeBox.getValue();

        Customer newCustomer;

        PayingCustomer assignedPayer = null;

        if ("Paying Customer".equals(type)) {

            PayingCustomer payer = new PayingCustomer();

            payer.setCustomerName(name);
            payer.setEmailAddress(email);
            payer.setAddress(address);
            payer.setSupplements(selectedSupplements);

            String paymentNumber
                    = paymentNumberField.getText().trim();

            if ("Credit Card".equals(paymentMethodBox.getValue())) {

                payer.setPaymentMethod(
                        new CreditCard(paymentNumber)
                );

            } else {

                payer.setPaymentMethod(
                        new BankAccount(paymentNumber)
                );
            }

            newCustomer = payer;

        } else {

            newCustomer = new Customer(
                    name,
                    email,
                    new ArrayList<Supplement>(),
                    magazine.getCustomers().toArray(
                            new Customer[0]
                    )
            );

            newCustomer.setAddress(address);
            newCustomer.setSupplements(selectedSupplements);

            if ("Associate Customer".equals(type)) {
                assignedPayer = payingCustomerBox.getValue();
            }
        }

        magazine.addCustomer(newCustomer);

        if (assignedPayer != null) {
            assignedPayer.addAssociate(newCustomer);

            assignedPayer.generateMonthlyBillingHistory(
                    assignedPayer,
                    magazine);
        }

        saveChanges("Customer added successfully.");
    }

    /**
     * Displays a text input dialog and retrieves the user's response.
     *
     * @param title the title of the dialog
     * @param prompt the instruction displayed in the dialog
     * @param initialValue the initial text value
     * @return an Optional containing the entered text, or an empty Optional if
     * the dialog is cancelled
     */
    private Optional<String> askText(
            String title,
            String prompt,
            String initialValue) {

        TextInputDialog dialog
                = new TextInputDialog(initialValue);

        dialog.setTitle(title);
        dialog.setHeaderText(prompt);

        return dialog.showAndWait();
    }

    /**
     * Displays a confirmation dialog and retrieves the user's decision.
     *
     * @param title the title of the confirmation dialog
     * @param message the confirmation message
     * @return true if the user confirms the operation; otherwise false
     */
    private boolean confirm(
            String title,
            String message) {

        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                message,
                ButtonType.YES,
                ButtonType.NO
        );

        alert.setTitle(title);
        alert.setHeaderText(null);

        return alert.showAndWait()
                .orElse(ButtonType.NO) == ButtonType.YES;
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
}
