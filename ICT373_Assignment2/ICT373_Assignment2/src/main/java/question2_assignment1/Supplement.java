/*
 * Title: Magazine Service
 * Author: Nguyen Tuan Anh
 * Date: 15/09/2026
 * File name: Supplement.java
 *
 * Purpose:
 * This class represents an optional supplement available
 * with a magazine subscription. It stores the supplement
 * name and weekly cost, and provides methods to access,
 * update and display these details.
 *
 * Assumptions:
 * 1. The supplement name is provided as a String.
 * 2. The weekly cost is provided as a valid non-negative amount.
 *
 * Expected input:
 * A supplement name and its weekly cost, supplied when
 * a Supplement object is created or updated.
 *
 * Expected output:
 * The supplement name and weekly cost when its details
 * are retrieved or its string representation is displayed.
 */
package question2_assignment1;

import java.io.Serializable;

/**
 * Represents an optional supplement available with a magazine subscription.
 *
 * @author Tuan Anh
 */
public class Supplement implements Serializable {

    private static final long serialVersionUID = 1L;

    private String supplementName;
    private double supplementWeeklyCost;

    /**
     * Constructs a Supplement with the specified name and weekly cost.
     *
     * @param supplementName the name of the supplement
     * @param supplementWeeklyCost the weekly cost of the supplement
     */
    public Supplement(String supplementName, double supplementWeeklyCost) {
        this.supplementName = supplementName;
        this.supplementWeeklyCost = supplementWeeklyCost;
    }

    /**
     * Returns the supplement's name.
     *
     * @return the supplement name
     */
    public String getSupplementName() {
        return supplementName;
    }

    /**
     * Returns the supplement's weekly cost.
     *
     * @return the weekly cost
     */
    public double getSupplementWeeklyCost() {
        return supplementWeeklyCost;
    }

    /**
     * Sets the supplement's name.
     *
     * @param supplementName the new supplement name
     */
    public void setSupplementName(String supplementName) {
        this.supplementName = supplementName;
    }

    /**
     * Sets the supplement's weekly cost.
     *
     * @param supplementWeeklyCost the new weekly cost
     */
    public void setSupplementWeeklyCost(double supplementWeeklyCost) {
        this.supplementWeeklyCost = supplementWeeklyCost;
    }

    /**
     * Returns a string representation of the supplement, including its name and
     * weekly cost.
     *
     * @return the supplement name and weekly cost as a string
     */
    @Override
    public String toString() {
        return supplementName + ": $" + supplementWeeklyCost;
    }
}
