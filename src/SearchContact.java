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

    public SearchContact(ContactHandler contactHandler, ContactManager contactManager) {
        this.contactHandler = contactHandler;
        this.contactManager = contactManager;

        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        JLabel searchCriteriaLabel = new JLabel("Search Criteria:");
        JLabel searchValueLabel = new JLabel("Search Value:");

        String[] searchOptions = {"Email", "Website", "Phone Number", "First Name", "Last Name"};
        searchCriteriaComboBox = new JComboBox<>(searchOptions);
        searchValueField = new JTextField();
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);

        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        resultTextArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int caretPosition = resultTextArea.viewToModel(e.getPoint());
                    try {
                        int rowStart = Utilities.getRowStart(resultTextArea, caretPosition);
                        int rowEnd = Utilities.getRowEnd(resultTextArea, caretPosition);
                        String selectedText = resultTextArea.getText().substring(rowStart, rowEnd);
                        showContactDetails(selectedText);
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(resultTextArea);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);

        contentPanel.add(searchCriteriaLabel);
        contentPanel.add(searchCriteriaComboBox);
        contentPanel.add(searchValueLabel);
        contentPanel.add(searchValueField);
        contentPanel.add(scrollPane);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(searchButton);
        buttonPanel.add(cancelButton);

        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setTitle("Search Contact");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            searchContact();
        } else if (e.getSource() == cancelButton) {
            closeAndShowMainFrame();
        }
    }

    private void searchContact() {
        String searchCriteria = (String) searchCriteriaComboBox.getSelectedItem();
        String searchValue = searchValueField.getText();

        List<String> formattedNames = contactHandler.searchContactFormattedNames(searchCriteria, searchValue);

        resultTextArea.setText(String.join("\n", formattedNames));
    }

    private void showContactDetails(String selectedText) {
        ContactHandler contactHandler = new ContactHandler();

        Contact contact = contactHandler.getContactDetails(selectedText);

        if (contact != null) {
            JOptionPane.showMessageDialog(this, formatContactDetails(contact), "Contact Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private String formatContactDetails(Contact contact) {
        return "First Name: " + contact.getFirstName() + "\n" +
                "Last Name: " + contact.getLastName() + "\n" +
                "Email: " + contact.getEmail() + "\n" +
                "Website: " + contact.getWebsite() + "\n" +
                "Phone Number: " + contact.getPhoneNumber();
    }

    private void closeAndShowMainFrame() {
        dispose();
        contactManager.setVisible(true);
    }
}
