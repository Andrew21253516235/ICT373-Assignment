
/*
 * Title: Magazine Service Application
 * Author: Nguyen Tuan Anh
 * Date: 04 November 2026
 * File Name: Address.java
 *
 * Purpose:
 * This class represents a customer's residential address.
 * It stores the street number, street name, suburb and postcode.
 *
 * Assumptions and Input Conditions:
 * - Address information is provided through constructors
 *   or setter methods.
 * - Address validation is handled by the application interface.
 * - The object supports Java serialization.
 *
 * Expected Output:
 * - Stores and provides access to address information.
 * - Allows address details to be updated.
 * - Returns a formatted address representation.
 */
package question2_assignment1;

import java.io.Serializable;

/**
 * Represents a customer's residential address.
 *
 * Stores the street number, street name, suburb and postcode. Implements
 * Serializable to support persistent storage.
 *
 * @author Nguyen Tuan Anh
 */
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    private String streetNumber;
    private String streetName;
    private String suburb;
    private String postCode;

    /**
     * Creates an Address object with empty address fields.
     */
    public Address() {
        streetNumber = "";
        streetName = "";
        suburb = "";
        postCode = "";

    }

    /**
     * Creates an Address object using the supplied address details.
     *
     * @param streetNumber the street number
     * @param streetName the street name
     * @param suburb the suburb
     * @param postCode the postcode
     */
    public Address(String streetNumber, String streetName, String suburb, String postCode) {
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.suburb = suburb;
        this.postCode = postCode;
    }

    /**
     * Updates the street number.
     *
     * @param streetNumber the new street number
     */
    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    /**
     * Returns the street number.
     *
     * @return the current street number
     */
    public String getStreetNumber() {
        return streetNumber;
    }

    /**
     * Updates the street name.
     *
     * @param streetName the new street name
     */
    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    /**
     * Returns the street name.
     *
     * @return the current street name
     */
    public String getStreetName() {
        return streetName;
    }

    /**
     * Updates the suburb.
     *
     * @param suburb the new suburb
     */
    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    /**
     * Returns the suburb.
     *
     * @return the current suburb
     */
    public String getSuburb() {
        return suburb;
    }

    /**
     * Returns the postcode.
     *
     * @return the current postcode
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * Updates the postcode.
     *
     * @param postCode the new postcode
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    /**
     * Returns the address as a formatted string.
     *
     * @return the formatted address containing the street number, street name,
     * suburb and postcode
     */
    @Override
    public String toString() {
        return streetNumber + " " + streetName + ", " + suburb + " ," + postCode;
    }

}
