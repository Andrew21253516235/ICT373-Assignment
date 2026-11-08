
/*
 * Title: Magazine Service Application
 * Author: Nguyen Tuan Anh
 * Date: 04 November 2026
 * File Name: ViewModeController.java
 *
 * Purpose:
 * This class manages the View Mode interface. It displays
 * registered supplements and customers and provides
 * detailed information about selected items.
 *
 * It also manages the display of paying customer billing
 * histories using JavaFX background tasks.
 *
 * Assumptions and Input Conditions:
 * - A Magazine object is provided to display its information.
 * - Customer and supplement information is retrieved
 *   from the Magazine object.
 * - Paying customers may have existing billing history.
 * - A payment method must be assigned before generating
 *   a new monthly billing history.
 * - Billing history will be updated if PayingCustomer or their last Associated ís updated. 
 * 
 * Expected Output:
 * - Displays supplement and customer lists.
 * - Displays selected customer and supplement information.
 * - Displays paying customer billing history.
 * - Generates and saves billing history when required.
 */
package au.edu.murdoch.ict373_assignment2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import question2_assignment1.Magazine;
import javafx.concurrent.Task;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import question2_assignment1.Customer;
import question2_assignment1.PayingCustomer;
import question2_assignment1.BankAccount;
import question2_assignment1.CreditCard;
import question2_assignment1.Supplement;

/**
 * Controls the View Mode interface and displays information about the Magazine,
 * registered supplements and customers.
 *
 * Provides detailed customer information and billing history for paying
 * customers.
 *
 * @author Nguyen Tuan Anh
 */
public class ViewModeController {

    private ListView<Customer> customerListView;
    private ListView<String> supplementListView;
    private TextArea informationArea;
    private final Assignment2 app;
    private Magazine magazine;
    private PayingCustomer payingCustomer;

    /**
     * Initializes the View Mode controller with the Magazine and main
     * application instance.
     *
     * @param magazine the Magazine containing the information to display
     * @param app the main application instance
     */
    public ViewModeController(Magazine magazine, Assignment2 app) {
        this.magazine = magazine;
        this.app = app;
    }

    /**
     * Constructs the View Mode interface.
     *
     * Creates the supplement list, customer list and information panel.
     * Configures selection listeners to display details about the selected
     * supplement or customer.
     *
     * Also checks whether a paying customer requires a new monthly billing
     * history.
     *
     * @return the root component of the View Mode interface
     */
    public Parent createView() {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        // Header and Back button
        Button backButton = new Button("Back");
        backButton.setMaxWidth(Double.MAX_VALUE);

        backButton.setOnAction(event -> app.showModeSelection());

        Label title = new Label("View Mode");
        title.setStyle(
                "-fx-font-size: 24px; -fx-font-weight: bold;"
        );

        HBox header = new HBox(15, backButton, title);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(5, 5, 15, 5));

        root.setTop(header);

        // Supplement list: top-left
        Label supplementTitle = new Label("List of supplements");
        supplementTitle.setStyle("-fx-font-weight: bold;");

        supplementListView = new ListView<>();
        supplementListView.setPlaceholder(
                new Label("No supplements available")
        );

        VBox supplementBox = new VBox(
                8, supplementTitle, supplementListView
        );
        supplementBox.setPadding(new Insets(10));
        VBox.setVgrow(supplementListView, Priority.ALWAYS);
        VBox.setVgrow(supplementBox, Priority.ALWAYS);

        // Customer list: bottom-left
        Label customerTitle = new Label("List of customers");
        customerTitle.setStyle("-fx-font-weight: bold;");

        customerListView = new ListView<>();
        customerListView.setPlaceholder(
                new Label("No customers available")
        );

        VBox customerBox = new VBox(
                8, customerTitle, customerListView
        );
        customerBox.setPadding(new Insets(10));
        VBox.setVgrow(customerListView, Priority.ALWAYS);
        VBox.setVgrow(customerBox, Priority.ALWAYS);

        // Left column: two lists vertically
        VBox leftPanel = new VBox(
                5, supplementBox, customerBox
        );
        leftPanel.setPrefWidth(300);
        VBox.setVgrow(supplementBox, Priority.ALWAYS);
        VBox.setVgrow(customerBox, Priority.ALWAYS);

        // Right column: Information Panel
        Label informationTitle = new Label("Information Panel");
        informationTitle.setStyle("-fx-font-weight: bold;");

        informationArea = new TextArea();
        informationArea.setEditable(false);
        informationArea.setWrapText(true);
        informationArea.setPromptText(
                "Select a customer or supplement to view details."
        );

        VBox informationPanel = new VBox(
                10, informationTitle, informationArea
        );
        informationPanel.setPadding(new Insets(10));
        informationPanel.setPrefWidth(350);
        VBox.setVgrow(informationArea, Priority.ALWAYS);

        // Combine left and right columns
        HBox content = new HBox(
                5, leftPanel, informationPanel
        );

        HBox.setHgrow(leftPanel, Priority.ALWAYS);
        HBox.setHgrow(informationPanel, Priority.ALWAYS);

        root.setCenter(content);

        // Populate both lists
        showSupplementList();
        showCustomerList();

        // Selecting a supplement displays its information
        supplementListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {

                    if (newValue != null) {
                        customerListView.getSelectionModel()
                                .clearSelection();
                        StringBuilder details = new StringBuilder();
                        details.append("SUPPLEMENT INFORMATION\n\n");

                        // Display supplement details
                        details.append(newValue.toString()).append("\n");

                        details.append("\nSUBSCRIBED CUSTOMERS\n\n");

                        ArrayList<Customer> customers = magazine.getCustomers();

                        int count = 0;

                        if (customers != null) {
                            for (Customer customer : customers) {

                                if (customer.getSupplements() == null) {
                                    continue;
                                }

                                for (Supplement subscribedSupplement
                                        : customer.getSupplements()) {

                                    if (subscribedSupplement != null
                                            && subscribedSupplement.toString()
                                                    .equals(newValue.toString())) {

                                        details.append(customer.toString())
                                                .append("\n");

                                        count++;
                                        break;
                                    }
                                }
                            }
                        }

                        if (count == 0) {
                            details.append("No customers subscribed.");
                        }

                        informationArea.setText(details.toString());
                    }

                });

        // Selecting a customer displays their information
        customerListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {

                    supplementListView.getSelectionModel()
                            .clearSelection();

                    if (newValue != null) {
                        displayCustomerInformation(newValue);
                        if (newValue instanceof PayingCustomer) {
                            PayingCustomer payingCustomer
                                    = (PayingCustomer) newValue;

                            if (payingCustomer.getBillingHistory().isEmpty()) {

                                if (payingCustomer.getPaymentMethod() != null) {

                                    payingCustomer.generateMonthlyBillingHistory(payingCustomer, magazine);

                                    // Save the updated customer and bill.
                                    app.saveMagazine();

                                } else {
                                    informationArea.appendText(
                                            "\nCannot generate bill: "
                                            + "no payment method assigned.\n"
                                    );
                                }
                            }

                            // Display billing history using multithreading.
                            displayBillingHistory(payingCustomer);
                        }

                    }
                });

        return root;
    }

    /**
     * Retrieves registered customers from the Magazine and updates the customer
     * list displayed in the interface.
     *
     * Returns without updating the list if no Magazine exists.
     */
    public void showCustomerList() {
        if (magazine == null) {
            return;

        }
        ArrayList<Customer> customers = magazine.getCustomers();
        ObservableList<Customer> items = FXCollections.observableArrayList();

        if (customers != null) {
            items.addAll(customers);
        }

        customerListView.setItems(items);

    }

    /**
     * Retrieves registered supplements from the Magazine and updates the
     * supplement list displayed in the interface.
     *
     * Returns without updating the list if no Magazine exists.
     */
    public void showSupplementList() {
        if (magazine == null) {
            return;

        }
        Supplement[] supplements = magazine.getSupplements();

        ObservableList<String> items
                = FXCollections.observableArrayList();

        if (supplements != null) {

            for (Supplement supplement : supplements) {

                if (supplement != null) {
                    items.add(supplement.toString());
                }
            }

            supplementListView.setItems(items);
        }
    }

    /**
     * Displays detailed information about the selected customer.
     *
     * Includes customer name, address, customer type, payment method, associate
     * customers and subscriptions.
     *
     * @param customer the customer whose information will be displayed
     */
    public void displayCustomerInformation(Customer customer) {
        StringBuilder info = new StringBuilder();

        info.append("Customer Information\n\n");

        info.append("Name: ")
                .append(customer.getCustomerName())
                .append("\n");

        info.append("Address: ")
                .append(customer.getAddress())
                .append("\n");

        if (customer instanceof PayingCustomer) {
            info.append("Status: Paying Customer\n");

            PayingCustomer payingCustomer
                    = (PayingCustomer) customer;

            info.append("Payment Method: " + payingCustomer.getPaymentMethod().getPaymentDetails());

            info.append("\nAssociate Customers:\n");
            if (payingCustomer.getAssociate().isEmpty()) {
                info.append("No associate customers.\n");
            } else {
                for (Customer associate : payingCustomer.getAssociate()) {
                    info.append("- ")
                            .append(associate.getCustomerName())
                            .append("\n");
                }
            }

        } else {
            info.append("Status: Associate Customer\n");
        }

        info.append("\nSubscriptions:\n");

        if (customer.getSupplements() != null
                && !customer.getSupplements().isEmpty()) {

            for (Supplement supplement : customer.getSupplements()) {
                if (supplement != null) {
                    info.append("- ")
                            .append(supplement.toString())
                            .append("\n");
                }
            }

        } else {
            info.append("No subscriptions.\n");
        }

        informationArea.setText(info.toString());
    }

    /**
     * Retrieves and displays the billing history of a paying customer using a
     * JavaFX background Task.
     *
     * Updates the information panel when the task succeeds or displays an error
     * message when the task fails.
     *
     * @param customer the paying customer whose billing history will be
     * displayed
     */
    public void displayBillingHistory(PayingCustomer customer) {

        if (customer == null) {
            return;
        }

        Task<String> billingTask = new Task<String>() {

            /**
             * Retrieves and formats the paying customer's billing history into
             * a String.
             *
             * @return the formatted billing history
             * @throws Exception if an error occurs while processing the billing
             * information
             */
            @Override
            protected String call() throws Exception {

                StringBuilder history = new StringBuilder();
                ArrayList<String> bills
                        = customer.getBillingHistory();

                history.append(
                        "============================================================\n");

                history.append(
                        "                       BILLING HISTORY\n");

                history.append(
                        "============================================================\n\n");

                if (bills == null || bills.isEmpty()) {

                    history.append(
                            "No billing history available.\n");

                    return history.toString();
                }

                for (String bill : bills) {
                    history.append(bill);
                    history.append("\n\n");
                }

                return history.toString();
            }
        };

        billingTask.setOnSucceeded(event -> {

            informationArea.appendText(
                    "\n" + billingTask.getValue());
        });

        billingTask.setOnFailed(event -> {

            Throwable error = billingTask.getException();

            informationArea.appendText(
                    "\nFailed to load billing history.\n");

            if (error != null) {
                informationArea.appendText(
                        error.getMessage() + "\n");
            }
        });

        Thread billingThread = new Thread(billingTask);

        billingThread.setDaemon(true);

        billingThread.start();
    }

}
