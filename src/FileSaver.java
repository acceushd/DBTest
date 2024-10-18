import wrapper.FileWrapper;

import java.io.*;

public class FileSaver {
    /**
     * Creates a new File
     *
     * @return Returns if it was successful
     */
    public boolean fileCreator() {
        String path;
        String separator = File.separator;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("windows")) {
            path = "C:" + separator + "ProgramData" + separator + "DBTest";
        } else if (os.contains("linux") || os.contains("mac") || os.contains("unix") || os.contains("solaris")) {
            path = System.getProperty("user.home") + separator + "DBTest";
        } else {
            System.out.println("Unsupported operating system: " + os);
            return false;
        }
        File folder = new File(path);
        try {
            if (!folder.exists()) {
                if (!folder.mkdirs()) {
                    System.out.println("Unable to create directory: " + path);
                    return false;
                }
            }
            if (isDirectoryEmpty(path)) {
                File newFile = new File(path + separator + "db1.json");
                return newFile.createNewFile();
            } else {
                int number = getHighestDataBaseNumber(path) + 1;
                File newFile = new File(path + separator + "db" + number + ".json");
                return newFile.createNewFile();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean fileCreatorTMP(FileWrapper fileWrapper) {
        String path;
        String separator = File.separator;
        String os = System.getProperty("os.name").toLowerCase();
        String tempDir = System.getProperty("java.io.tmpdir");
        if (os.contains("windows")) {
            path = tempDir + separator + "DBTest";
        } else if (os.contains("linux") || os.contains("mac") || os.contains("unix") || os.contains("solaris")) {
            path = tempDir + separator + "DBTest";
        } else {
            System.out.println("Unsupported operating system: " + os);
            return false;
        }
        File folder = new File(path);
        try {
            if (!folder.exists()) {
                if (!folder.mkdirs()) {
                    System.out.println("Unable to create directory: " + path);
                    return false;
                }
            }
            File newFile = new File(path + separator + "tmp.json");
            if (newFile.exists()) {
                if (!newFile.delete()) {
                    System.err.printf("Unable to delete file: %s%n", newFile.getAbsolutePath());
                }
            }
            fileWrapper.file = newFile;
            return newFile.createNewFile();

        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private boolean isDirectoryEmpty(String directoryPath) {
        File directory = new File(directoryPath);

        // Check if it is a directory
        if (directory.isDirectory()) {
            // List files in the directory
            String[] files = directory.list();
            // Return true if the directory is empty (list is null or has no files)
            return files == null || files.length == 0;
        } else {
            System.out.println("Provided path is not a directory.");
            return false;
        }
    }

    private int getHighestDataBaseNumber(String directoryPath) {
        File directory = new File(directoryPath);
        int highestNumber = -1;
        if (directory.isDirectory()) {
            //list alls files with the correct name scheme
            File[] files = directory.listFiles(((dir, name) -> name.matches("db[0-9]*[1-9].json")));
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    String numberPart = fileName.replaceAll("db", "").replaceAll(".json", "");
                    try {
                        int number = Integer.parseInt(numberPart);
                        if (number > highestNumber) {
                            highestNumber = number;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing number from file name: " + numberPart);
                    }
                }
            }
        } else {
            System.out.println("Provided path is not a directory.");
        }
        return highestNumber;
    }
}
