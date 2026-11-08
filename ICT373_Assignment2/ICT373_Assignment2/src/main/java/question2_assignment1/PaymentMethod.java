/*
 * Title: Magazine Service
 * Author: Nguyen Tuan Anh
 * Date: 15 September 2026
 * File name: PaymentMethod.java
 *
 * Purpose:
 * To define the interface required for payment methods
 * used by the magazine subscription service.
 *
 * Assumptions:
 * Implementing classes provide valid payment details
 * and handle the specified payment amount.
 *
 * Expected input:
 * Payment details and a payment amount are supplied
 * by the implementing classes.
 *
 * Expected output:
 * Payment details and payment processing results,
 * as implemented by each payment method.
 */
package question2_assignment1;

import java.io.Serializable;

/**
 * Defines the methods that a payment method must provide.
 *
 * @author Tuan Anh
 */
public interface PaymentMethod extends Serializable {

    /**
     * Returns the details of the payment method.
     *
     * @return a string containing the payment details
     */
    public String getPaymentDetails();

    /**
     * Processes a payment for the specified amount.
     *
     * @param amount the amount to be processed
     */
    public void processPayment(double amount);

}
