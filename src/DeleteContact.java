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

    public DeleteContact(ContactHandler contactHandler, ContactManager contactManager) {
        this.contactHandler = contactHandler;
        this.contactManager = contactManager;

        setLayout(new BorderLayout());

        List<Contact> contacts = contactHandler.getAllContacts();

        contactList = new JList<>(contacts.toArray(new Contact[0]));
        contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        contactList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    handleContactDoubleClick();
                }
            }
        });

        JScrollPane listScrollPane = new JScrollPane(contactList);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);

        add(listScrollPane, BorderLayout.CENTER);
        add(cancelButton, BorderLayout.SOUTH);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setTitle("Delete Contact");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            closeAndShowMainFrame();
        }
    }

    private void handleContactDoubleClick() {
        Contact selectedContact = contactList.getSelectedValue();
        if (selectedContact != null) {
            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this contact?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                contactHandler.deleteContact(selectedContact);
                dispose();
                new DeleteContact(contactHandler, contactManager);
            }
        }
    }

    private void closeAndShowMainFrame() {
        dispose();
        contactManager.setVisible(true);
    }
}