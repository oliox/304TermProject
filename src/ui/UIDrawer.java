package ui;

import database.DatabaseConnectionHandler;
import model.File;
import model.Repository;
import model.UserAccount;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;

public class UIDrawer extends JFrame {

    private JPanel mainPanel;
    private String currentTab;
    private TabButton UserTab;
    private TabButton RepoTab;
    private TabButton OrgTab;
    private LoginUI userPanel;
    private OrgsUI orgPanel;
    private RepoUI repoPanel;
    private GridBagConstraints c;
    private DatabaseConnectionHandler db;
    private UserAccount activeUser;
    private Repository  activeRepo;
    private File activeFile;
    private HashMap<String,Integer> repoCommits;

    private int windowX;
    private int windowY;

    public UIDrawer(DatabaseConnectionHandler db) {
        super("Main Window");
        this.db = db;
        this.setMinimumSize(new Dimension(800, 600));
        windowX = this.getWidth();
        windowY = this.getHeight();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        this.add(mainPanel);

        JPanel buttons = new JPanel();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(buttons, c);

        JPanel fillerPanel = new JPanel();
        c.gridx = 1;
        c.gridy = 0;
        c.weighty = 1.0;
        c.weightx = 1.0;
        mainPanel.add(fillerPanel, c);

        UserTab = new TabButton("User Login", this);
        RepoTab = new TabButton("Repositories", this);
        OrgTab = new TabButton("Organizations", this);

        buttons.add(UserTab);
        buttons.add(RepoTab);
        buttons.add(OrgTab);

        RepoTab.setVisible(false);
        OrgTab.setVisible(false);

        pack();
//        setVisible(true);
        this.setBackground(Color.gray);

        this.addWindowListener(new WindowsCloseHandler());
    }

    private class WindowsCloseHandler implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {
            updateCurrentTab("User Login");
        }

        @Override
        public void windowClosing(WindowEvent e) {
            db.close();
        }

        @Override
        public void windowClosed(WindowEvent e) {

        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    }

    public String getCurrentTab() {
        return currentTab;
    }

    public UserAccount getUserAccount() {
        return activeUser;
    }

    public void setUserAccount(UserAccount u) {
        this.activeUser = u;
    }

    public void updateCurrentTab(String currentTab) {
        this.currentTab = currentTab;
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 5.0;
        c.weightx = 5.0;
        c.fill = GridBagConstraints.BOTH;

        if (currentTab.equals("User Login")) {
            if (userPanel == null) {
                userPanel = new LoginUI(db, this);
                mainPanel.add(userPanel, c);
            } else {
                userPanel.setVisible(true);
            }
            mainPanel.validate();
        } else if (userPanel != null) {
            userPanel.setVisible(false);
        }

        if (currentTab.equals("Repositories")) {
            if (repoPanel == null) {
                repoPanel = new RepoUI(db,this);
                mainPanel.add(repoPanel, c);      
            } else {
                repoPanel.setVisible(true);
            }
            mainPanel.validate();
        } else if (repoPanel != null) {
            repoPanel.setVisible(false);
        }

        if (currentTab.equals("Organizations")) {
            if (orgPanel == null) {
                orgPanel = new OrgsUI(db, this);
                mainPanel.add(orgPanel, c);
                
            } else {
                orgPanel.setVisible(true);
            }
            mainPanel.validate();
        } else if (orgPanel != null) {
            orgPanel.setVisible(false);
        }
    }

    public Repository getActiveRepo() {
        return activeRepo;
    }

    public void setActiveRepo(Repository activeRepo) {
        this.activeRepo = activeRepo;
    }

    public TabButton getRepoTab() {
        return RepoTab;
    }

    public TabButton getOrgTab() {
        return OrgTab;
    }

    public File getActiveFile() {
        return activeFile;
    }

    public void setActiveFile(File activeFile) {
        this.activeFile = activeFile;
    }

    public HashMap<String, Integer> getRepoCommits() {
        return repoCommits;
    }

    public void setRepoCommits(HashMap<String, Integer> repoCommits) {
        this.repoCommits = repoCommits;
    }
}
