package ui;

import database.DatabaseConnectionHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// This class is inspired from the demo project https://github.students.cs.ubc.ca/CPSC304/CPSC304_Java_Project
public class DatabaseLoginUi extends JFrame {

    private static final int TEXT_FIELD_WIDTH = 10;
    private JTextField usernameField;
    private JTextField passwordField;

    public DatabaseLoginUi(DatabaseConnectionHandler db, UIDrawer ui) {
        super("Database Login");

        JLabel userLabel = new JLabel("Enter Username: ");
        JLabel passLabel = new JLabel("Enter Password: ");

        usernameField = new JTextField(TEXT_FIELD_WIDTH);
        passwordField = new JTextField(TEXT_FIELD_WIDTH);

        JButton login = new JButton("Log Into Database");

        JPanel contentPanel = new JPanel();
        this.setContentPane(contentPanel);

        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        contentPanel.setLayout(gb);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(userLabel, c);
        contentPanel.add(userLabel);


        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(usernameField, c);
        contentPanel.add(usernameField);


        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(passLabel, c);
        contentPanel.add(passLabel);


        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(passwordField, c);
        contentPanel.add(passwordField);


        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(login, c);
        contentPanel.add(login);


        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = usernameField.getText();
                String pass = passwordField.getText();
                if(db.login(user,pass)) {
                    ui.setVisible(true);
                    dispose();
                }
            }
        });


        this.pack();


        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );


        this.setVisible(true);
    }
}
