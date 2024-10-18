import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class FileSaver {
    /**
     * Creates a new File
     *
     * @return Returns if it was successful
     */
    public boolean fileCreator(FileWrapper fileWrapper, String fileName) {
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
            File newFile = new File(path + separator + fileName + ".json");
            fileWrapper.file = newFile;
            return newFile.createNewFile();

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
            fileWrapper.file = newFile;
            return newFile.createNewFile();

        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
