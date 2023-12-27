import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DeleteContact extends JFrame implements ActionListener {
    private ContactHandler contactHandler;
    private JList<Contact> contactList;
    private JButton cancelButton;
    private ContactManager contactManager;

    // Constructor for DeleteContact window
    public DeleteContact(ContactHandler contactHandler, ContactManager contactManager) {
        this.contactHandler = contactHandler;
        this.contactManager = contactManager;

        // Set up the layout manager
        setLayout(new BorderLayout());

        // Get the list of all contacts
        List<Contact> contacts = contactHandler.getAllContacts();

        // Create a JList to display contacts with single selection mode
        contactList = new JList<>(contacts.toArray(new Contact[0]));
        contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add a double-click listener to the contact list
        contactList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // Handle double-click on a contact
                    handleContactDoubleClick();
                }
            }
        });

        // Create a scroll pane for the contact list
        JScrollPane listScrollPane = new JScrollPane(contactList);

        // Create a Cancel button and add an action listener
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);

        // Add components to the frame
        add(listScrollPane, BorderLayout.CENTER);
        add(cancelButton, BorderLayout.SOUTH);

        // Set the frame properties
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("Delete Contact");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    // ActionListener implementation
    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle button clicks
        if (e.getSource() == cancelButton) {
            // Close the window and show the main ContactManager window
            closeAndShowMainFrame();
        }
    }

    // Handle double-click on a contact in the list
    private void handleContactDoubleClick() {
        Contact selectedContact = contactList.getSelectedValue();
        if (selectedContact != null) {
            // Show a confirmation dialog before deleting the contact
            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this contact?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                // Delete the contact, dispose of this window, and open a new DeleteContact window
                contactHandler.deleteContact(selectedContact);
                dispose();
                new DeleteContact(contactHandler, contactManager);
            }
        }
    }

    // Close the window and show the main ContactManager window
    private void closeAndShowMainFrame() {
        dispose();
        contactManager.setVisible(true);
    }
}
