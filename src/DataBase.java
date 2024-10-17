import enums.METACOMMANDRESULT;
import enums.PREPARERESULT;
import enums.STATEMENT;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A class which creates and runs a little database
 */
public class DataBase {

    ArrayList<User> users = new ArrayList<>();
    FileWrapper file = new FileWrapper(new File(""));

    /**
     * Creates and runs the Database
     */
    public void start() {
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
    public void printBuffer() {
        System.out.print("db > ");
    }

    /**
     * It executes the Meta command as given
     *
     * @param command It takes a String as command
     * @return It returns a Statement if the Command was a success or if it was unrecognized
     */
    public METACOMMANDRESULT doMetaCommand(String command) {
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
    public PREPARERESULT prepareStatement(String line, CommandWrapper command) {
        return switch (line.split(" ")[0]) {
            case "insert" -> {
                command.command = STATEMENT.STATEMENT_INSERT;
                yield PREPARERESULT.PREPARE_SUCCESS;
            }
            case "select" -> {
                command.command = STATEMENT.STATEMENT_SELECT;
                yield PREPARERESULT.PREPARE_SUCCESS;
            }
            case "update" -> {
                command.command = STATEMENT.STATEMENT_UPDATE;
                yield PREPARERESULT.PREPARE_SUCCESS;
            }
            case "delete" -> {
                command.command = STATEMENT.STATEMENT_DELETE;
                yield PREPARERESULT.PREPARE_SUCCESS;
            }
            case "print" -> {
                command.command = STATEMENT.STATEMENT_PRINT;
                yield PREPARERESULT.PREPARE_SUCCESS;
            }
            case "save" -> {
                command.command = STATEMENT.STATEMENT_SAVE;
                yield PREPARERESULT.PREPARE_SUCCESS;
            }
            default -> PREPARERESULT.PREPARE_UNRECOGNIZED_STATEMENT;
        };
    }

    /**
     * Executes the given command
     *
     * @param command The given non-meta command
     * @param line    When the command is input this is where it gets the user
     */
    public void execute(CommandWrapper command, String line) {
        switch (command.command) {
            case STATEMENT_INSERT -> handleInsert(line);
            case STATEMENT_SELECT -> handleSelect(line);
            case STATEMENT_DELETE, STATEMENT_UPDATE -> handleDeleteAndUpdate(command);
            case STATEMENT_PRINT -> printAll();
            case STATEMENT_SAVE -> saveFile();
            default -> throw new IllegalStateException("Unexpected value: " + command);
        }
    }

    /**
     * Executes the command with the given User
     *
     * @param command The given command, can be delete, print update
     * @param user    The given user which should be manipulated
     */
    public void execute(CommandWrapper command, User user) {
        switch (command.command) {
            case STATEMENT_DELETE -> users.remove(user);
            case STATEMENT_PRINT -> System.out.println(user);
            case STATEMENT_UPDATE -> {
                Scanner scanner = new Scanner(System.in);
                System.out.print("New User: ");
                User newUser = userChecker(scanner.nextLine());
                if (newUser == null) {
                    System.out.println("User not found");
                } else {
                    if (!userExists(newUser)) {
                        for (int i = 0; i < users.size(); i++) {
                            if (users.get(i) == user) {
                                users.set(i, newUser);
                            }
                        }
                    }
                }
            }
        }
    }

    private void handleInsert(String line) {
        int index = line.indexOf(" ");
        if (index == -1) {
            System.out.println("No insert statement");
        } else {
            insert(line.substring(index).trim());
        }
    }

    private void handleSelect(String line) {
        User user = selectChooser();
        System.out.println("What do you want to do? (delete, print, update)");
        Scanner scanner = new Scanner(System.in);
        CommandWrapper commandNew = new CommandWrapper(STATEMENT.STATEMENT_NULL);
        String newLine = scanner.nextLine();
        switch (prepareStatement(newLine, commandNew)) {
            case PREPARE_SUCCESS -> execute(commandNew, user);
            case PREPARE_UNRECOGNIZED_STATEMENT -> System.out.printf("Unrecognized keyword at start of '%s'.%n", line);
        }
    }

    private void handleDeleteAndUpdate(CommandWrapper command) {
        User user = selectChooser();
        execute(command, user);
    }

    private void printAll() {
        for (User user : users) {
            if (user != null) {
                System.out.println(user);
            }
        }
    }

    private void saveFile() {
        FileSaver fileSaver = new FileSaver();
        fileSaver.fileCreator(file);
        for (User user : users) {
            if (user != null) {
                if (!fileSaver.fileWriter(user, file)) {
                    System.err.printf("Could not save User %s%n", user);
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
    public User userChecker(String user) {
        String[] inserts = user.split(" ");
        if (inserts.length < 3) {
            System.out.println("Not enough insert statements");
            return null;
        }
        try {
            int id = Integer.parseInt(inserts[0]);
            String name = inserts[1];
            String email = inserts[2];
            if (inserts.length == 4) {
                String role = inserts[3];
                if (email == null || !email.contains("@")) {
                    System.out.println("Invalid email address");
                    return null;
                }
                return new User(id, name, email, role);
            }
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
    public void insert(String insert) {
        User user = userChecker(insert);
        if (user == null) {
            return;
        }
        if (userExists(user)) {
            System.out.println("User already exists");
            return;
        }
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i) == null) {
                users.set(i, user);
                System.out.printf("Inserted successfully: %s%n", user);
                return;
            }
        }
        users.add(user);
        System.out.printf("Inserted successfully: %s%n", user);
    }

    /**
     * Checks if the given User or User-ID is already in use
     *
     * @param user The to be checked User
     * @return Returns false if no User with this ID or exact profile exists
     */
    public boolean userExists(User user) {
        if (user == null) {
            return true;
        }
        for (User value : users) {
            if (value.getID() == user.getID()) {
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
    public User selectChooser() {
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
            default -> null;
        };
    }

    /**
     * It selects a User based on its ID
     *
     * @param id The User-ID
     * @return The selected User or a placeholder
     */
    public User select(int id) {
        for (User user : users) {
            if (user.getID() == id) {
                return user;
            }
        }
        return null;
    }

    /**
     * It selects a User based on its Username or email address
     *
     * @param usernameOrEmail The Username or Email address
     * @return The selected User or a placeholder
     */
    public User select(String usernameOrEmail) {
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
        return null;
    }
}