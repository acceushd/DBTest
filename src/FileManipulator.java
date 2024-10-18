import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import wrapper.FileWrapper;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

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

    public boolean fileSave(FileWrapper fileWrapper, List<User> users) {
        FileSaver fileSaver = new FileSaver();
        if (!fileSaver.fileCreator()) {
            System.err.printf("Was not able to save file: %s%n", fileWrapper);
            return false;
        }
        for (User user : users) {
            try {
                this.fileWriter(user, fileWrapper);
            } catch (Exception e) {
                System.err.printf("Was not able to save file for user: %s%n", e.getMessage());
                return false;
            }
        }
        return true;
    }

    public boolean deleteUser(User user, FileWrapper fileWrapper) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            Reader reader = new FileReader(fileWrapper.file);
            Type listType = new TypeToken<List<Map<String, Object>>>() {
            }.getType();
            List<Map<String, Object>> jsonArray = gson.fromJson(reader, listType);

            if (!jsonArray.removeIf(jsonObject -> jsonObject.containsKey("ID") && (Double) jsonObject.get("ID") == user.getID())) {
                System.out.printf("Couldn't find user %s%n", user);
                return false;
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            return false;
        }
        return true;
    }

    public boolean fileWriter(User user, FileWrapper file) {
        return filePath(user, file);
    }

    public boolean fileWriter(FileWrapper file) {
        return filePath(file);
    }

    private static boolean filePath(User user, FileWrapper file) {
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

    private static boolean filePath(FileWrapper file) {
        String path = file.file.getAbsolutePath();
        System.out.println(path);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path, true))) {
            bufferedWriter.write("[\n]");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }
}
