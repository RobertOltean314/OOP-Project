import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class SearchContact extends JFrame implements ActionListener {
    private JComboBox<String> searchCriteriaComboBox;
    private JTextField searchValueField;
    private JButton searchButton;
    private JTextArea resultTextArea;
    private JButton cancelButton;
    private ContactHandler contactHandler;
    private ContactManager contactManager;

    // Constructor for the SearchContact window
    public SearchContact(ContactHandler contactHandler, ContactManager contactManager) {
        this.contactHandler = contactHandler;
        this.contactManager = contactManager;

        // Set up the layout manager
        setLayout(new BorderLayout());

        // Create a panel to hold the input fields and search results
        JPanel contentPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        // Create labels, combo box, and text field for search criteria and value
        JLabel searchCriteriaLabel = new JLabel("Search Criteria:");
        JLabel searchValueLabel = new JLabel("Search Value:");

        String[] searchOptions = {"Email", "Website", "Phone Number", "First Name", "Last Name"};
        searchCriteriaComboBox = new JComboBox<>(searchOptions);
        searchValueField = new JTextField();
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);

        // Create a text area to display search results
        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);

        // Add a double click listener to the text area for showing contact details
        resultTextArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    handleDoubleClickOnResultTextArea(e);
                }
            }
        });

        // Create a scroll pane for the text area
        JScrollPane scrollPane = new JScrollPane(resultTextArea);

        // Create a Cancel button and add an action listener
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);

        // Add components to the content panel and button panel
        contentPanel.add(searchCriteriaLabel);
        contentPanel.add(searchCriteriaComboBox);
        contentPanel.add(searchValueLabel);
        contentPanel.add(searchValueField);
        contentPanel.add(scrollPane);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(searchButton);
        buttonPanel.add(cancelButton);

        // Add panels to the frame
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set the frame properties
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("Search Contact");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    // ActionListener implementation
    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle button clicks
        if (e.getSource() == searchButton) {
            // Perform the contact search
            searchContact();
        } else if (e.getSource() == cancelButton) {
            // Close the window and show the main ContactManager window
            closeAndShowMainFrame();
        }
    }

    // Perform the contact search based on criteria and value
    private void searchContact() {
        String searchCriteria = (String) searchCriteriaComboBox.getSelectedItem();
        String searchValue = searchValueField.getText();

        // Get formatted names of contacts that match the search criteria
        List<String> formattedNames = contactHandler.searchContactFormattedNames(searchCriteria, searchValue);

        // Display the search results in the text area
        resultTextArea.setText(String.join("\n", formattedNames));
    }

    // Show detailed information about a contact on double-click
    private void showContactDetails(String selectedText) {
        // Create a new ContactHandler to fetch details
        ContactHandler contactHandler = new ContactHandler();

        // Get Contact details based on the selected text
        Contact contact = contactHandler.getContactDetails(selectedText);

        // Display details in a dialog
        if (contact != null) {
            JOptionPane.showMessageDialog(this, formatContactDetails(contact), "Contact Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Format contact details for display
    private String formatContactDetails(Contact contact) {
        return "First Name: " + contact.getFirstName() + "\n" +
                "Last Name: " + contact.getLastName() + "\n" +
                "Email: " + contact.getEmail() + "\n" +
                "Website: " + contact.getWebsite() + "\n" +
                "Phone Number: " + contact.getPhoneNumber();
    }

    // Close the window and show the main ContactManager window
    private void closeAndShowMainFrame() {
        dispose();
        contactManager.setVisible(true);
    }

    // Handle double-click on the result text area to show contact details
    private void handleDoubleClickOnResultTextArea(MouseEvent e) {
        int caretPosition = resultTextArea.viewToModel2D(e.getPoint());
        try {
            // Get the start and end positions of the clicked line
            int rowStart = Utilities.getRowStart(resultTextArea, caretPosition);
            int rowEnd = Utilities.getRowEnd(resultTextArea, caretPosition);
            // Extract the selected text
            String selectedText = resultTextArea.getText().substring(rowStart, rowEnd);
            // Show detailed information about the contact
            showContactDetails(selectedText);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }
}
