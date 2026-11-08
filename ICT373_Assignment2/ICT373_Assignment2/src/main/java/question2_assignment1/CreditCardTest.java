/*
 * Title: Magazine Service
 * Author: Nguyen Tuan Anh
 * Date: 15 September 2026
 * File name: CreditCardTest.java
 *
 * Purpose:
 * To verify the CreditCard class by displaying
 * payment details and processing a payment.
 *
 * Assumptions:
 * The credit card number and payment amount
 * are valid.
 *
 * Expected input:
 * The credit card number and payment amount
 * provided in the test code.
 *
 * Expected output:
 * Credit card payment details and payment confirmation.
 */
package question2_assignment1;

/**
 *
 * @author Tuan Anh
 */
public class CreditCardTest {

    public static void main(String[] args) {

        CreditCard card = new CreditCard("123456789");

        // Test getPaymentDetails()
        System.out.println(card.getPaymentDetails());

        // Test processPayment()
        card.processPayment(50.00);
    }
}
