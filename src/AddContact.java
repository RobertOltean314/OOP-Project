import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * AddContact.java
 * <p>
 * Descriere:
 * <p>
 * Această clasă reprezintă fereastra de adăugare a unui nou contact în aplicația de gestionare a contactelor.
 * Utilizatorul poate introduce informațiile despre un contact, precum numele, prenumele, email-ul, website-ul și numărul de telefon.
 * După introducerea datelor, utilizatorul poate apăsa butonul "Add" pentru a adăuga contactul într-un fișier CSV.
 * În caz de anulare, utilizatorul poate apăsa butonul "Cancel" pentru a închide fereastra fără a adăuga un contact.
 * <p>
 * Metode principale:
 * <p>
 * - AddContact(ContactHandler contactHandler, ContactManager contactManager): Constructorul clasei care inițializează interfața grafică.
 * <p>
 * - actionPerformed(ActionEvent e): Metoda apelată atunci când sunt efectuate acțiuni asupra butoanelor.
 * <p>
 * - Dacă butonul "Add" este apăsat, creează un nou obiect Contact și apelează metoda addContact din clasa ContactHandler.
 * <p>
 * - Dacă butonul "Cancel" este apăsat, închide fereastra de adăugare a contactului.
 */

public class AddContact extends JFrame implements ActionListener {
    // Class members
    private final JButton addButton, cancelButton;
    private final JTextField firstNameField, lastNameField, emailField, websiteField, phoneNumberField;
    private final ContactHandler contactHandler;
    private final ContactManager contactManager;

    // Function that adds a new contact to the CSV file
    public AddContact(ContactHandler contactHandler, ContactManager contactManager) {
        this.contactHandler = contactHandler;
        this.contactManager = contactManager;

        // Layout
        setLayout(new GridLayout(7, 2, 10, 10));

        // Labels
        JLabel firstNameLabel = new JLabel("First Name:");
        JLabel lastNameLabel = new JLabel("Last Name:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel websiteLabel = new JLabel("Website:");
        JLabel phoneNumberLabel = new JLabel("Phone Number:");

        // Fields
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        emailField = new JTextField();
        websiteField = new JTextField();
        phoneNumberField = new JTextField();

        // Buttons
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);

        addButton = new JButton("Add");
        addButton.addActionListener(this);

        // Adding the widgets on the panel
        add(firstNameLabel);
        add(firstNameField);
        add(lastNameLabel);
        add(lastNameField);
        add(emailLabel);
        add(emailField);
        add(websiteLabel);
        add(websiteField);
        add(phoneNumberLabel);
        add(phoneNumberField);
        add(addButton);
        add(cancelButton);

        // fullscreen
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setTitle("Add Contact");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    // Action when button is pressed
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            // Creating a new contact
            Contact newContact = new Contact(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    emailField.getText(),
                    websiteField.getText(),
                    phoneNumberField.getText()
            );
            // Calling the addContact method from contact Handler class
            try {
                contactHandler.addContact(newContact);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            dispose();
            contactManager.setVisible(true);
        } else if (e.getSource() == cancelButton) {
            dispose();
            contactManager.setVisible(true);
        }
    }
}
