
/*
 * Title: Magazine Service Application
 * Author: Nguyen Tuan Anh
 * Date: 04 November 2026
 * File Name: PayingCustomer.java
 *
 * Purpose:
 * This class represents a paying customer in the Magazine
 * Service application. It manages associate customers,
 * payment methods, monthly subscription costs and billing history.
 *
 * Assumptions and Input Conditions:
 * - A paying customer inherits general customer information.
 * - A payment method must be assigned before generating
 *   a monthly payment email.
 * - Associate customers are maintained through the paying
 *   customer's associate list.
 * - Magazine and supplement costs are calculated over
 *   a four-week billing period.
 *
 * Expected Output:
 * - Calculates monthly subscription costs.
 * - Generates monthly payment emails.
 * - Generates weekly and monthly billing records.
 * - Manages associate customers and payment methods.
 * - Stores and retrieves billing history.
 */
package question2_assignment1;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents a customer who pays for magazine subscriptions and manages
 * subscriptions for associated customers.
 *
 * Extends Customer and provides functionality for payment methods, monthly cost
 * calculation, associate customer management and billing history.
 *
 * @author Nguyen Tuan Anh
 */
public class PayingCustomer extends Customer {

    private static final long serialVersionUID = 1L;
    public Magazine magazine;
    public Supplement supplement;
    private double fourWeekTotal = 0.0;

    private ArrayList<Customer> associate;
    private PaymentMethod paymentMethod;
    private final ArrayList<String> billingHistory = new ArrayList<>();

    /**
     * Creates a PayingCustomer with default customer details, an empty list of
     * associate customers and no assigned payment method.
     */
    public PayingCustomer() {
        super();

        associate = new ArrayList<>();
        paymentMethod = null;
    }

    /**
     * Returns the list of customers associated with this paying customer.
     *
     * @return the list of associate customers
     */
    public ArrayList<Customer> getAssociate() {
        return associate;
    }

    /**
     * Returns the payment method assigned to this paying customer.
     *
     * @return the assigned payment method, or null if none has been assigned
     */
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Adds a customer to this paying customer's associate list if the customer
     * has not already been registered.
     *
     * @param customer the customer to add as an associate
     */
    public void addAssociate(Customer customer) {
        if (!associate.contains(customer)) {
            associate.add(customer);
        }
    }

    /**
     * Assigns a payment method to this paying customer.
     *
     * @param paymentMethod the payment method to assign
     * @throws IllegalArgumentException if the payment method is null
     */
    public void setPaymentMethod(PaymentMethod paymentMethod) {

        if (paymentMethod == null) {
            throw new IllegalArgumentException(
                    "Payment method cannot be null.");
        }

        this.paymentMethod = paymentMethod;
    }

    /**
     * Generates a monthly payment email for this paying customer.
     *
     * Includes the main magazine cost, weekly supplement charges, associate
     * customer subscription charges and the total monthly amount.
     *
     * @param magazineWeeklyCost the weekly cost of the main magazine
     * @return the formatted monthly payment email
     */
    public String getMonthlyEmail(double magazineWeeklyCost) {
        String email = "To " + getEmailAddress() + ",\n";
        email += "Dear " + getCustomerName() + ",\n";
        email += "Current payment method: "
                + getPaymentMethod().getPaymentDetails() + "\n";

        email += "Your monthly charges are:\n";

        email += "Main magazine (4 weeks): $"
                + (magazineWeeklyCost * 4) + "\n";

        // Paying customer's weekly supplements
        for (int week = 1; week <= 4; week++) {

            email += "\nWeek " + week + ":\n";

            ArrayList<Supplement> weeklyList
                    = getWeeklySupplements(week);

            if (weeklyList.isEmpty()) {
                email += "No supplements this week.\n";
            } else {
                for (Supplement supplement : weeklyList) {
                    email += supplement.getSupplementName()
                            + ": $"
                            + supplement.getSupplementWeeklyCost()
                            + "\n";
                }
            }
        }

        // Associate customers
        for (Customer customer : associate) {

            email += "\nAssociate "
                    + customer.getCustomerName() + ":\n";

            email += "Main magazine (4 weeks): $"
                    + (magazineWeeklyCost * 4) + "\n";

            for (int week = 1; week <= 4; week++) {

                email += "\nWeek " + week + ":\n";

                ArrayList<Supplement> weeklyList
                        = customer.getWeeklySupplements(week);

                if (weeklyList.isEmpty()) {
                    email += "No supplements this week.\n";
                } else {
                    for (Supplement supplement : weeklyList) {
                        email += supplement.getSupplementName()
                                + ": $"
                                + supplement.getSupplementWeeklyCost()
                                + "\n";
                    }
                }
            }
        }

        email += "\nTotal monthly charge is $"
                + getMonthlyCost(magazineWeeklyCost) + ".\n";

        return email;
    }

    /**
     * Calculates the total monthly subscription cost for this paying customer
     * and all associated customers.
     *
     * Includes the main magazine and supplement charges calculated over four
     * weeks.
     *
     * @param magazineWeeklyCost the weekly cost of the main magazine
     * @return the total monthly subscription cost
     */
    public double getMonthlyCost(double magazineWeeklyCost) {

        double total = magazineWeeklyCost * 4;

        // Paying customer's supplements
        for (Supplement supplement : getSupplements()) {
            total += supplement.getSupplementWeeklyCost() * 4;
        }

        // Associate customers' magazine and supplement costs
        for (Customer customer : associate) {
            total += magazineWeeklyCost * 4;

            for (Supplement supplement : customer.getSupplements()) {
                total += supplement.getSupplementWeeklyCost() * 4;
            }
        }

        return total;

    }

    /**
     * Allows the user to change the current payment method through
     * console-based input.
     *
     * Supports credit card and bank account payment methods. Validates the
     * user's selection and payment information before assigning the new payment
     * method.
     *
     * @param sc the Scanner used to receive user input
     */
    public void changePaymentMethod(Scanner sc) {
        System.out.println("\n================================");
        System.out.println("Paying Customer: " + getCustomerName());
        System.out.println("================================");

        if (paymentMethod != null) {
            System.out.println("Current payment method: "
                    + paymentMethod.getPaymentDetails());
        }

        System.out.print(
                "Would you like to change your payment method? (Y/N): ");

        String answer = sc.nextLine().trim();

        while (!answer.equalsIgnoreCase("Y")
                && !answer.equalsIgnoreCase("N")) {

            System.out.print("Please enter Y or N: ");
            answer = sc.nextLine().trim();
        }

        if (answer.equalsIgnoreCase("N")) {
            System.out.println("Keeping current payment method.");
            return;
        }

        System.out.println("\nChoose a new payment method:");
        System.out.println("1. Credit Card");
        System.out.println("2. Bank Account (Direct Debit)");
        System.out.print("Enter choice: ");

        String choice = sc.nextLine().trim();

        while (!choice.equals("1") && !choice.equals("2")) {
            System.out.print("Invalid choice. Enter 1 or 2: ");
            choice = sc.nextLine().trim();
        }

        PaymentMethod newMethod;

        if (choice.equals("1")) {
            System.out.print("Enter new card number: ");
            String cardNumber = sc.nextLine().trim();

            if (cardNumber.isEmpty()) {
                System.out.println(
                        "Card number cannot be empty. "
                        + "Payment method unchanged.");
                return;
            }

            newMethod = (PaymentMethod) new CreditCard(cardNumber);

        } else {
            System.out.print("Enter new bank account number: ");
            String bankAccount = sc.nextLine().trim();

            if (bankAccount.isEmpty()) {
                System.out.println(
                        "Bank account cannot be empty. "
                        + "Payment method unchanged.");
                return;
            }

            newMethod = (PaymentMethod) new BankAccount(bankAccount);
        }

        setPaymentMethod(newMethod);

        System.out.println("Payment method updated successfully.");
    }

    /**
     * Removes the specified customer from the associate list.
     *
     * @param customer the associate customer to remove
     */
    public void removeAssociate(Customer customer) {
        for (int i = associate.size() - 1; i >= 0; i--) {
            if (associate.get(i) == customer) {
                associate.remove(i);
            }
        }
    }

    /**
     * Generates a formatted billing record for a specific week.
     *
     * Calculates the weekly magazine and supplement charges for the paying
     * customer and their associate customers. Accumulates the weekly totals and
     * includes the monthly total when the fourth week's bill is generated.
     *
     * @param week the billing week number
     * @param magazineName the name of the magazine
     * @param magazineCost the weekly cost of the main magazine
     * @param supplements the supplements subscribed to by the paying customer
     * @return the formatted weekly billing record
     */
    public String generateWeeklyBill(
            int week,
            String magazineName,
            double magazineCost,
            ArrayList<Supplement> supplements) {

        StringBuilder bill = new StringBuilder();
        double weeklyTotal = magazineCost;

        bill.append("WEEK ").append(week).append("\n");
        bill.append("------------------------------------------------------------\n");

        bill.append(String.format(
                "%-38s %14s%n",
                "Subscription Item", "Weekly Cost"));

        bill.append("------------------------------------------------------------\n");

        bill.append(String.format(
                "%-38s $%13.2f%n",
                magazineName,
                magazineCost));

        // Paying customer's supplements for this week.
        if (supplements != null) {

            for (Supplement supplement : supplements) {

                if (supplement == null
                        || !isSubscribedInWeek(supplement, week)) {
                    continue;
                }

                double cost = supplement.getSupplementWeeklyCost();

                bill.append(String.format(
                        "%-38s $%13.2f%n",
                        supplement.getSupplementName(), cost));

                weeklyTotal += cost;
            }
        }

        // Associate customers.
        if (associate != null) {

            for (Customer customer : associate) {

                if (customer == null) {
                    continue;
                }

                bill.append("\nASSOCIATE: ")
                        .append(customer.getCustomerName())
                        .append("\n");

                bill.append(String.format(
                        "%-38s $%13.2f%n",
                        "Main Magazine", magazineCost));

                weeklyTotal += magazineCost;

                if (customer.getSupplements() == null) {
                    continue;
                }

                for (Supplement supplement : customer.getSupplements()) {

                    if (supplement == null
                            || !customer.isSubscribedInWeek(supplement, week)) {
                        continue;
                    }

                    double cost = supplement.getSupplementWeeklyCost();

                    bill.append(String.format(
                            "%-38s $%13.2f%n",
                            supplement.getSupplementName(), cost));

                    weeklyTotal += cost;
                }
            }
        }

        bill.append("------------------------------------------------------------\n");

        bill.append(String.format(
                "%-38s $%13.2f%n",
                "WEEK " + week + " TOTAL", weeklyTotal));

        bill.append("============================================================\n");

        fourWeekTotal += weeklyTotal;

        if (week == 4) {
            bill.append("============================================================\n");

            bill.append(String.format(
                    "%-38s $%13.2f%n", "MONTHLY TOTAL (4 WEEKS)", fourWeekTotal));

            fourWeekTotal = 0.0;
        }

        bill.append("============================================================\n");

        return bill.toString();
    }

    /**
     * Generates and updates the monthly billing history for the specified
     * paying customer.
     *
     * Creates billing records for all four weeks using the magazine and
     * customer subscription information. Replaces the existing billing history
     * after generating the new records.
     *
     * @param customer the paying customer whose billing history will be
     * generated
     * @param magazine the magazine containing subscription and pricing
     * information
     */
    public void generateMonthlyBillingHistory(PayingCustomer customer, Magazine magazine) {

        if (customer == null || magazine == null) {
            return;
        }
        synchronized (customer) {

            synchronized (customer) {

                ArrayList<String> newBillingHistory = new ArrayList<>();

                // Generate all four weeks before modifying existing history.
                for (int week = 1; week <= 4; week++) {

                    String weeklyBill = generateWeeklyBill(
                            week,
                            magazine.getMagazineName(),
                            magazine.getMagazineWeeklyCost(),
                            customer.getSupplements());

                    newBillingHistory.add(weeklyBill);
                }

                // Replace history only after all bills are generated successfully.
                customer.clearBillingHistory();

                for (String bill : newBillingHistory) {
                    customer.addBillingRecord(bill);
                }
            }
        }

    }

    /**
     * Regenerates monthly billing histories for all paying customers registered
     * in the supplied Magazine.
     *
     * @param magazine the magazine containing registered customers and
     * subscription information
     */
    public void regenerateMonthlyBillingHistory(Magazine magazine) {

        if (magazine == null) {
            return;
        }

        for (Customer customer : magazine.getCustomers()) {

            if (customer instanceof PayingCustomer) {

                PayingCustomer payingCustomer = (PayingCustomer) customer;

                generateMonthlyBillingHistory(
                        payingCustomer,
                        magazine
                );
            }
        }
    }

    /**
     * Removes all records from this paying customer's billing history.
     */
    public void clearBillingHistory() {
        billingHistory.clear();
    }

    /**
     * Adds a non-null and non-empty billing record to the customer's billing
     * history.
     *
     * @param bill the billing record to add
     */
    public synchronized void addBillingRecord(String bill) {
        if (bill != null && !bill.trim().isEmpty()) {
            billingHistory.add(bill);
        }
    }

    /**
     * Returns a copy of the customer's billing history.
     *
     * @return a new ArrayList containing the billing records
     */
    public synchronized ArrayList<String> getBillingHistory() {
        return new ArrayList<>(billingHistory);
    }

}
