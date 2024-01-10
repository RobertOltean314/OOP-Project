import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * EditContacts.java
 * <p>
 * Descriere:
 * Această clasă reprezintă fereastra de gestionare a contactelor în cadrul aplicației de gestionare a contactelor.
 * Utilizatorul poate accesa funcționalitățile de adăugare, actualizare și ștergere a contactelor, sau poate reveni la fereastra principală.
 * Fereastra conține patru butoane: "Add Contact", "Update Contact", "Delete Contact" și "Back".
 * Fiecare buton are dimensiuni ajustate în funcție de dimensiunile ecranului și are un font proporțional.
 * <p>
 * Metode principale:
 * <p>
 * - EditContacts(ContactManager contactManager): Constructorul clasei care inițializează fereastra și butoanele de gestionare a contactelor.
 * <p>
 * - setButtonSize(JButton button, double widthPercentage, double heightPercentage): Metodă privată pentru a seta dimensiunea și fontul butoanelor în funcție de dimensiunile ecranului.
 * <p>
 * - actionPerformed(ActionEvent e): Implementare a interfeței ActionListener pentru gestionarea evenimentelor de acțiune.
 */

public class EditContacts extends JFrame implements ActionListener {
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton backButton;

    private ContactManager contactManager;

    public EditContacts(ContactManager contactManager) {
        this.contactManager = contactManager;

        // Set up the layout manager
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create buttons
        addButton = new JButton("Add Contact");
        addButton.addActionListener(this);
        setButtonSize(addButton, 0.2, 0.1);
        add(addButton, gbc);

        gbc.gridy++;
        updateButton = new JButton("Update Contact");
        updateButton.addActionListener(this);
        setButtonSize(updateButton, 0.2, 0.1);
        add(updateButton, gbc);

        gbc.gridy++;
        deleteButton = new JButton("Delete Contact");
        deleteButton.addActionListener(this);
        setButtonSize(deleteButton, 0.2, 0.1);
        add(deleteButton, gbc);

        gbc.gridy++;
        backButton = new JButton("Back");
        backButton.addActionListener(this);
        setButtonSize(backButton, 0.2, 0.1);
        add(backButton, gbc);

        // Set the frame properties
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("Manage Contacts");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    // Method to set the size and font of a button based on screen size
    private void setButtonSize(JButton button, double widthPercentage, double heightPercentage) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * widthPercentage);
        int height = (int) (screenSize.height * heightPercentage);
        button.setPreferredSize(new Dimension(width, height));

        int fontSize = Math.max((int) (height * 0.4), 12);
        button.setFont(new Font("Arial", Font.PLAIN, fontSize));
    }

    // ActionListener implementation
    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle button clicks
        if (e.getSource() == addButton) {
            // Open the AddContact window and dispose of this window
            new AddContact(new ContactHandler(), contactManager);
            dispose();
        } else if (e.getSource() == updateButton) {
            // Open the UpdateContact window and dispose of this window
            new UpdateContact(new ContactHandler());
            dispose();
        } else if (e.getSource() == deleteButton) {
            // Open the DeleteContact window and dispose of this window
            new DeleteContact(new ContactHandler(), contactManager);
            dispose();
        } else if (e.getSource() == backButton) {
            // Dispose of this window and show the ContactManager window
            dispose();
            contactManager.setVisible(true);
        }
    }
}
