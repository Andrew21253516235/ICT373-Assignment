/*
 * Title: Magazine Service
 * Author: Nguyen Tuan Anh
 * Date: 15 September 2026
 * File name: BankAccountTest.java
 *
 * Purpose:
 * To verify the BankAccount class by displaying its
 * payment details and processing a payment.
 *
 * Assumptions:
 * The bank account number and payment amount are valid.
 *
 * Expected input:
 * The bank account number and payment amount provided
 * in the test code.
 *
 * Expected output:
 * Direct debit payment details and payment confirmation.
 */
package question2_assignment1;

public class BankAccountTest {

    public static void main(String[] args) {

        // Create a Direct Debit account
        BankAccount bankAccount = new BankAccount("123456789");

        // Test payment details
        System.out.println(bankAccount.getPaymentDetails());

        // Test payment
        bankAccount.processPayment(50.00);
    }

}
