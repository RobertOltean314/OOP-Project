import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ContactHandler.java
 * <p>
 * Descriere:
 * <p>
 * Această clasă gestionează manipularea contactelor în cadrul aplicației de gestionare a contactelor.
 * Ea oferă funcționalități precum adăugarea, ștergerea, actualizarea și căutarea contactelor într-un fișier CSV.
 * Clasa se ocupă, de asemenea, de validarea datelor de contact, inclusiv verificarea formatului de email, website si a numărului de telefon.
 * <p>
 * Metode principale:
 * <p>
 * - addContact(Contact contact): Adaugă un nou contact în fișierul CSV după ce validează informațiile de contact.
 * <p>
 * - getAllContacts(): Returnează o listă cu toate contactele citite din fișierul CSV.
 * <p>
 * - deleteContact(Contact contactToDelete): Șterge un contact specificat din fișierul CSV.
 * <p>
 * - updateContact(Contact selectedContact): Actualizează informațiile unui contact specificat în fișierul CSV.
 * <p>
 * - searchContactFormattedNames(String searchCriteria, String searchValue): Caută contacte în funcție de criterii specifice și returnează numele formatate.
 * <p>
 * - getContactDetails(String fullName): Returnează detaliile unui contact în funcție de numele complet specificat.
 * <p>
 * Metode de validare:
 * <p>
 * - isValidEmail(String email): Verifică dacă un șir de caractere reprezintă un format de email valid.
 * <p>
 * - isValidWebsite(String website): Verifică dacă un șir de caractere reprezintă un format de website valid.
 * <p>
 * - isPhoneNumberUnique(String phoneNumber): Verifică dacă un număr de telefon este unic în lista de contacte.
 * <p>
 * Metode ajutătoare:
 * <p>
 * - formatName(String firstName, String lastName): Formatează numele într-un stil cu prima literă mare și restul litere mici.
 * <p>
 * - capitalize(String str): Capitalizează primul caracter al unui șir și transformă restul literelor în minuscule.
 * <p>
 * - formatCSVEntry(Contact contact): Formatează un contact pentru a fi scris într-un fișier CSV.
 */

public class ContactHandler {

    private final String CSV_FILE_PATH = "contacts.csv";

    // Method that's writing the contact to the CSV File
    public void addContact(Contact contact) throws IOException {
        // Validate email pattern
        if (!isValidEmail(contact.getEmail())) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error: Invalid email format.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // Validate phone number length
        if (contact.getPhoneNumber().length() != 10) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error: Invalid phone number",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Check if the phone number is unique before adding the contact
        if (!isPhoneNumberUnique(contact.getPhoneNumber())) {
            Object[] options = {"Cancel", "Update"};
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Error: This phone number already exists in your agenda. Do you want to update it?",
                    "Duplicate Phone Number",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == JOptionPane.YES_OPTION) {
                // User clicked "Cancel", return to the main page or perform necessary action
                new ContactManager().setVisible(true);
            } else if (choice == JOptionPane.NO_OPTION) {
                // User clicked "Update", open the Update window or perform necessary action
                new UpdateContact(this).setVisible(true);
            }
            return;
        }

        // Validate website pattern (you can adjust the pattern as needed)
        if (!isValidWebsite(contact.getWebsite())) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error: Invalid website format.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        // Continue with adding the contact if all validations pass
        String csvEntry = formatCSVEntry(contact);

        try (FileWriter writer = new FileWriter(CSV_FILE_PATH, true)) {
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(csvEntry);
            bufferedWriter.newLine();
            bufferedWriter.flush(); // Flush changes to ensure everything is saved
            bufferedWriter.close(); // Close the writer
            System.out.println("Contact added successfully");
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Error adding contact to CSV file: " + ex.getMessage());

        }
    }

    // Reading all contacts from the CSV and return them as a list
    public List<Contact> getAllContacts() {
        // Creating a list to store all the contacts
        List<Contact> contacts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] contactData = line.split(",");

                // Check if the array has enough elements to create a Contact
                if (contactData.length >= 5) {
                    Contact contact = new Contact(contactData[0], contactData[1], contactData[2], contactData[3], contactData[4]);
                    contacts.add(contact);
                } else {
                    // Handle the case where the line doesn't have enough fields
                    System.err.println("Error: Invalid data in CSV file. Skipping line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading CSV file");
        }

        return contacts;
    }


    // Method that deletes a contact
    public void deleteContact(Contact contactToDelete) {
        try {
            List<String> lines = new ArrayList<>();
            File file = new File(CSV_FILE_PATH);

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                // Searching for the contact to delete by Phone Number
                while ((line = reader.readLine()) != null) {
                    if (!line.contains(contactToDelete.getPhoneNumber())) {
                        lines.add(line);
                    }
                }
            }

            // Write the updated list back to the CSV
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String line : lines) {
                    writer.write(line + System.lineSeparator());
                }
            }

            System.out.println("Contact deleted successfully!");

        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Error deleting contact from CSV file");
        }
    }

    // Method that updates a contact
    public void updateContact(Contact selectedContact) {
        try {
            File file = new File(CSV_FILE_PATH);
            List<String> lines = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(selectedContact.getPhoneNumber())) {
                        String updatedLine = formatCSVEntry(selectedContact);
                        lines.add(updatedLine);
                    } else {
                        lines.add(line);
                    }
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String line : lines) {
                    writer.write(line + System.lineSeparator());
                }
            }

            System.out.println("Contact updated successfully!");

        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Error updating contact in CSV file");
        }
    }

    public List<String> searchContactFormattedNames(String searchCriteria, String searchValue) {
        List<String> formattedNames = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the CSV line into an array of contact data
                String[] contactData = line.split(",");

                // Check if the contact matches the specified criteria
                if (matchesCriteria(contactData, searchCriteria, searchValue)) {
                    // Add the formatted name to the list
                    formattedNames.add(formatName(contactData[0], contactData[1]));
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Error reading CSV file");
        }

        return formattedNames;
    }


    // Method that returns a contact's details
    public Contact getContactDetails(String fullName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the CSV line into an array of contact data
                String[] contactData = line.split(",");

                // Create a Contact object using the contact data
                Contact contact = new Contact(contactData[0], contactData[1], contactData[2], contactData[3], contactData[4]);

                // Check if the full name of the contact matches (case-insensitive)
                if (contact.getFullName().equalsIgnoreCase(fullName)) {
                    return contact;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Error reading CSV file");
        }

        return null;
    }


    // Method that checks a specific criteria
    private boolean matchesCriteria(String[] contactData, String searchCriteria, String searchValue) {
        return switch (searchCriteria) {
            case "First Name" -> contactData[0].equalsIgnoreCase(searchValue);
            case "Last Name" -> contactData[1].equalsIgnoreCase(searchValue);
            case "Email" -> contactData[2].equalsIgnoreCase(searchValue);
            case "Website" -> contactData[3].equalsIgnoreCase(searchValue);
            case "Phone Number" -> contactData[4].equalsIgnoreCase(searchValue);
            default -> false;
        };
    }

    // Method that creates Full Name
    private String formatName(String firstName, String lastName) {
        return capitalize(firstName) + " " + capitalize(lastName);
    }

    // Method that capitalize the Name (First + Last Name)
    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    // Method that formats the user input as String
    private String formatCSVEntry(Contact contact) {
        return String.format("%s,%s,%s,%s,%s", contact.getFirstName().toLowerCase(), contact.getLastName().toLowerCase(),
                contact.getEmail().toLowerCase(), contact.getWebsite().toLowerCase(), contact.getPhoneNumber().toLowerCase());
    }

    private boolean isPhoneNumberUnique(String phoneNumber) {
        List<Contact> contacts = getAllContacts();
        return contacts.stream().noneMatch(contact -> contact.getPhoneNumber().equals(phoneNumber));
    }

    // Validation check for email pattern
    private boolean isValidEmail(String email) {
        // You can use a more sophisticated email validation pattern if needed
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    // Validation check for website pattern
    private boolean isValidWebsite(String website) {
        // You can use a more sophisticated website validation pattern if needed
        return website.matches("^www\\.[a-zA-Z0-9]+\\.[a-zA-Z]{2,}$");
    }
}