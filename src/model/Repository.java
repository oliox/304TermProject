package model;

import java.sql.Timestamp;
import java.util.Date;

public class Repository {

    private final String Repository_name;
    private final String Organization_name;
    private final Timestamp timestamp;

    public Repository(String name, String organization_name, Timestamp timestamp) {
        Repository_name = name;
        this.Organization_name = organization_name;
        this.timestamp = timestamp;
    }

    public String getRepository_name() {
        return this.Repository_name;
    }

    public String getOrganization_name() {
        return Organization_name;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

}