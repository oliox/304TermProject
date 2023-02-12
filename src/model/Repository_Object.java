package model;

public class Repository_Object {
    private final String repositoryName;
    private final String path;

    private final String parentDirPath;
    private final int Size;

    public Repository_Object(String repositoryName, String parent_path, String path, int size) {
        this.repositoryName = repositoryName;
        this.path = path;
        this.parentDirPath = parent_path;
        this.Size = size;
    }

    public String getPath() {
        return path;
    }
    public String getParentDirectory() { return parentDirPath; }
    public int getSize() {
        return Size;
    }
    public String getRepositoryName() { return repositoryName; }
}