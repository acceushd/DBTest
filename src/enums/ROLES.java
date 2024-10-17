package enums;

public enum ROLES {
    ADMIN,
    USER;


    public static ROLES which(String role) {
        return switch (role) {
            case "admin" -> ADMIN;
            case "user" -> USER;
            default -> throw new IllegalArgumentException("Invalid role");
        };
    }
}
