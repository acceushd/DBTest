import java.util.Scanner;

public class Main {

    static User[] users = new User[10];

    /**
     * A wrapper class for the command which can be manipulated
     */
    public static class CommandWrapper {
        public STATEMENT command;

        public CommandWrapper(STATEMENT command) {
            this.command = command;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printBuffer();
            String line = scanner.nextLine().trim();
            if (line.startsWith(".")) {
                switch (doMetaCommand(line.substring(1))) {
                    case METACOMMANDSUCCESS:
                        break;
                    case METACOMMANDUNRECOGNIZED:
                        System.out.printf("Unrecognized command: %s%n", line);
                }
            } else {
                CommandWrapper command = new CommandWrapper(STATEMENT.STATEMENT_NULL);
                switch (prepareStatement(line, command)) {
                    case PREPARE_SUCCESS:
                        break;
                    case PREPARE_UNRECOGNIZED_STATEMENT:
                        System.out.printf("Unrecognized keyword at start of '%s'.%n", line);
                        continue;
                }
                execute(command, line);
            }
        }
    }

    /**
     * Prints a Buffer before the input area for the user
     */
    public static void printBuffer() {
        System.out.print("db > ");
    }

    /**
     * It executes the Meta command as given
     *
     * @param command It takes a String as command
     * @return It returns a Statement if the Command was a success or if it was unrecognized
     */
    public static METACOMMANDRESULT doMetaCommand(String command) {
        if (command.contains("exit")) {
            if (command.length() > 4) {
                int index = command.indexOf("t");
                System.out.printf("Message: %s", command.substring(index + 1).trim());
            }
            System.exit(0);
            return METACOMMANDRESULT.METACOMMANDSUCCESS;
        } else {
            return METACOMMANDRESULT.METACOMMANDUNRECOGNIZED;
        }
    }

    /**
     * This function takes a line and a CommandWrapper as Inputs and gives the command the according
     * Statement
     *
     * @param line    This is where the actual command is in
     * @param command A wrapper for the actual command
     * @return It returns whether the command was successfully declared or if it was
     * not recognized
     */
    public static PrepareResult prepareStatement(String line, CommandWrapper command) {
        return switch (line.split(" ")[0]) {
            case "insert" -> {
                command.command = STATEMENT.STATEMENT_INSERT;
                yield PrepareResult.PREPARE_SUCCESS;
            }
            case "select" -> {
                command.command = STATEMENT.STATEMENT_SELECT;
                yield PrepareResult.PREPARE_SUCCESS;
            }
            case "update" -> {
                command.command = STATEMENT.STATEMENT_UPDATE;
                yield PrepareResult.PREPARE_SUCCESS;
            }
            case "delete" -> {
                command.command = STATEMENT.STATEMENT_DELETE;
                yield PrepareResult.PREPARE_SUCCESS;
            }
            case "print" -> {
                command.command = STATEMENT.STATEMENT_PRINT;
                yield PrepareResult.PREPARE_SUCCESS;
            }
            default -> PrepareResult.PREPARE_UNRECOGNIZED_STATEMENT;
        };
    }

    /**
     * Executes the given command
     *
     * @param command The given non-meta command
     * @param line    When the command is input this is where it gets the user
     */
    public static void execute(CommandWrapper command, String line) {
        switch (command.command) {
            case STATEMENT_INSERT -> {
                int index = line.indexOf(" ");
                if (index == -1) {
                    System.out.println("No insert statement");
                } else {
                    insert(line.substring(index).trim());
                }
            }
            case STATEMENT_SELECT -> {
                User user = selectChooser();
                System.out.println("What do you want to do? (delete, print, update)");
                Scanner scanner = new Scanner(System.in);
                CommandWrapper commandNew = new CommandWrapper(STATEMENT.STATEMENT_NULL);
                String newLine = scanner.nextLine();
                switch (prepareStatement(newLine, commandNew)) {
                    case PREPARE_SUCCESS -> execute(commandNew, user);
                    case PREPARE_UNRECOGNIZED_STATEMENT ->
                            System.out.printf("Unrecognized keyword at start of '%s'.%n", line);
                }
            }
            case STATEMENT_DELETE, STATEMENT_UPDATE -> {
                User user = selectChooser();
                execute(command, user);
            }
            case STATEMENT_PRINT -> {
                for (User user : users) {
                    if (user != null) {
                        System.out.println(user);
                    }
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + command);
        }
    }

    /**
     * Executes the command with the given User
     *
     * @param command The given command, can be delete, print update
     * @param user    The given user which should be manipulated
     */
    public static void execute(CommandWrapper command, User user) {
        switch (command.command) {
            case STATEMENT_DELETE -> {
                for (int i = 0; i < users.length; i++) {
                    if (users[i] == user) {
                        users[i] = null;
                    }
                }
            }
            case STATEMENT_PRINT -> System.out.println(user);
            case STATEMENT_UPDATE -> {
                Scanner scanner = new Scanner(System.in);
                System.out.print("New User: ");
                User newUser = userChecker(scanner.nextLine());
                if (newUser == null) {
                    System.out.println("User not found");
                } else {
                    if (!userExists(newUser)) {
                        for (int i = 0; i < users.length; i++) {
                            if (users[i] == user) {
                                users[i] = newUser;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if it is a valid User
     *
     * @param user Gets a line with the user as Input
     * @return Returns the User or null if not correct
     */
    public static User userChecker(String user) {
        String[] inserts = user.split(" ");
        if (inserts.length < 3) {
            System.out.println("Not enough insert statements");
            return null;
        }
        try {
            int id = Integer.parseInt(inserts[0]);
            String name = inserts[1];
            String email = inserts[2];
            if (email == null || !email.contains("@")) {
                System.out.println("Invalid email address");
                return null;
            }
            return new User(id, name, email);
        } catch (NumberFormatException e) {
            System.out.println("Invalid id format, please try again");
        }
        return null;
    }

    /**
     * This function adds a user into the User array
     *
     * @param insert Here is the new User specified
     */
    public static void insert(String insert) {
        User user = userChecker(insert);
        if (user == null) {
            return;
        }
        if (userExists(user)) {
            System.out.println("User already exists");
            return;
        }
        for (int i = 0; i < users.length; i++) {
            if (users[i] == null) {
                users[i] = user;
                break;
            }
        }
        System.out.printf("Inserted successfully: %s%n", user);
    }

    /**
     * Checks if the given User or User-ID is already in use
     *
     * @param user The to be checked User
     * @return Returns false if no User with this ID or exact profile exists
     */
    public static boolean userExists(User user) {
        if (user == null) {
            return false;
        }
        for (User value : users) {
            if (value == user || value.getID() == user.getID()) {
                return true;
            }
        }
        return false;
    }

    /**
     * A chooser class in which the User can choose how they want to choose their
     * wished for user
     *
     * @return Returns the user or a placeholder
     */
    public static User selectChooser() {
        System.out.print("Select a user by ID(1), username(2) or email(3):");
        Scanner scanner = new Scanner(System.in);
        int selector = scanner.nextInt();
        return switch (selector) {
            case 1 -> {
                System.out.println("Please enter a ID:");
                yield select(scanner.nextInt());
            }
            case 2 -> {
                System.out.println("Please enter a username:");
                yield select(scanner.nextLine());
            }
            case 3 -> {
                System.out.println("Please enter a email:");
                yield select(scanner.nextLine());
            }
            default -> new User(0, "null", "null");
        };
    }

    /**
     * It selects a User based on its ID
     *
     * @param id The User-ID
     * @return The selected User or a placeholder
     */
    public static User select(int id) {
        for (User user : users) {
            if (user.getID() == id) {
                return user;
            }
        }
        return new User(0, "null", "null");
    }

    /**
     * It selects a User based on its Username or email address
     *
     * @param usernameOrEmail The Username or Email address
     * @return The selected User or a placeholder
     */
    public static User select(String usernameOrEmail) {
        if (usernameOrEmail.contains("@")) {
            for (User user : users) {
                if (user.getEmail().equals(usernameOrEmail)) {
                    return user;
                }
            }
        } else {
            for (User user : users) {
                if (user.getUserName().equals(usernameOrEmail)) {
                    return user;
                }
            }
        }
        return new User(0, "null", "null");
    }
}