import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddContact extends JFrame implements ActionListener {
    // Class members
    private JButton addButton, cancelButton;
    private JTextField firstNameField, lastNameField, emailField, websiteField, phoneNumberField;
    private ContactHandler contactHandler;
    private ContactManager contactManager;

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
            contactHandler.addContact(newContact);
            closeAndShowMainFrame();
        } else if (e.getSource() == cancelButton) {
            closeAndShowMainFrame();
        }
    }
    // Closing the window and open the main Frame
    private void closeAndShowMainFrame() {
        dispose();
        contactManager.setVisible(true);
    }
}
