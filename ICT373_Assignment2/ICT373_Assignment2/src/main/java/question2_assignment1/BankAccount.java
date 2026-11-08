/*
 * Title: Magazine Service
 * Author: Nguyen Tuan Anh
 * Date: 15 September 2026
 * File name: BankAccount.java
 *
 * Purpose:
 * To verify a direct debit payment method and
 * test its payment details and payment processing.
 *
 * Assumptions:
 * The bank account number and payment amount are valid.
 *
 * Expected input:
 * The bank account number and payment amount provided
 * by the test code.
 *
 * Expected output:
 * Direct debit payment details and payment confirmation.
 */
package question2_assignment1;

import java.io.Serializable;

/**
 * Represents a direct debit payment method.
 *
 * @author Tuan Anh
 */
public class BankAccount implements Serializable, PaymentMethod {

    private static final long serialVersionUID = 1L;
    private String bankAccount;

    /**
     * Constructs a BankAccount object.
     *
     * @param bankAccount the bank account number used for direct debit.
     */
    public BankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    /**
     * Returns the payment details of the direct debit method.
     *
     * @return a string containing the direct debit payment details.
     */
    @Override
    public String getPaymentDetails() {
        return "Bank Account : " + bankAccount;
    }

    /**
     * Processes a direct debit payment for the specified amount.
     *
     * @param amount the amount to be charged to the bank account.
     */
    @Override
    public void processPayment(double amount) {
        System.out.println("Direct Debit charged: $ " + amount);
    }
}
