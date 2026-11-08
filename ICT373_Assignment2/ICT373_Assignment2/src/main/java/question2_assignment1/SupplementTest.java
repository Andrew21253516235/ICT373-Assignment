/*
 * Title: Magazine Service
 * Author: Nguyen Tuan Anh
 * Date: 15/09/2026
 * File name: SupplementTest.java
 *
 * Purpose:
 * This class tests the Supplement class by creating a supplement,
 * retrieving its name and weekly cost, and updating these values.
 *
 * Assumptions:
 * 1. The Supplement object is created with a valid name and weekly cost.
 * 2. The getter and setter methods work as expected.
 *
 * Expected input:
 * The test uses the supplement name "Protein" and a weekly cost of $10.00.
 * It then changes the name to "Vitamin" and the weekly cost to $15.00.
 *
 * Expected output:
 * The program displays the supplement's original name and weekly cost,
 * followed by the updated name and weekly cost.
 */
package question2_assignment1;

/**
 *
 * @author Tuan Anh
 */
public class SupplementTest {

    public static void main(String[] args) {

        // Create a supplement
        Supplement supplement
                = new Supplement("Protein", 10.00);

        // Test getSupplementName()
        System.out.println("Supplement Name: "
                + supplement.getSupplementName());

        // Test getSupplementWeeklyCost()
        System.out.println("Weekly Cost: $" + supplement.getSupplementWeeklyCost());

        // Test setSupplementName()
        supplement.setSupplementName("Vitamin");

        System.out.println("\nAfter changing the name:");
        System.out.println("Supplement Name: " + supplement.getSupplementName());

        // Test setSupplementWeeklyCost()
        supplement.setSupplementWeeklyCost(15.00);

        System.out.println("\nAfter changing the weekly cost:");
        System.out.println("Weekly Cost: $" + supplement.getSupplementWeeklyCost());
    }
}
