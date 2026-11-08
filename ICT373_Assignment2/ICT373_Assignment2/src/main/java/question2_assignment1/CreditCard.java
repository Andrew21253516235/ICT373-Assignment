/*
 * Title: Magazine Service
 * Author: Nguyen Tuan Anh
 * Date: 15 September 2026
 * File name: CreditCard.java
 *
 * Purpose:
 * To verify a credit card payment method and
 * provide methods to process payments.
 *
 * Assumptions:
 * The card number is provided as a non-empty String.
 * The payment amount is non-negative.
 *
 * Expected input:
 * The credit card number and payment amount.
 *
 * Expected output:
 * Credit card payment details and payment
 * confirmation displayed on the console.
 */
package question2_assignment1;

import java.io.Serializable;
import java.lang.String;

/**
 * Represents a credit card payment method.
 *
 * @author Anh Nguyen
 */
public class CreditCard implements Serializable, PaymentMethod {

    private static final long serialVersionUID = 1L;
    private String cardNumber;

    /**
     * Constructs a CreditCard object with the specified card number
     *
     * @param cardNumber
     */
    public CreditCard(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Returns the payment details of the credit card
     *
     * @return the credit card payment details, including the card number
     */
    @Override
    public String getPaymentDetails() {
        return "Credit Card:  " + cardNumber;
    }

    /**
     * Processes a credit card payment for the specified amount
     *
     * @param amount the amount is charged to the credit card
     */
    @Override
    public void processPayment(double amount) {
        System.out.println("Credit card Charged: $" + amount);
    }

}
