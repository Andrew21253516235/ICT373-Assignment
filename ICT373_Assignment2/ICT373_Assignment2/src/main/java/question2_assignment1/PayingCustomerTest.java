/*
 * Title: Magazine Service
 * Author: Nguyen Tuan Anh
 * Date: 15 September 2026
 * File name: PayingCustomerTest.java
 *
 * Purpose:
 * To test the PayingCustomer class by setting customer
 * details, adding supplements, calculating the monthly
 * cost and displaying the monthly email.
 *
 * Assumptions:
 * The customer details and supplement costs are valid.
 *
 * Expected input:
 * Customer details and supplement information are
 * provided directly in the test code.
 *
 * Expected output:
 * The paying customer's monthly subscription cost
 * and monthly email message.
 */
package question2_assignment1;

public class PayingCustomerTest {

    public static void main(String[] args) {

        // Create a paying customer
        PayingCustomer customer = new PayingCustomer();

        // Set customer information
        customer.setCustomerName("John");
        customer.setEmailAddress("john@gmail.com");

        System.out.println("Name:" + customer.getCustomerName());
        System.out.println("emailAddress:" + customer.getEmailAddress());

        // Create supplements
        Supplement supplement1 = new Supplement("Protein", 10.0);
        Supplement supplement2 = new Supplement("Vitamin", 5.0);

        // Add supplements to customer
        customer.addSupplement(supplement1);
        customer.addSupplement(supplement2);
        System.out.println("Supplement: ");
        System.out.println(supplement1.getSupplementName() + "and" + supplement2.getSupplementName());

        double magazineWeeklyCost = 3.50;

        // Test monthly cost, including the main magazine cost.
        System.out.println("Monthly Cost: $"
                + customer.getMonthlyCost(magazineWeeklyCost));

        // Test monthly email
        System.out.println("\nMonthly Email:");
        System.out.println(customer.getMonthlyEmail(magazineWeeklyCost));
    }

}
