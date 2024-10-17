public class User {
    private int ID;
    private String userName;
    private String email;

    public User(int ID, String userName, String email) {
        this.ID = ID;
        this.userName = userName;
        this.email = email;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "User {ID = " + ID + ", userName = " + userName + ", email = " + email + "}";
    }
}
