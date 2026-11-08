
/*
 * Title: Magazine Service Application
 * Author: Nguyen Tuan Anh
 * Date: 04 November 2026
 * File Name: Magazine.java
 *
 * Purpose:
 * This class manages magazine details, registered customers,
 * available supplements and customer email messages.
 *
 * Assumptions and Input Conditions:
 * - Magazine details, customers and supplements are provided
 *   through constructors or application methods.
 * - The magazine uses a four-week subscription period.
 * - Customers can subscribe to available supplements.
 * - Weekly subscription information is managed for each customer.
 *
 * Expected Output:
 * - Stores magazine name and weekly cost.
 * - Manages registered customers and available supplements.
 * - Generates weekly email messages.
 * - Generates end-of-month emails for paying customers.
 * - Manages weekly supplement subscriptions and payment methods.
 */
package question2_assignment1;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
import java.io.Serializable;

/**
 * Represents a magazine subscription service.
 *
 * Manages magazine information, registered customers, available supplements,
 * subscription operations and customer email messages.
 *
 * @author Nguyen Tuan Anh
 */
public class Magazine implements Serializable {

    private static final long serialVersionUID = 1L;

    private double magazineWeeklyCost;
    private ArrayList<Customer> customers;
    private Supplement[] supplements;
    private String magazineName;

    /**
     * Constructs a Magazine with default values.
     *
     * Initializes the weekly cost to 0.0, the magazine name to an empty string
     * and the customer and supplement collections as empty.
     */
    public Magazine() {

        this.magazineWeeklyCost = 0.0;
        this.customers = new ArrayList<>();
        this.supplements = new Supplement[0];
        this.magazineName = "";
    }

    /**
     * Constructs a Magazine with the specified weekly cost, available
     * supplements and magazine name.
     *
     * Initializes the customer list as empty.
     *
     * @param magazineWeeklyCost the weekly cost of the magazine
     * @param supplements the supplements available with the magazine
     * @param magazineName the name of the magazine
     */
    public Magazine(double magazineWeeklyCost, Supplement[] supplements, String magazineName) {
        this.magazineWeeklyCost = magazineWeeklyCost;
        this.customers = new ArrayList<>();
        this.supplements = supplements;
        this.magazineName = magazineName;
    }

    /**
     * Returns the list of registered customers.
     *
     * @return the list of customers registered in the magazine
     */
    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    /**
     * Updates the magazine's weekly cost.
     *
     * @param magazineWeeklyCost the new weekly cost
     */
    public void setMagazineWeeklyCost(double magazineWeeklyCost) {
        this.magazineWeeklyCost = magazineWeeklyCost;
    }

    /**
     * Returns the magazine's weekly cost.
     *
     * @return the current weekly magazine cost
     */
    public double getMagazineWeeklyCost() {
        return magazineWeeklyCost;
    }

    /**
     * Adds a customer to the magazine service.
     *
     * @param c the customer to register
     */
    public void addCustomer(Customer c) {
        customers.add(c);
    }

    /**
     * Removes a customer from the magazine service.
     *
     * @param c the customer to remove
     */
    public void removeCustomer(Customer c) {
        customers.remove(c);
    }

    /**
     * Returns the array of available supplements.
     *
     * @return the array of available supplements
     */
    public Supplement[] getSupplements() {
        return supplements;
    }

    /**
     * Returns the magazine's name.
     *
     * @return the current magazine name
     */
    public String getMagazineName() {
        return magazineName;
    }

    /**
     * Registers each customer in the supplied customer array.
     *
     * Each customer is added to the magazine service using the addCustomer
     * method.
     *
     * @param customers the array of customers to register
     */
    public void registerCustomers(Customer[] customers) {
        for (Customer customer : customers) {
            addCustomer(customer);
        }
    }

    /**
     * Adds a new supplement to the magazine.
     *
     * Checks that the supplied supplement is not null and that another
     * supplement with the same name does not already exist.
     *
     * @param supplement the supplement to add
     * @throws IllegalArgumentException if the supplement is null or a
     * supplement with the same name already exists
     */
    public void addSupplement(Supplement supplement) {

        if (supplement == null) {
            throw new IllegalArgumentException(
                    "Supplement cannot be null."
            );
        }

        for (Supplement existing : supplements) {

            if (existing.getSupplementName()
                    .equalsIgnoreCase(
                            supplement.getSupplementName()
                    )) {

                throw new IllegalArgumentException(
                        "A supplement with this name already exists."
                );
            }
        }

        supplements = Arrays.copyOf(
                supplements,
                supplements.length + 1
        );

        supplements[supplements.length - 1] = supplement;
    }

    /**
     * Removes a supplement from the magazine.
     *
     * Updates the supplement array and removes the deleted supplement from all
     * registered customer subscriptions.
     *
     * @param supplement the supplement to remove
     */
    public void removeSupplement(Supplement supplement) {

        if (supplement == null) {
            return;
        }

        int index = -1;

        for (int i = 0; i < supplements.length; i++) {

            if (supplements[i] == supplement) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return;
        }

        Supplement[] updated
                = new Supplement[supplements.length - 1];

        for (int i = 0, j = 0; i < supplements.length; i++) {

            if (i != index) {
                updated[j++] = supplements[i];
            }
        }

        supplements = updated;

        // Remove deleted supplement from all customer subscriptions.
        for (Customer customer : customers) {
            customer.removeSupplement(supplement);
        }
    }

    /**
     * Prints weekly email messages for all registered customers for weeks 1
     * through 4.
     *
     * Each email includes customer contact information, a greeting and the
     * supplements subscribed to for the corresponding week.
     */
    public void printAllWeeklyEmails() {

        for (int week = 1; week <= 4; week++) {

            System.out.println("\n========== WEEK " + week + " ==========");

            for (Customer customer : customers) {

                System.out.println("\nTo: "
                        + customer.getEmailAddress());
                System.out.println("Dear "
                        + customer.getCustomerName() + ",");
                System.out.println("Your magazine for Week " + week + " is ready to look at.");

                ArrayList<Supplement> weeklyList
                        = customer.getWeeklySupplements(week);

                if (weeklyList.isEmpty()) {

                    System.out.println("No supplements this week.");
                } else {

                    System.out.println("Your supplements this week:");

                    for (Supplement supplement : weeklyList) {
                        System.out.println("- " + supplement.getSupplementName());
                    }
                }
            }
        }
    }

    /**
     * Prints the end-of-month email for each registered PayingCustomer.
     *
     * Customers who are not instances of PayingCustomer are skipped.
     */
    public void printEndOfMonthEmails() {
        for (Customer customer : customers) {
            if (customer instanceof PayingCustomer) {
                PayingCustomer payingCustomer = (PayingCustomer) customer;
                System.out.println(payingCustomer.getMonthlyEmail(magazineWeeklyCost));
            }
        }
    }

    /**
     * Collects weekly subscription information for all registered customers.
     *
     * Prompts the user to confirm whether each customer continues subscribing
     * to their supplements for each of the four weeks.
     *
     * @param sc the Scanner used to receive user input
     */
    public void collectWeeklySubscriptions(Scanner sc) {

        for (Customer customer : customers) {

            for (int week = 1; week <= 4; week++) {

                System.out.println("\nCustomer: "
                        + customer.getCustomerName());
                System.out.println("Week " + week);

                System.out.print(
                        "Does this customer still subscribe to their "
                        + "supplements this week? (Y/N): ");

                String choice = sc.nextLine().trim();

                while (!choice.equalsIgnoreCase("Y")
                        && !choice.equalsIgnoreCase("N")) {

                    System.out.print("Please enter Y or N: ");
                    choice = sc.nextLine().trim();
                }

                ArrayList<Supplement> weeklyList = customer.getWeeklySupplements(week);

                if (choice.equalsIgnoreCase("Y")) {
                    weeklyList.addAll(customer.getSupplements());
                }

                customer.setWeeklySupplements(week, weeklyList);
            }
        }
    }

    /**
     * Allows users to change the payment methods of all registered paying
     * customers.
     *
     * Iterates through the registered customers and invokes the payment method
     * modification process for each PayingCustomer.
     *
     * @param sc the Scanner used to receive user input
     */
    public void changePayingCustomersPaymentMethods(Scanner sc) {

        for (Customer customer : getCustomers()) {

            if (customer instanceof PayingCustomer) {

                PayingCustomer payingCustomer
                        = (PayingCustomer) customer;

                payingCustomer.changePaymentMethod(sc);
            }
        }
    }

}
