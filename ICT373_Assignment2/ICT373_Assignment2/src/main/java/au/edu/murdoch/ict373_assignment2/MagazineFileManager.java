
/*
 * Title: Magazine Service Application
 * Author: Nguyen Tuan Anh
 * Date: 04 November 2026
 * File Name: MagazineFileManager.java
 *
 * Purpose:
 * This class manages persistent storage of Magazine objects.
 * It uses Java object serialization and deserialization
 * to save and load magazine information from a binary file.
 *
 * Assumptions and Input Conditions:
 * - Magazine-related objects must support serialization.
 * - The application uses the data/magazine.dat file.
 * - The save file must contain a valid Magazine object.
 *
 * Expected Output:
 * - Saves magazine information to a binary file.
 * - Loads existing magazine information.
 * - Removes invalid subscription references after loading.
 * - Checks whether saved data exists.
 * - Deletes the saved data file when requested.
 */
package au.edu.murdoch.ict373_assignment2;

import question2_assignment1.Magazine;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import question2_assignment1.Customer;
import question2_assignment1.PayingCustomer;
import question2_assignment1.Supplement;
import java.util.ArrayList;

/**
 * Manages persistent storage of Magazine objects through Java object
 * serialization and deserialization.
 *
 * Provides methods for saving, loading, checking and deleting saved magazine
 * data.
 *
 * @author Nguyen Tuan Anh
 */
public class MagazineFileManager {

    private final Path filePath = Path.of("data", "magazine.dat");

    /**
     * Serializes and saves the supplied Magazine object to the configured
     * binary file.
     *
     * Creates the required parent directory if it does not exist.
     *
     * @param magazine the Magazine object to save
     * @throws IOException if an input/output error occurs while saving the
     * magazine
     */
    public void save(Magazine magazine) throws IOException {
        Files.createDirectories(filePath.getParent());

        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            out.writeObject(magazine);
        }
    }

    /**
     * Loads a Magazine object from the binary file.
     *
     * Checks the serialized object's type, cleans invalid customer subscription
     * information and saves the cleaned magazine data.
     *
     * @return the loaded Magazine object, or null if no save file exists
     * @throws IOException if the file cannot be read or does not contain a
     * valid Magazine object
     * @throws ClassNotFoundException if a serialized class cannot be found
     */
    public Magazine load()
            throws IOException, ClassNotFoundException {

        if (!Files.exists(filePath)) {
            return null;
        }

        Magazine magazine;

        try (ObjectInputStream in
                = new ObjectInputStream(Files.newInputStream(filePath))) {

            Object object = in.readObject();

            if (!(object instanceof Magazine)) {
                throw new IOException(
                        "Save file does not contain a Magazine.");
            }

            magazine = (Magazine) object;
        }

        // Clean invalid subscription data after loading.
        cleanCustomerSubscriptions(magazine);

        // Persist the cleaned data.
        save(magazine);

        return magazine;
    }

    /**
     * Checks whether the configured save file exists and is a regular file.
     *
     * @return true if the save file exists and is a regular file; otherwise
     * false
     */
    public boolean hasSavedData() {
        return Files.exists(filePath) && Files.isRegularFile(filePath);
    }

    /**
     * Deletes the saved magazine file if it exists.
     *
     * @throws IOException if an input/output error occurs while deleting the
     * file
     */
    public void deleteSavedData() throws IOException {
        Files.deleteIfExists(filePath);
    }

    /**
     * Removes invalid supplement references from registered customers and their
     * associate customers.
     *
     * @param magazine the Magazine whose customer subscriptions must be checked
     */
    private void cleanCustomerSubscriptions(Magazine magazine) {

        if (magazine.getCustomers() == null) {
            return;
        }

        for (Customer customer : magazine.getCustomers()) {

            if (customer == null) {
                continue;
            }

            cleanSupplements(customer);

            if (customer instanceof PayingCustomer) {

                PayingCustomer payer = (PayingCustomer) customer;

                if (payer.getAssociate() != null) {

                    for (Customer associate : payer.getAssociate()) {

                        if (associate != null) {
                            cleanSupplements(associate);
                        }
                    }
                }
            }
        }
    }

    /**
     * Creates a cleaned supplement list containing only non-null supplement
     * references and assigns it to the specified customer.
     *
     * @param customer the customer whose supplement list must be cleaned
     */
    private void cleanSupplements(Customer customer) {

        ArrayList<Supplement> cleaned = new ArrayList<>();

        if (customer.getSupplements() != null) {

            for (Supplement supplement : customer.getSupplements()) {

                if (supplement != null) {
                    cleaned.add(supplement);
                }
            }
        }

        customer.setSupplements(cleaned);
    }
}
