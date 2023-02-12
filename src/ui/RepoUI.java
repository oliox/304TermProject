package ui;

import com.google.common.base.Charsets;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import database.DatabaseConnectionHandler;
import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

public class RepoUI extends JPanel {
    private JTextField repoSearch;
    private JTextField fileSearch;

    private JPanel repoPanel;
    private Button repoEnterButton;
    private Button selectRepo;
    private JList repoList;
    private JScrollPane repoScrollPane;
    private JLabel repoLabel;

    private JPanel filePanel;
    private Button fileEnterButton;
    private Button displayFile;
    private JList FileList;
    private JScrollPane fileScrollPane;
    private JLabel fileLabel;
    private JTextArea extensionList;
    private JScrollPane extensionScroller;
    

    private JPanel contentPanel;
    private JTextField filePath;
    private JTextField fileName;
    private JTextArea contents;
    private JScrollPane contentsScroll;
    private Button createFileButton;
    private JLabel contentLabel;

    private JPanel fileEditPanel;
    private Button deleteFile;
    private Button commit;
    private JLabel fileEditLabel;
    private JLabel commitLabel;
    private JTextArea changes;
    private JScrollPane changesScroll;
    private JTextField linesChanged;

    private JLabel userCommits;

    private JLabel topTitle;
    private JList topList;
    private JScrollPane topScrollPane;

    public RepoUI(DatabaseConnectionHandler db, UIDrawer ui) {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        c.weightx = 5.0;
        c.weighty = 5.0;


        repoPanel = buildRepoPanel(db, ui);
        repoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        c.gridx = 0;
        c.gridy = 0;
        this.add(repoPanel, c);

        filePanel = buildFilePanel(db,ui);
        filePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        c.gridx = 1;
        c.gridy = 0;
        this.add(filePanel, c);


        JPanel column3Panel = new JPanel(); 
        column3Panel.setLayout(new GridBagLayout());

        contentPanel = buildContentPanel(db,ui);
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        c.gridx = 0;
        c.gridy = 0;
        column3Panel.add(contentPanel, c);

        fileEditPanel = buildFileEditPanel(db,ui);
        fileEditPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        c.gridx = 0;
        c.gridy = 1;
        column3Panel.add(fileEditPanel, c);

        c.gridx = 2;
        c.gridy = 0;
        this.add(column3Panel, c);


        filePanel.setVisible(false);
        contentPanel.setVisible(false);
        fileEditPanel.setVisible(false);
    }


    private JPanel buildRepoPanel(DatabaseConnectionHandler db, UIDrawer ui) {
        JPanel myPanel = new JPanel();
        GridBagConstraints c = new GridBagConstraints();
        
        c.weightx = 5.0;
        c.gridx = 0;
 
        myPanel.setLayout(new GridBagLayout());

        //No fill

        repoEnterButton = new Button("Enter");
        c.gridy = 2;
        myPanel.add(repoEnterButton, c);

        repoEnterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Repository[] repos = db.getRepositories(repoSearch.getText(), ui.getUserAccount());
                DefaultListModel model = new DefaultListModel();
                for (int i = 0; i < repos.length; i++) {
                    model.addElement(repos[i].getRepository_name());
                }
                repoList.setModel(model);
            }
        });

        selectRepo = new Button("Select Repository");
        c.gridy = 4;
        myPanel.add(selectRepo, c);

        userCommits = new JLabel("");
        c.gridy = 5;
        myPanel.add(userCommits,c);

//        topTitle = new JLabel("Top contributors:");
//        c.gridy = 6;
//        myPanel.add(topList, c);

        userCommits.setVisible(false);
        selectRepo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (repoList.getSelectedValue() != null) {
                    Repository repo = db.getRepository(repoList.getSelectedValue().toString());
                    ui.setActiveRepo(repo);
                    Repository_Object[] files =
                            db.get_repository_objects("",ui.getActiveRepo().getRepository_name());
                    DefaultListModel model = new DefaultListModel();
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].getPath().split("/").length <= 2) {
                            model.addElement(files[i].getPath());
                        }
                    }
                    FileList.setModel(model);

                    extensionList.setText("");
                    String[] extensions = db.get_extensions_list(repo);
                    String ext = "";
                    for (String s: extensions) {
                        ext += s + ",";
                    }
                    if (ext.length() >= 1) {
                        extensionList.setText(ext.substring(0,ext.length()-1));
                    }
                    updateRepoCommits(ui, db);

                    UserAccount[] users = db.getTopContributors(ui.getActiveRepo());
                    if (users != null) {
                        DefaultListModel model2 = new DefaultListModel();
                        for (int i = 0; i < users.length; i++) {
                            model2.addElement(users[i].getUsername());
                        }
                        topList.setModel(model2);
                        topTitle.setText("Top Contributors for " + ui.getActiveRepo().getRepository_name());
                    }

                    filePanel.setVisible(true);
                    contentPanel.setVisible(true);
                } else {
                    filePanel.setVisible(false);
                    contentPanel.setVisible(false);
                }
            }
        });
         
        repoLabel = new JLabel("Search repository here:");
        c.gridy = 0;
        myPanel.add(repoLabel, c);

        //Horizontal fill
        c.fill = GridBagConstraints.HORIZONTAL;
        repoSearch = new JTextField("");
        c.gridy = 1;
        myPanel.add(repoSearch, c);

        //Both fill
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 5.0;
        repoList = new JList();
        repoScrollPane= new JScrollPane(repoList);
        repoScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        c.gridy = 3;
        myPanel.add(repoScrollPane, c);

        return myPanel;
    }

    private JPanel buildFilePanel(DatabaseConnectionHandler db, UIDrawer ui) {
        JPanel myPanel = new JPanel();
        GridBagConstraints c = new GridBagConstraints();
        
        c.weightx = 5.0;
        c.gridx = 0;
 
        myPanel.setLayout(new GridBagLayout());

        //No fill

        c.weightx = 5.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.NONE;

        //No fill
        fileEnterButton = new Button("Enter");
        c.gridy = 2;
        myPanel.add(fileEnterButton, c);

        fileEnterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Repository_Object[] repo_obj =
                        db.get_repository_objects(fileSearch.getText(),ui.getActiveRepo().getRepository_name());
                DefaultListModel model = new DefaultListModel();
                for (int i = 0; i < repo_obj.length; i++) {
                    model.addElement(repo_obj[i].getPath());
                }
                FileList.setModel(model);
            }
        });

        fileLabel = new JLabel("Search file here:");
        c.gridy = 0;
        myPanel.add(fileLabel, c);

        displayFile = new Button("Display File");
        c.gridy = 4;
        myPanel.add(displayFile, c);
        displayFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path = FileList.getSelectedValue().toString();
                File file = db.selectFile(ui.getActiveRepo().getRepository_name(),path);
                if (file.getFile_contents() == null) {
                    File[] files = db.getDirFiles(ui.getActiveRepo().getRepository_name(), path);
                    DefaultListModel model = new DefaultListModel();
                    for (int i = 0; i < files.length; i++) {
                        if (!files[i].getPath().equals(path)) {
                            model.addElement(files[i].getPath());
                        }
                    }
                    FileList.setModel(model);
                    fileEditPanel.setVisible(false);
                } else {
                    String Content = db.viewContents(file);
                    JFrame contentFrame = new JFrame();
                    contentFrame.add(new JLabel(Content));
                    contentFrame.setSize(500,500);
                    contentFrame.setVisible(true);
                    ui.setActiveFile(file);
                    fileEditPanel.setVisible(true);
                }
            }
        });

        //Horizontal fill
        c.fill = GridBagConstraints.HORIZONTAL;
        fileSearch = new JTextField("Search Files");
        c.gridy = 1;
        myPanel.add(fileSearch, c);

        //Both fill
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 5.0;
        String m[] = {"file1", "readme", " dontreadme", "private"};
        FileList = new JList();
        fileScrollPane= new JScrollPane(FileList);
        fileScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        c.gridy = 3;
        myPanel.add(fileScrollPane, c);

        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        extensionList = new JTextArea();
        extensionScroller= new JScrollPane(extensionList);
        extensionScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        c.gridy = 5;
        myPanel.add(extensionScroller, c);

        topTitle = new JLabel("Top Contributors: ");
        c.gridy = 6;
        myPanel.add(topTitle, c);
        topList = new JList();
        topScrollPane= new JScrollPane(topList);
        topScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        c.gridy = 7;
        c.weighty = 0.5;
        myPanel.add(topScrollPane,c);


        return myPanel;
    }

    private JPanel buildContentPanel(DatabaseConnectionHandler db, UIDrawer ui) {
        JPanel myPanel = new JPanel();
        GridBagConstraints c = new GridBagConstraints();
        myPanel.setLayout(new GridBagLayout());
        
        c.weightx = 5.0;
        c.gridx = 0;

        c.weightx = 5.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.NONE;

        createFileButton = new Button("Create File");
        c.gridy = 4;
        myPanel.add(createFileButton, c);

        createFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path = filePath.getText();
                String name = fileName.getText();
                String content = contents.getText();
                String[] split = name.split("[.]");
                String extension = split[split.length - 1];
                System.out.println(extension);
                if (path != null && name != null && content != null) {
                    db.addFile(new File(ui.getActiveRepo().getRepository_name(),
                            path,path + "/" + name, 32,content,extension));
                }
            }
        });
        contentLabel = new JLabel("Create new file:");
        c.gridy = 0;
        myPanel.add(contentLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        filePath = new JTextField("File Path");
        c.gridy = 1;
        myPanel.add(filePath, c);
        fileName = new JTextField("File Name");
        c.gridy = 2;
        myPanel.add(fileName, c);

        c.fill = GridBagConstraints.BOTH;
        c.weighty = 5.0;
        contents = new JTextArea("File Contents");
        contentsScroll= new JScrollPane(contents);
        contentsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        c.gridy = 3;
        myPanel.add(contentsScroll, c);

        return myPanel;
    }

    private JPanel buildFileEditPanel(DatabaseConnectionHandler db, UIDrawer ui) {
        JPanel myPanel = new JPanel();
        GridBagConstraints c = new GridBagConstraints();
        myPanel.setLayout(new GridBagLayout());
        
        c.weightx = 5.0;
        c.gridx = 0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.NONE;

        deleteFile = new Button("Delete File");
        c.gridy = 1;
        myPanel.add(deleteFile, c);

        deleteFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                db.deleteFile(ui.getActiveFile());
            }
        });

        commit = new Button("Commit Changes");
        c.gridy = 5;
        myPanel.add(commit, c);

        commit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String c = changes.getText();
                int lines = Integer.parseInt(linesChanged.getText().substring(13));
                System.out.println(lines);
                int commitNo = db.getHighestCommit(ui.getActiveRepo());
                String repo = ui.getActiveRepo().getRepository_name();
                String author = ui.getUserAccount().getUsername();
                String path = ui.getActiveFile().getPath();
                Timestamp time = db.addTimestamp();

                // Next 3 lines inspired from
                // https://stackoverflow.com/questions/5531455/how-to-hash-some-string-with-sha-256-in-java
                Hasher hasher = Hashing.sha256().newHasher();
                hasher.putString(c, Charsets.UTF_8);
                String sha256 = hasher.hash().toString();

                db.addCommit(new Commit(sha256,commitNo + 1, repo, path, author, time),
                        new LineChange(sha256, lines));

                db.updateFile(ui.getActiveFile(),c);

                updateRepoCommits(ui, db);
            }
        });

        commitLabel = new JLabel("Commit to currently selected file");
        c.gridy = 2;
        myPanel.add(commitLabel, c);

        fileEditLabel = new JLabel("Operations with currently selected file: ");
        c.gridy = 0;
        myPanel.add(fileEditLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        linesChanged = new JTextField("Line changed:");
        c.gridy = 4;
        myPanel.add(linesChanged, c);


        c.fill = GridBagConstraints.BOTH;
        c.weighty = 5.0;
        changes = new JTextArea("Enter new line here");
        changesScroll= new JScrollPane(changes);
        changesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        c.gridy = 3;
        myPanel.add(changesScroll, c);
        
        return myPanel;
    }

    private void updateRepoCommits(UIDrawer ui, DatabaseConnectionHandler db) {
        ui.setRepoCommits(db.get_num_user_commits_per_repo(ui.getUserAccount()));
        int numCommits = 0;
        if (ui.getRepoCommits() != null &&
                ui.getRepoCommits().get(ui.getActiveRepo().getRepository_name()) != null) {
            numCommits = ui.getRepoCommits().get(ui.getActiveRepo().getRepository_name());
        }
        userCommits.setText(ui.getUserAccount().getUsername() + " has " + numCommits + " commits" );
        userCommits.setVisible(true);
    }
}