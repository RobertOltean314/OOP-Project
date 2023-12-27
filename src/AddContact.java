import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddContact extends JFrame implements ActionListener {
    private JButton addButton, cancelButton;
    private JTextField firstNameField, lastNameField, emailField, websiteField, phoneNumberField;
    private ContactHandler contactHandler;
    private ContactManager contactManager;

    public AddContact(ContactHandler contactHandler, ContactManager contactManager) {
        this.contactHandler = contactHandler;
        this.contactManager = contactManager;

        setLayout(new GridLayout(7, 2, 10, 10));

        JLabel firstNameLabel = new JLabel("First Name:");
        JLabel lastNameLabel = new JLabel("Last Name:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel websiteLabel = new JLabel("Website:");
        JLabel phoneNumberLabel = new JLabel("Phone Number:");

        firstNameField = new JTextField();
        lastNameField = new JTextField();
        emailField = new JTextField();
        websiteField = new JTextField();
        phoneNumberField = new JTextField();

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);

        addButton = new JButton("Add");
        addButton.addActionListener(this);

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

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setTitle("Add Contact");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            Contact newContact = new Contact(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    emailField.getText(),
                    websiteField.getText(),
                    phoneNumberField.getText()
            );
            contactHandler.addContact(newContact);
            closeAndShowMainFrame();
        } else if (e.getSource() == cancelButton) {
            closeAndShowMainFrame();
        }
    }

    private void closeAndShowMainFrame() {
        dispose();
        contactManager.setVisible(true);
    }
}
