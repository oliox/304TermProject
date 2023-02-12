package model;

public class Organization {
    private final String Organization_name;
    private final String Country;

    public Organization(String name, String country) {
        this.Organization_name = name;
        this.Country = country;
    }

    public String getOrganization_name() {
        return Organization_name;
    }

    public String getCountry() {
        return Country;
    }
}