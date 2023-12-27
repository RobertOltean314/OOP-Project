import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ContactHandler {

    private static final String CSV_FILE_PATH = "contacts.csv";

    // Method that's writing the contact to the CSV File
    public void addContact(Contact contact) {
        String csvEntry = formatCSVEntry(contact); // Formatting the user input to be written as a string in the CSV

        try (FileWriter writer = new FileWriter(CSV_FILE_PATH, true);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write(csvEntry);
            bufferedWriter.newLine();  // Add a newline character to separate contacts
            System.out.println("Contact added successfully!");
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Error adding contact to CSV file");
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
                Contact contact = new Contact(contactData[0], contactData[1], contactData[2], contactData[3], contactData[4]);
                contacts.add(contact);
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
                // Searching for a specific contact by its Phone Number
                while ((line = reader.readLine()) != null) {
                    if (line.contains(selectedContact.getPhoneNumber())) {
                        String updatedLine = formatCSVEntry(selectedContact); // Formatting the user's input to be String
                        lines.add(updatedLine);
                    } else {
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
            case "First Name" -> contactData[0].equals(searchValue);
            case "Last Name" -> contactData[1].equals(searchValue);
            case "Email" -> contactData[2].equals(searchValue);
            case "Website" -> contactData[3].equals(searchValue);
            case "Phone Number" -> contactData[4].equals(searchValue);
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
}
