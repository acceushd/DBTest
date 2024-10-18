import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import wrapper.FileWrapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManipulator {
    public File fileOPen(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        return file;
    }

    public File fileImport(String filePath) {
        return fileOPen(filePath);
    }

    public boolean fileSave() {}

    public boolean fileWriter(User user, FileWrapper file) {
        return filePath(user, file);
    }

    static boolean filePath(User user, FileWrapper file) {
        String path = file.file.getAbsolutePath();
        System.out.println(path);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(user);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path, true))) {
            bufferedWriter.write(json);
            bufferedWriter.write(",");
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }
}
