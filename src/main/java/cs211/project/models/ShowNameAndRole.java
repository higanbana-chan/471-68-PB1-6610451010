package cs211.project.models;

public class ShowNameAndRole {
    private String firstName;
    private String lastName;
    private String username;
    private String role;

    public ShowNameAndRole(String username , String firstName , String lastName , String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.role = role;
    }



    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }


}
