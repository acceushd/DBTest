import enums.ROLES;

/**
 * Creates a User with a unique ID, a Username and an email-address
 */
public class User {
    final private int ID;
    private String userName;
    private String email;
    private ROLES role;


    /**
     * Creates a new User with a unique ID, Username, email and Role
     *
     * @param ID       Int
     * @param userName String
     * @param email    String
     * @param role     String
     */
    public User(int ID, String userName, String email, ROLES role) {
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (isNotValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email address");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }
        this.ID = ID;
        this.userName = userName;
        this.email = email;
        this.role = role;
    }

    /**
     * Creates a User with a unique ID, Username, email and the user Role
     *
     * @param ID       Int
     * @param userName String
     * @param email    String
     */
    public User(int ID, String userName, String email) {
        this(ID, userName, email, ROLES.USER);
    }

    public int getID() {
        return ID;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public ROLES getRole() {
        return role;
    }

    public void setEmail(String email) {
        if (isNotValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email address");
        }
        this.email = email;
    }

    public void setUserName(String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        this.userName = userName;
    }

    public void setRole(ROLES role) {
        this.role = role;
    }

    private boolean isNotValidEmail(String email) {
        return email == null || !email.contains("@") || email.indexOf('@') >= email.length() - 1;
    }

    @Override
    public String toString() {
        return "User {ID = " + ID + ", userName = " + userName + ", email = " + email + ", role = " + role + "}";
    }
}
