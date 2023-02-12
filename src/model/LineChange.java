package model;

public class LineChange {

    private final String Commit_SHA;
    private final int Line_number;

    public LineChange(String commitSHA, int line_number) {
        this.Commit_SHA = commitSHA;
        this.Line_number = line_number;
    }

    public String getCommit_SHA() {
        return Commit_SHA;
    }

    public int getLine_number() {
        return Line_number;
    }
}