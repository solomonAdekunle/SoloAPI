package config;

public enum ContactDetails {

    ADDRESS1("Acme", "Street", "", "Town", "Bedfordshire", "NN17 9RS", "UK"),
    ADDRESS2("Acme", "Street2", "", "Town2", "Hertfordshire", "NN17 9RS", "UK");

    public static ContactDetails currentDetails;
    private String company_name;
    private String line1;
    private String line2;
    private String city;
    private String region;
    private String postcode;

    ContactDetails(String company_name, String line1, String line2, String city, String region, String postcode, String country) {
        this.company_name = company_name;
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.region = region;
        this.postcode = postcode;
        String country1 = country;
    }

    public static void setDetails(ContactDetails details) {
        currentDetails = details;
    }

    public String getCompanyName() {
        return company_name;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getPostcode() {
        return postcode;
    }


}
