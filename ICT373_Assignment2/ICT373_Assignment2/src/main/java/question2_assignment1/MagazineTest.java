/*
 * Title: Magazine Service
 * Author: Nguyen Tuan Anh
 * Date: 15 September 2026
 * File name: MagazineTest.java
 *
 * Purpose:
 * To create a magazine, display its details and
 * test the creation of its available supplements.
 *
 * Assumptions:
 * The user enters a valid magazine name and weekly cost.
 *
 * Expected input:
 * Magazine name and weekly cost are entered by the user.
 *
 * Expected output:
 * The magazine name, weekly cost and available
 * supplements with their weekly costs.
 */
package question2_assignment1;

import java.util.Scanner;

public class MagazineTest {

    // TEST PROGRAM
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Magazine magazine = createMagazine(scanner);

    }

    public static Magazine constructMagazine(String magazineName, double magazineWeeklyCost) {

        Supplement[] supplements = new Supplement[3];

        supplements[0] = new Supplement("Technology Weekly", 5.99);

        supplements[1] = new Supplement("Sports Monthly", 4.99);

        supplements[2] = new Supplement("Travel Guide", 6.99);

        Magazine magazine = new Magazine(magazineWeeklyCost, supplements, magazineName);
        return magazine;

    }

    private static double readWeeklyCost(Scanner scanner) {
        while (true) {
            System.out.print("Enter weekly cost: ");

            if (!scanner.hasNextDouble()) {
                System.out.println("Weekly cost must be a number.");
                scanner.nextLine();
                continue;
            }

            double cost = scanner.nextDouble();
            scanner.nextLine();

            if (cost >= 0) {
                return cost;
            }

            System.out.println("Weekly cost cannot be negative.");
        }
    }

    public static Magazine createMagazine(Scanner scanner) {

        System.out.print("Enter magazine name: ");
        String magazineName = scanner.nextLine();

        double magazineWeeklyCost = readWeeklyCost(scanner);

        Magazine magazine = constructMagazine(magazineName, magazineWeeklyCost);

        System.out.println("Magazine: " + magazine.getMagazineName());
        System.out.println("Weekly cost: $" + magazine.getMagazineWeeklyCost());

        System.out.println("\nSupplements:");

        for (Supplement supplement : magazine.getSupplements()) {
            System.out.println(
                    supplement.getSupplementName()
                    + ": $"
                    + supplement.getSupplementWeeklyCost()
            );
        }

        return magazine;

    }

}
