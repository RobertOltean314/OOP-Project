import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * SearchContact.java
 * <p>
 * Descriere:
 * Această clasă reprezintă fereastra de căutare a contactelor în cadrul aplicației de gestionare a contactelor.
 * Utilizatorul poate selecta un criteriu de căutare (Email, Website, Număr de telefon, Prenume, Nume) și introduce o valoare într-un câmp de căutare.
 * După apăsarea butonului "Search", rezultatele căutării sunt afișate într-o zonă de text.
 * <p>
 * Metode principale:
 * <p>
 * - SearchContact(ContactHandler contactHandler, ContactManager contactManager): Constructorul clasei care inițializează fereastra și componentele de căutare.
 * <p>
 * - actionPerformed(ActionEvent e): Implementare a interfeței ActionListener pentru gestionarea evenimentelor de acțiune (butonul de căutare și butonul de anulare).
 * <p>
 * - searchContact(): Metodă privată pentru efectuarea căutării de contacte în funcție de criteriu și valoare.
 * <p>
 * - closeAndShowMainFrame(): Metodă privată pentru închiderea ferestrei și afișarea ferestrei principale de gestionare a contactelor.
 * <p>
 * - handleDoubleClickOnResultTextArea(MouseEvent e): Metodă privată pentru gestionarea dublu-clicului pe zona de text cu rezultate pentru afișarea detaliilor contactului.
 */

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
        JPanel contentPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        // Create labels, combo box, and text field for search criteria and value
        JLabel searchCriteriaLabel = new JLabel("Search Criteria:");
        JLabel searchValueLabel = new JLabel("Search:");

        String[] searchOptions = {"Email", "Website", "Phone Number", "First Name", "Last Name"};
        searchCriteriaComboBox = new JComboBox<>(searchOptions);
        searchValueField = new JTextField();
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);

        // Create a label and entry box for displaying data
        JLabel infoLabel = new JLabel("Info:");
        JTextField infoEntryField = new JTextField();
        infoEntryField.setEditable(false);

        // Set empty border to remove the default border of text fields
        searchValueField.setBorder(BorderFactory.createEmptyBorder());
        infoEntryField.setBorder(BorderFactory.createEmptyBorder());

        // Create a text area to display search results
        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);

        // Set the preferred size of the text area
        resultTextArea.setPreferredSize(new Dimension(400, 200));

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

        // Add components to the frame
        contentPanel.add(searchCriteriaLabel);
        contentPanel.add(searchCriteriaComboBox);
        contentPanel.add(searchValueLabel);
        contentPanel.add(searchValueField);
        contentPanel.add(infoLabel);
        contentPanel.add(infoEntryField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(searchButton);
        buttonPanel.add(cancelButton);

        // Add panels to the frame
        add(contentPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set the frame properties
        setTitle("Search Contact");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Make the frame fullscreen
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

    // Close the window and show the main ContactManager window
    private void closeAndShowMainFrame() {
        dispose();
        contactManager.setVisible(true);
    }

    // Handle double-click on the result text area to show contact details
    private void handleDoubleClickOnResultTextArea(MouseEvent e) {

        System.out.println("Double-click detected!");

        int caretPosition = resultTextArea.viewToModel2D(e.getPoint());
        try {
            // Get the start and end positions of the clicked line
            int rowStart = resultTextArea.getLineStartOffset(resultTextArea.getLineOfOffset(caretPosition));
            int rowEnd = resultTextArea.getLineEndOffset(resultTextArea.getLineOfOffset(caretPosition));
            // Extract the selected text
            String selectedText = resultTextArea.getText().substring(rowStart, rowEnd);
            // Show detailed information about the contact
            contactHandler.getContactDetails(selectedText);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }
}