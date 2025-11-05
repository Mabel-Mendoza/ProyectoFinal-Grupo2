package clases;

public class User {
    private final String username;
    private final String displayName;
    private final Role role;

    public User(String username, String displayName, Role role) {
        this.username = username;
        this.displayName = displayName;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getDisplayName() { return displayName; }
    public Role getRole() { return role; }
}
