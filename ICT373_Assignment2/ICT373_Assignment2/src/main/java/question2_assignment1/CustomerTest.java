/**
 * Title: Magazine Service
 * Author: Nguyen Tuan Anh
 * Date: 15 September 2026
 * File name: CustomerTest.java
 *
 * Purpose:
 * To verify customer creation, customer classification,
 * supplement selection and payment methods.
 *
 * Assumptions:
 * The user enters valid customer information
 * and selects valid menu options.
 *
 * There are no rules from any specified banks
 * so credit card numbers cannot be limited, as well as direct debit payments from a bank account.
 *
 *
 * Expected input:
 * Customer details, customer type, supplement
 * selection, and payment information.
 *
 * Expected output:
 * Customer information,
 * including payment and associate details.
 */
package question2_assignment1;

import java.util.Scanner;

public class CustomerTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Magazine magazine = MagazineTest.createMagazine(sc);
        Supplement[] supplements = magazine.getSupplements();
        Customer[] customers = constructCustomers(magazine, sc);

        displayCustomers(magazine);

        Customer customer = createCustomer(magazine, sc);
        displayCustomers(magazine);

        removeCustomer(magazine, sc);
        displayCustomers(magazine);
    }

    /// Only accecpt a selection as integer format    
    private static int readChoice(Scanner sc, int maximum) {
        while (true) {
            System.out.println("Enter choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Please enter a number.");
                sc.nextLine();
                continue;
            }
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice >= 1 && choice <= maximum) {
                return choice;
            }
            System.out.println("Choose a number from 1 to " + maximum);

        }

    }

    private static int readAssociateChoice(Scanner sc, Customer[] customers, int payingCustomerNumber) {

        while (true) {
            int choice = readChoice(sc, customers.length + 1);

            if (choice == customers.length + 1) {
                return choice;
            }
            if (choice == payingCustomerNumber) {
                System.out.println("A paying customer cannot be their own associate");

            } else if (customers[choice - 1] instanceof PayingCustomer) {
                System.out.println("A paying customer cannot choose another paying customer as an associate");
            } else {
                return choice;
            }
        }
    }

    /// Create 6 customer and let user is able to type customer's, emailAddress.
    public static Customer[] constructCustomers(Magazine magazine, Scanner sc) {

        Customer[] customers = new Customer[6];

        Supplement[] supplements = magazine.getSupplements();

        int customerCount = 0;

        for (int i = 0; i < customers.length; i++) {
            System.out.println("\n===== Customer " + (i + 1) + " =====");

            System.out.print("Enter customer name: ");
            String name = sc.nextLine();

            System.out.print("Enter email address: ");
            String email = sc.nextLine();

            System.out.println("Choose customer type:");
            System.out.println("1. Normal Customer");
            System.out.println("2. Paying Customer");

            int type = readChoice(sc, 2);

            if (type == 1) {
                customers[i] = new Customer();
            } else {
                customers[i] = new PayingCustomer();
            }

            customers[i].setCustomerName(name);
            customers[i].setEmailAddress(email);

            // Assign at least one supplement
            while (true) {
                System.out.println("\nAvailable supplements:");

                for (int j = 0; j < supplements.length; j++) {
                    System.out.println((j + 1) + ". " + supplements[j]);
                }

                System.out.println("4. No more supplements");
                int supplementChoice = readChoice(sc, 4);

                if (supplementChoice == 4) {
                    break;
                }

                if (customers[i].addSupplement(supplements[supplementChoice - 1])) {
                    System.out.println("Supplement added.");
                } else {
                    System.out.println("This supplement has already been selected.");
                }

            }

            // Additional information for PayingCustomer
            if (customers[i] instanceof PayingCustomer) {
                PayingCustomer payingCustomer = (PayingCustomer) customers[i];

                System.out.println("\nChoose payment method:");
                System.out.println("1. Credit Card");
                System.out.println("2. Bank Account");

                int paymentChoice = readChoice(sc, 2);

                while (true) {
                    try {
                        if (paymentChoice == 1) {
                            System.out.print("Enter a credit card number: ");
                            String cardNumber = sc.nextLine();
                            Long.parseLong(cardNumber);
                            payingCustomer.setPaymentMethod((PaymentMethod) new CreditCard(cardNumber));
                        } else {
                            System.out.println("Enter a bank account number: ");
                            String bankAccount = sc.nextLine();
                            payingCustomer.setPaymentMethod((PaymentMethod) new BankAccount(bankAccount));
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter number only");
                    }
                }
            }
            // Ask a user to continue to create a customer.
            customerCount++;
            if (i < customers.length - 1) {
                System.out.println("Do you want to create another customer? (Y/N): ");
                String answer = sc.nextLine();

                while (!answer.equalsIgnoreCase("Y") && !answer.equalsIgnoreCase("N")) {
                    System.out.println("Please enter Y or N.");
                    answer = sc.nextLine();
                }

                if (answer.equalsIgnoreCase("N")) {
                    break;
                }
            }
        }

        // Assign associates after all 6 customers exist
        for (int i = 0; i < customerCount; i++) {
            if (customers[i] instanceof PayingCustomer) {
                PayingCustomer payingCustomer = (PayingCustomer) customers[i];
                while (true) {
                    System.out.println("\nChoose an associate for " + customers[i].getCustomerName());

                    for (int j = 0; j < customerCount; j++) {
                        if (i != j) {
                            System.out.println((j + 1) + ". " + customers[j].getCustomerName());
                        }
                    }
                    System.out.println((customerCount + 1) + ".no more associates");
                    int associateChoice = readAssociateChoice(sc, customers, i + 1);

                    if (associateChoice == customerCount + 1) {
                        break;
                    }

                    Customer selectedCustomer = customers[associateChoice - 1];

                    boolean alreadyAssociate = false;
                    for (int k = 0; k < customerCount; k++) {
                        if (customers[k] instanceof PayingCustomer) {
                            PayingCustomer otherPayingCustomer = (PayingCustomer) customers[k];
                            if (otherPayingCustomer.getAssociate().contains(selectedCustomer)) {
                                alreadyAssociate = true;
                                break;
                            }
                        }

                    }
                    if (alreadyAssociate) {
                        System.out.println("This customer is already an associate");
                    } else {
                        payingCustomer.addAssociate(customers[associateChoice - 1]);
                        System.out.println(selectedCustomer.getCustomerName() + "added successfully");
                    }
                }

            }
        }

        for (Customer customer : customers) {
            if (customer != null) {
                magazine.addCustomer(customer);
            }
        }

        return customers;
    }

    public static void displayCustomers(Magazine magazine) {

        for (Customer customer : magazine.getCustomers()) {
            System.out.println("Name: " + customer.getCustomerName());
            System.out.println("Email: " + customer.getEmailAddress());
            System.out.println("Supplements: " + customer.getSupplements());

            if (customer instanceof PayingCustomer) {
                PayingCustomer payingCustomer = (PayingCustomer) customer;
                System.out.println("Payment: " + payingCustomer.getPaymentMethod().getPaymentDetails());

                System.out.println("Associates:");

                for (Customer associate : payingCustomer.getAssociate()) {
                    if (associate != null) {
                        System.out.println("- " + associate.getCustomerName());
                    }
                }

                System.out.println();

            }
        }
    }

    //Adding new customer 
    public static Customer createCustomer(Magazine magazine, Scanner sc) {
        Customer customer = new Customer();

        System.out.println("Enter customer name: ");
        customer.setCustomerName(sc.nextLine());

        System.out.println("Enter email address: ");
        customer.setEmailAddress(sc.nextLine());

        Supplement[] supplements = magazine.getSupplements();

        while (true) {

            for (int i = 0; i < supplements.length; i++) {
                System.out.println((i + 1) + ". " + supplements[i]);
            }

            System.out.println("4.No more supplements");
            int choice = readChoice(sc, 4);
            if (choice == 4) {
                break;
            }
            if (customer.addSupplement(supplements[choice - 1])) {
                System.out.println("Supplement added.");
            } else {
                System.out.println("This supplement has already been selected.");
            }

        }
        magazine.addCustomer(customer);
        System.out.println("New customer added successfully.");

        return customer;

    }

    /// Removing an existing customer 
public static void removeCustomer(Magazine magazine, Scanner sc) {
        System.out.print("Enter customer name to remove: ");
        String name = sc.nextLine();

        Customer customerToRemove = null;

        for (Customer customer : magazine.getCustomers()) {
            if (customer.getCustomerName().equalsIgnoreCase(name)) {
                customerToRemove = customer;
                break;
            }
        }

        if (customerToRemove != null) {
            magazine.removeCustomer(customerToRemove);
            System.out.println("Customer removed successfully.");
        } else {
            System.out.println("Customer not found.");
        }
    }


///Removing an exisit customer 
//    public static void removeCustomer(Magazine magazine, Scanner sc) {
//        System.out.print("Enter customer name to remove: ");
//        String name = sc.nextLine();
//
//        Customer customerToRemove = null;
//
//        for (Customer customer : magazine.getCustomers()) {
//            if (customer.getCustomerName().equalsIgnoreCase(name)) {
//                customerToRemove = customer;
//                break;
//            }
//        }
//
//        if (customerToRemove != null) {
//            if (customerToRemove instanceof PayingCustomer) {
//
//                PayingCustomer payingCustomer
//                        = (PayingCustomer) customerToRemove;
//
//                for (Customer associate : payingCustomer.getAssociate()) {
//
//                    if (associate != null) {
//                        magazine.removeCustomer(associate);
//                    }
//                }
//            }
//
//            magazine.removeCustomer(customerToRemove);
//            System.out.println("Customer removed successfully.");
//
//        } else {
//            System.out.println("Customer not found.");
//        }
//    }
    
}
