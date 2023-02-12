package ui;

import database.DatabaseConnectionHandler;
import model.UserAccount;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends JPanel {
    private JTextField username;
    private JTextField password;
    private Button enterButton;
    private JLabel activeUser;
    private JLabel statusButton;
    private Button signUp;

    public LoginUI(DatabaseConnectionHandler db, UIDrawer ui) {
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(new GridBagLayout());

        username = new JTextField("username");
        username.setPreferredSize(new Dimension(150, (int)username.getPreferredSize().getHeight()));
        c.gridx = 0;
        c.gridy = 0;
        this.add(username, c);
        password = new JTextField("password");
        password.setPreferredSize(new Dimension(150, (int)password.getPreferredSize().getHeight()));
        c.gridx = 0;
        c.gridy = 1;
        this.add(password, c);
        enterButton = new Button("Enter");
        c.gridx = 0;
        c.gridy = 2;
        this.add(enterButton, c);
        signUp = new Button("Sign up");
        c.gridx = 0;
        c.gridy = 3;
        this.add(signUp,c);
        activeUser = new JLabel("Currently logged in as: None");
        c.gridx = 0;
        c.gridy = 4;
        this.add(activeUser, c);
        statusButton = new JLabel("");
        c.gridx = 0;
        c.gridy = 5;
        this.add(statusButton, c);
        statusButton.setVisible(false);

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = username.getText();
                String pass = password.getText();

                if (db.selectUser(user,pass)) {
                    // logged in
                    ui.setUserAccount(new UserAccount(user,pass));
                    activeUser.setText("Currently logged in as: " + user);

                    ui.setRepoCommits(db.get_num_user_commits_per_repo(ui.getUserAccount()));

                    ui.getOrgTab().setVisible(true);
                    ui.getRepoTab().setVisible(true);
                    statusButton.setVisible(false);
                } else{
                    statusButton.setText("Incorrect Username/password");
                    statusButton.setVisible(true);
                }
            }
        });

        signUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = username.getText();
                String pass = password.getText();

                if (!db.selectUser(user,pass)) {
                    db.createUser(user,pass);
                    ui.setUserAccount(new UserAccount(user,pass));
                    activeUser.setText("Currently logged in as: " + user);
                    ui.getOrgTab().setVisible(true);
                    ui.getRepoTab().setVisible(true);
                    statusButton.setVisible(false);
                } else {
                    statusButton.setText("User already exists");
                    statusButton.setVisible(true);
                }
            }
        });
    }


}