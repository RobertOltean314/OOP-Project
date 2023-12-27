import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContactManager extends JFrame implements ActionListener {
    private JButton searchButton;
    private JButton manageButton;
    private JButton exitButton;

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
        manageButton = new JButton("Manage Contacts");
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

        setButtonProperties(searchButton, 0.2, 0.1);
        setButtonProperties(manageButton, 0.2, 0.1);
        setButtonProperties(exitButton, 0.2, 0.1);
    }

    private void setButtonProperties(JButton button, double widthPercentage, double heightPercentage) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * widthPercentage);
        int height = (int) (screenSize.height * heightPercentage);
        button.setPreferredSize(new Dimension(width, height));

        int fontSize = Math.max((int) (height * 0.4), 12);
        button.setFont(new Font("Arial", Font.PLAIN, fontSize));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            new SearchContact(new ContactHandler(), this);
            dispose();
        } else if (e.getSource() == manageButton) {
            new ManageContacts(this);
            dispose();
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ContactManager());
    }
}
