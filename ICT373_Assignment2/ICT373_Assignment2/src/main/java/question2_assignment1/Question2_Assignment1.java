/**
 * Title: Magazine Service
 * Author: Nguyen Tuan Anh
 * Date: 15/09/2026
 *
 * Purpose:
 * This program is designed to manage weekly magazine subscriptions and
 * customer information along with their selected supplements.
 *
 * Assumptions:
 * 1. The program will initially construct 6 customers and
 * classify them as either a normal customer or a paying customer with assigned
 * an associate customer. Each paying customer will be assigned an associate
 * customer who will be chosen from normal customer.
 * 2. The program will display all customers after adding a new customer.
 * 3. The program will illustrate all customers after removing an existing customer.
 *
 * Expected output:
 * The program displays student details, the menu and the
 * results of the selected magazine and customer operations.
 */
package question2_assignment1;

import java.util.Scanner;

public class Question2_Assignment1 {

    public static void displayStudentDetails() {
        System.out.println("Name: Nguyen Tuan Anh");
        System.out.println("StudentNumber: 35542093");
        System.out.println("ModeOfEnrolment: ICT373");
        System.out.println("TutorName: Dr.Poo Lok Kok");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        displayStudentDetails();
        Magazine magazine = null;
        String choice;

        // Display menu
        do {
            System.out.println("=====Menu=========");
            System.out.println("a. Create Magazine.");
            System.out.println("b. Create Customer. ");
            System.out.println("c. Print out the text of all the emails for all customers for four weeks.");
            System.out.println("d. Print out the text for the end of month emails for the paying customer.");
            System.out.println("e. Add a new customer.");
            System.out.println("f. Remove an existing customer.");
            System.out.println("g. Quit.");
            System.out.println("Press 'g' to exit the menu.");
            System.out.print("Please enter your choice: ");
            choice = sc.nextLine().trim().toLowerCase();

            // User need to create magazine first at option a 
            if (magazine == null && choice.matches("[b-f]")) {
                System.out.println("Please create a magazine first.");
                continue;
            }

            switch (choice) {
                case "a":
                    magazine = MagazineTest.createMagazine(sc);
                    break;
                case "b":
                    CustomerTest.constructCustomers(magazine, sc);
                    CustomerTest.displayCustomers(magazine);

                    break;
                case "c":
                    magazine.collectWeeklySubscriptions(sc);
                    magazine.printAllWeeklyEmails();

                    break;

                case "d":
                    magazine.changePayingCustomersPaymentMethods(sc);
                    magazine.printEndOfMonthEmails();
                    break;

                case "e":
                    CustomerTest.createCustomer(magazine, sc);
                    CustomerTest.displayCustomers(magazine);

                    break;
                case "f":
                    CustomerTest.removeCustomer(magazine, sc);
                    CustomerTest.displayCustomers(magazine);

                    break;
                case "g":
                    System.out.println("Bye bye");
                    break;
                default:
                    System.out.println("Invalid choice");
            }

        } while (!choice.equals("g"));
    }

}
