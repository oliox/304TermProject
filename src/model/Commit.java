package model;

import java.sql.Timestamp;

public class Commit {
    private final String Commit_SHA;
    private final int Commit_number;
    private final String Repository_name;
    private final String file_changed_path;
    private final String Author;
    private final Timestamp timestamp;

    public Commit(String commit_SHA, int commit_number, String repository_name,
                  String file_changed_path, String author, Timestamp timestamp) {
        this.Commit_SHA = commit_SHA;
        this.Commit_number = commit_number;
        this.Repository_name = repository_name;
        this.file_changed_path = file_changed_path;
        this.Author = author;
        this.timestamp = timestamp;
    }

    public String getCommit_SHA() {
        return Commit_SHA;
    }

    public int getCommit_number() {
        return Commit_number;
    }

    public String getRepository_name() {
        return Repository_name;
    }

    public String getFile_changed() {
        return file_changed_path;
    }

    public String getAuthor() {
        return Author;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}