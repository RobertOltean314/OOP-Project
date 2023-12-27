import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ContactHandler {

    private static final String CSV_FILE_PATH = "contacts.csv";

    public void addContact(Contact contact) {
        String csvEntry = formatCSVEntry(contact);

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

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] contactData = line.split(",");
                Contact contact = new Contact(contactData[0], contactData[1], contactData[2], contactData[3], contactData[4]);
                contacts.add(contact);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Error reading CSV file");
        }

        return contacts;
    }

    public void deleteContact(Contact contactToDelete) {
        try {
            List<String> lines = new ArrayList<>();
            File file = new File(CSV_FILE_PATH);

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.contains(contactToDelete.getEmail())) {
                        lines.add(line);
                    }
                }
            }

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

    public void updateContact(Contact selectedContact) {
        try {
            File file = new File(CSV_FILE_PATH);
            List<String> lines = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(selectedContact.getEmail())) {
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
                String[] contactData = line.split(",");
                if (matchesCriteria(contactData, searchCriteria, searchValue)) {
                    formattedNames.add(formatName(contactData[0], contactData[1]));
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Error reading CSV file");
        }

        return formattedNames;
    }

    public Contact getContactDetails(String fullName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] contactData = line.split(",");
                Contact contact = new Contact(contactData[0], contactData[1], contactData[2], contactData[3], contactData[4]);
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

    private boolean matchesCriteria(String[] contactData, String searchCriteria, String searchValue) {
        return switch (searchCriteria) {
            case "Email" -> contactData[2].equals(searchValue);
            case "Website" -> contactData[3].equals(searchValue);
            case "Phone Number" -> contactData[4].equals(searchValue);
            case "First Name" -> contactData[0].equals(searchValue);
            case "Last Name" -> contactData[1].equals(searchValue);
            default -> false;
        };
    }

    private String formatName(String firstName, String lastName) {
        return capitalize(firstName) + " " + capitalize(lastName);
    }
    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    private String formatCSVEntry(Contact contact) {
        return String.format("%s,%s,%s,%s,%s", contact.getFirstName(), contact.getLastName(),
                contact.getEmail(), contact.getWebsite(), contact.getPhoneNumber());
    }
}
