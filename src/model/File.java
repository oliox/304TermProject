package model;

public class File extends Repository_Object {
    private final String file_extension;
    private final String file_contents;
    public File(String repositoryName, String parent_path, String path, int size, String file_contents, String file_extension) {
        super(repositoryName, parent_path, path, size);
        this.file_contents = file_contents;
        this.file_extension = file_extension;
    }

    public String getFile_extension() {
        return file_extension;
    }

    public String getFile_contents() {
        return file_contents;
    }
}
