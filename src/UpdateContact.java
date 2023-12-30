import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UpdateContact extends JFrame implements ActionListener {
    private final ContactHandler contactHandler;
    private final JComboBox<Contact> contactComboBox;
    private final JTextField firstNameField, lastNameField, emailField, websiteField, phoneNumberField;
    private final JButton updateButton, cancelButton;

    // Constructor initializes the UI components
    public UpdateContact(ContactHandler contactHandler) {
        this.contactHandler = contactHandler;

        setLayout(new BorderLayout());

        List<Contact> contacts = contactHandler.getAllContacts();

        contactComboBox = new JComboBox<>(contacts.toArray(new Contact[0]));
        contactComboBox.addActionListener(this);

        firstNameField = new JTextField();
        lastNameField = new JTextField();
        emailField = new JTextField();
        websiteField = new JTextField();
        phoneNumberField = new JTextField();

        updateButton = new JButton("Update");
        updateButton.addActionListener(this);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);

        JPanel contentPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        contentPanel.add(new JLabel("Select Contact:"));
        contentPanel.add(contactComboBox);
        contentPanel.add(new JLabel("First Name:"));
        contentPanel.add(firstNameField);
        contentPanel.add(new JLabel("Last Name:"));
        contentPanel.add(lastNameField);
        contentPanel.add(new JLabel("Email:"));
        contentPanel.add(emailField);
        contentPanel.add(new JLabel("Website:"));
        contentPanel.add(websiteField);
        contentPanel.add(new JLabel("Phone Number:"));
        contentPanel.add(phoneNumberField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setTitle("Update Contact");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    // ActionListener method to handle button clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == contactComboBox) {
            displayContactDetails((Contact) contactComboBox.getSelectedItem());
        } else if (e.getSource() == updateButton) {
            Contact selectedContact = (Contact) contactComboBox.getSelectedItem();
            if (selectedContact != null && validateInput()) {
                // Update contact fields
                selectedContact.setFirstName(firstNameField.getText());
                selectedContact.setLastName(lastNameField.getText());
                selectedContact.setEmail(emailField.getText());
                selectedContact.setWebsite(websiteField.getText());
                selectedContact.setPhoneNumber(phoneNumberField.getText());

                try {
                    // Call the ContactHandler to update the contact
                    contactHandler.updateContact(selectedContact);
                    // Close the current window and show the main frame
                    closeAndShowMainFrame();
                } catch (Exception ex) {
                    // Handle exception (e.g., display an error message)
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error updating contact: " + ex.getMessage(),
                            "Update Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == cancelButton) {
            // Confirm cancellation
            int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel?",
                    "Confirm Cancel", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                // Clear fields, close the current window, and show the main frame
                clearFields();
                closeAndShowMainFrame();
            }
        }
    }

    // Clear input fields
    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        websiteField.setText("");
        phoneNumberField.setText("");
    }

    // Close the current window and show the main frame
    private void closeAndShowMainFrame() {
        Frame[] frames = Frame.getFrames();

        for (Frame frame : frames) {
            if (frame instanceof ContactManager) {
                // Dispose only if it's not the main frame
                if (frame != this) {
                    dispose();
                }
                frame.setVisible(true);
            }
        }
    }


    // Validate input fields (you can customize this method based on your requirements)
    private boolean validateInput() {
        // Add your validation logic here
        // For example, check if required fields are not empty, if email is in a valid format, etc.
        return true;
    }


    // Display details of the selected contact
    private void displayContactDetails(Contact selectedContact) {
        if (selectedContact != null) {
            firstNameField.setText(selectedContact.getFirstName());
            lastNameField.setText(selectedContact.getLastName());
            emailField.setText(selectedContact.getEmail());
            websiteField.setText(selectedContact.getWebsite());
            phoneNumberField.setText(selectedContact.getPhoneNumber());
        }
    }

}
