/*
 * Title: Magazine Service
 * Author: Nguyen Tuan Anh
 * Date: 15 September 2026
 * File name: Customer.java
 *
 * Purpose:
 * To verify a registered customer with the
 * magazine subscription service.
 *
 * Assumptions:
 * Customer information is provided as valid input.
 * A customer may have multiple supplements.
 *
 * Expected input:
 * Customer name, email address and supplement
 * information.
 *
 * Expected output:
 * Customer information and associated supplements.
 */
package question2_assignment1;

import java.lang.String;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a registered customer with the magazine subscription service..
 *
 * @author Anh Nguyen
 */
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    private String customerName;
    private String emailAddress;
    private ArrayList<Supplement> supplements;
    private Customer[] customers;
    private ArrayList<ArrayList<Supplement>> weeklySupplements;
    private Address address;
    private Map<String, ArrayList<Integer>> supplementSubscriptionWeeks = new LinkedHashMap<>();

    /*
    
     */
    Customer() {
        customerName = "";
        emailAddress = "";
        supplements = new ArrayList<>();
        weeklySupplements = new ArrayList<ArrayList<Supplement>>();
        address = new Address();

    }

    /**
     * Constructs Customer object with the specified information.
     *
     * @param customerName the customer's name.
     * @param emailAddress the customer's email address.
     * @param supplements the customer's selected supplements.
     * @param customers the array of customers.
     */
    public Customer(String customerName, String emailAddress, ArrayList<Supplement> supplements, Customer[] customers) {
        this.customerName = customerName;
        this.emailAddress = emailAddress;
        this.supplements = new ArrayList<>(supplements);
        this.customers = customers;
        // Your existing regular supplement list
        supplements = new ArrayList<Supplement>();

        // Initialise the weekly subscription lists
        weeklySupplements = new ArrayList<ArrayList<Supplement>>();

        // Four weeks
        for (int i = 0; i < 4; i++) {
            weeklySupplements.add(new ArrayList<Supplement>());
        }
    }

    /**
     * Return the customer's name
     *
     * @return the customer's name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Return the customer's email address.
     *
     * @return the customer's email address
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Return the list of supplements associated with the customer.
     *
     * @return the customer's supplement list
     */
    public ArrayList<Supplement> getSupplements() {
        return supplements;
    }

    /**
     * Set the customer's name
     *
     * @param name the new customer name
     */
    public void setCustomerName(String name) {
        this.customerName = name;
    }

    /**
     * Set the email address's customer
     *
     * @param email the new customer's email address
     */
    public void setEmailAddress(String email) {
        this.emailAddress = email;
    }

    /**
     * Returns the array of customer
     *
     * @return the customer array
     */
    public Customer[] getCustomers() {
        return customers;
    }

    /**
     * Set the customer's supplement list.
     *
     * @param supplements the new list of supplements
     */
    public void setSupplements(ArrayList<Supplement> supplements) {
        this.supplements = new ArrayList<>();

        if (supplements == null) {
            return;
        }

        for (Supplement supplement : supplements) {
            if (supplement != null) {
                this.supplements.add(supplement);
            }
        }
    }

    /**
     * Adds a supplement to the customer's subscription.
     *
     * @param supplement the supplement to add
     * @return true if the supplement is successfully added; otherwise false
     */
    public boolean addSupplement(Supplement supplement) {
        for (Supplement s : supplements) {
            if (s == supplement) {
                return false;
            }
        }

        supplements.add(supplement);
        return true;
    }

    /**
     * Removes a supplement from the customer's supplement list.
     *
     * @param s the supplement to remove
     */
    public void removeSupplement(Supplement s) {
        supplements.remove(s);
    }

    /**
     * Returns a string representation of the customer.
     *
     * @return a string containing the customer's name
     */
    @Override
    public String toString() {
        return "Customer Name: " + customerName;
    }

    public void setWeeklySupplements(
            int week, ArrayList<Supplement> selectedSupplements) {

        if (week < 1 || week > 4) {
            throw new IllegalArgumentException("Week must be between 1 and 4.");
        }

        weeklySupplements.set(
                week - 1, new ArrayList<Supplement>(selectedSupplements));
    }

    ArrayList<Supplement> getWeeklySupplements(int week) {
        if (week < 1 || week > 4) {
            throw new IllegalArgumentException(
                    "Week must be between 1 and 4.");
        }
        // Initialise the outer list if necessary
        if (weeklySupplements == null) {
            weeklySupplements
                    = new ArrayList<ArrayList<Supplement>>();
        }

        // Ensure all four weeks exist
        while (weeklySupplements.size() < 4) {
            weeklySupplements.add(new ArrayList<Supplement>());
        }

        return weeklySupplements.get(week - 1);
    }

    /**
     *
     * @return
     */
    public Address getAddress() {
        return address;
    }

    /**
     *
     * @param address
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    public Map<String, ArrayList<Integer>> getSupplementSubscriptionWeeks() {

        if (supplementSubscriptionWeeks == null) {
            supplementSubscriptionWeeks = new LinkedHashMap<>();
        }

        return supplementSubscriptionWeeks;
    }

    public void setSupplementSubscriptionWeeks(Map<String, ArrayList<Integer>> weeks) {

        supplementSubscriptionWeeks = new LinkedHashMap<>(weeks);
    }

    public boolean isSubscribedInWeek(
            Supplement supplement, int week) {

        ArrayList<Integer> weeks
                = getSupplementSubscriptionWeeks().get(
                        supplement.getSupplementName());

        // If no schedule exists, keep the original behaviour.
        return weeks == null || weeks.contains(week);
    }

}
