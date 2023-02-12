package ui;

import database.DatabaseConnectionHandler;
import model.Organization;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrgsUI extends JPanel {
    private JTextField orgSearch;
    private Button enterButton;
    private Button addToUser;
    private JList orgList;
    private JScrollPane orgScrollPane;
    private JList megaList;
    private JScrollPane megaScrollPane;
    private JLabel megaTitle;
    private JLabel aveCommits;

    private JPanel beforePanel;
    private JPanel afterPanel;

    private DatabaseConnectionHandler db;
    private UIDrawer ui;


    public OrgsUI(DatabaseConnectionHandler db, UIDrawer ui) {
        this.db = db;
        this.ui = ui;
        GridBagConstraints c = new GridBagConstraints();
        
        c.weightx = 5.0;
        c.weighty = 5.0;
        c.fill = GridBagConstraints.BOTH;
 
        this.setLayout(new GridBagLayout());

        beforePanel = buildBeforeSelectPanel();
        afterPanel = buildAfterSelectPanel();
        afterPanel.setVisible(false);
        beforePanel.setBorder(new EmptyBorder(10, 10, 10, 20));
        c.gridx = 0;
        c.gridy = 0;
        this.add(beforePanel, c);
        c.gridx = 1;
        c.gridy = 0;
        this.add(afterPanel, c);
        this.add(new JPanel(), c);

        addListener();
    }

    private JPanel buildBeforeSelectPanel() {
        JPanel myPanel = new JPanel();
        GridBagConstraints c = new GridBagConstraints();
        myPanel.setLayout(new GridBagLayout());
        c.fill = GridBagConstraints.NONE;
        c.weightx = 5.0;

        enterButton = new Button("Enter");
        c.gridx = 0;
        c.gridy = 1;

        myPanel.add(enterButton, c);

        addToUser = new Button("Add org to current user");
        c.gridx = 0;
        c.gridy = 3;
        myPanel.add(addToUser, c);


        //Horizontal fill
        c.fill = GridBagConstraints.HORIZONTAL;
        orgSearch = new JTextField("Search Organizations");
        c.gridx = 0;
        c.gridy = 0;
        myPanel.add(orgSearch, c);

        //Both fill
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 5.0;
        String l[] = {};
        orgList = new JList(l);

        orgScrollPane= new JScrollPane(orgList);
        orgScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        c.gridx = 0;
        c.gridy = 2;
   
        myPanel.add(orgScrollPane, c);
        return myPanel;

    }

    private JPanel buildAfterSelectPanel() {
        JPanel myPanel = new JPanel();
        GridBagConstraints c = new GridBagConstraints();
        myPanel.setLayout(new GridBagLayout());
        c.fill = GridBagConstraints.NONE;
        c.weightx = 2.0;
        
        c.gridx = 0;

        aveCommits = new JLabel("Average number of commits per user: 10");
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 2.0;
        myPanel.add(aveCommits, c);

//        lbel3 = new JLabel("something else?");
//        c.gridx = 1;
//        c.gridy = 1;
//        c.weightx = 2.0;
//        myPanel.add(lbel3, c);

//        mostUsed = new JLabel("Most commited repo: here");
//        c.gridx = 1;
//        c.gridy = 2;
//        c.weightx = 2.0;
//        myPanel.add(mostUsed, c);
//        c.weightx = 5.0;

        c.fill = GridBagConstraints.BOTH;
        
        megaTitle = new JLabel("Mega Users List");
        c.gridx = 0;
        c.gridy = 0;
        myPanel.add(megaTitle, c);

        c.weighty = 5.0;
        String m[] = {};
        megaList = new JList(m);
        megaScrollPane= new JScrollPane(megaList);
        megaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        c.gridx = 0;
        c.gridy = 3;
        myPanel.add(megaScrollPane, c);

        return myPanel;
    }

//    public void updateVisibility() {
//        if (ui.getUserAccount() != null) {
//            beforePanel.setVisible(true);
//            afterPanel.setVisible(false);
//
//        } else {
//            beforePanel.setVisible(false);
//            afterPanel.setVisible(false);
//        }
//    }



    protected void addListener() {
        enterButton.addActionListener(new EnterPressHandler());
        addToUser.addActionListener(new AddOrgPressHandler());
        orgList.addListSelectionListener(new OrgSelectListener());
    }

    //When the enter button is pressed for the search bar, search and display matching orgs
    private class EnterPressHandler implements ActionListener {
        private EnterPressHandler() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Organization[] orgs = db.getAllOrganizations(orgSearch.getText());
            //String[] orgNames = new String[orgs.length];
            DefaultListModel model = new DefaultListModel();
            for (int i = 0; i < orgs.length; i++) {
                //orgNames[i] = orgs[i].getOrganization_name();
                model.addElement(orgs[i].getOrganization_name());
            }
            //model.addElement(orgNames);
            orgList.setModel(model);

        }
    }

    //When a new item is selected on the list of orgs, update mega contributers and average commits
    private class OrgSelectListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (orgList.getSelectedValue() != null) {
                Organization org = db.getOrganization(orgList.getSelectedValue().toString());
                String[] megaContributors = db.find_mega_contributers(org);

                DefaultListModel model = new DefaultListModel();
                for (int i = 0; i < megaContributors.length; i++) {
                    model.addElement(megaContributors[i]);
                }
                afterPanel.setVisible(true);
                megaList.setModel(model);

                int aveNum = db.get_avg_contributer_commit_per_repo(org);
                String ave = "Average number of commits per repo: " + aveNum;
                aveCommits.setText(ave);
            } else {
                afterPanel.setVisible(false);
            }
        }
    }

    //Add org to current user
    private class AddOrgPressHandler implements ActionListener {
        private AddOrgPressHandler() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (ui.getUserAccount() != null) {
                Organization org = db.getOrganization(orgList.getSelectedValue().toString());
                db.setOrganization(ui.getUserAccount(), org);
            }
        }
    }
}