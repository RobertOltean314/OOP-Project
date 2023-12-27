import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageContacts extends JFrame implements ActionListener {
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton backButton;

    private ContactManager contactManager;

    public ManageContacts(ContactManager contactManager) {
        this.contactManager = contactManager;

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

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

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setTitle("Manage Contacts");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void setButtonSize(JButton button, double widthPercentage, double heightPercentage) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * widthPercentage);
        int height = (int) (screenSize.height * heightPercentage);
        button.setPreferredSize(new Dimension(width, height));

        int fontSize = Math.max((int) (height * 0.4), 12);
        button.setFont(new Font("Arial", Font.PLAIN, fontSize));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            new AddContact(new ContactHandler(), contactManager);
            dispose();
        } else if (e.getSource() == updateButton) {
            new UpdateContact(new ContactHandler());
            dispose();
        } else if (e.getSource() == deleteButton) {
            new DeleteContact(new ContactHandler(), contactManager);
            dispose();
        } else if (e.getSource() == backButton) {
            dispose();
            contactManager.setVisible(true);
        }
    }
}
