import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ContactManager.java
 * <p>
 * Descriere:
 * <p>
 * Această clasă reprezintă fereastra principală a aplicației de gestionare a contactelor.
 * Utilizatorul poate accesa funcționalități precum căutarea și editarea contactelor sau oprirea aplicației.
 * Interfața grafică este alcătuită din trei butoane: "Search Contacts", "Edit Contacts" și "Exit".
 * Fiecare buton are dimensiuni ajustate în funcție de dimensiunile ecranului și are un font proporțional.
 * <p>
 * Metode principale:
 * <p>
 * - ContactManager(): Constructorul clasei care inițializează fereastra și butoanele principale ale aplicației.
 * <p>
 * - setButtonProperties(JButton button, double widthPercentage, double heightPercentage): Metodă privată pentru a seta proprietățile butoanelor,
 * <p>
 * precum dimensiunea și fontul, în funcție de dimensiunile ecranului.
 * <p>
 * - actionPerformed(ActionEvent e): Metodă apelată la acțiuni asupra butoanelor, care deschide ferestrele corespunzătoare și închide fereastra curentă.
 * <p>
 * - main(String[] args): Metodă statică pentru a rula aplicația `ContactManager` pe firul de dispecerare al evenimentelor Swing.
 */

public class ContactManager extends JFrame implements ActionListener {
    private final JButton searchButton;
    private final JButton manageButton;
    private final JButton exitButton;

    public ContactManager() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create buttons
        searchButton = new JButton("Search Contacts");
        searchButton.addActionListener(this);
        add(searchButton, gbc);

        gbc.gridy++;
        manageButton = new JButton("Edit Contacts");
        manageButton.addActionListener(this);
        add(manageButton, gbc);

        gbc.gridy++;
        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
        add(exitButton, gbc);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setTitle("Contact Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Set button properties
        setButtonProperties(searchButton, 0.2, 0.1);
        setButtonProperties(manageButton, 0.2, 0.1);
        setButtonProperties(exitButton, 0.2, 0.1);
    }

    // Set properties for buttons (size + font)
    private void setButtonProperties(JButton button, double widthPercentage, double heightPercentage) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * widthPercentage);
        int height = (int) (screenSize.height * heightPercentage);
        button.setPreferredSize(new Dimension(width, height));

        // Adjust font size based on button height
        int fontSize = Math.max((int) (height * 0.4), 12);
        button.setFont(new Font("Arial", Font.PLAIN, fontSize));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            // Open the SearchContact window and dispose of the current window
            new SearchContact(new ContactHandler(), this);
            dispose();
        } else if (e.getSource() == manageButton) {
            // Open the EditContacts window and dispose of the current window
            new EditContacts(this);
            dispose();
        } else if (e.getSource() == exitButton) {
            // Exit the application
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        // Run the ContactManager application on the Swing event dispatch thread
        SwingUtilities.invokeLater(ContactManager::new);
    }
}
