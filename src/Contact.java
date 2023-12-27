public class Contact {
    private String firstName;
    private String lastName;
    private String email;
    private String website;
    private String phoneNumber;

    public Contact(String firstName, String lastName, String email, String website, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.website = website;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return String.format("%s %s", capitalize(firstName), capitalize(lastName));
    }

    public String getSummary() {
        return getFullName();
    }

    // Getting the details of a contact
    public String getDetails() {
        return String.format("First Name: %s%nLast Name: %s%nEmail: %s%nWebsite: %s%nPhone Number: %s%n",
                firstName, lastName, email, website, phoneNumber);
    }

    @Override
    public String toString() {
        return getSummary();
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
