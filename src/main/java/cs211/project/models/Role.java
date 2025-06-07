package cs211.project.models;

public class Role {
    String username;
    String eventId;
    String role;

    public Role(String username, String eventId, String role) {
        this.username = username;
        this.eventId = eventId;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getEventId() {
        return eventId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
